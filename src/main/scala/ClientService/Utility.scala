package ClientService

import com.mongodb.{BasicDBList, BasicDBObject}
import org.json4s.jackson.JsonMethods.parse

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




object Utility extends App with JsonSupport {

  //Extracting the jobs from given json file path.
  def getEntity[T](path: String)(implicit m: Manifest[T]): Array[T] = {
    val entitySrc = scala.io.Source.fromFile(path)
    val entityStr = try entitySrc.mkString finally entitySrc.close()
    val data = parse(entityStr).children

    val N = data.size
    val jobs: Array[T] = new Array[T](N)

    for (index <- 0 until N)
      jobs(index) = data(index).extract[T]

    return jobs
  }

  //Building Filters for given set of rules.
  def buildQuery(rules:List[Rule],ruleType:String):BasicDBObject={
    val queryList=new BasicDBList()

    for(rule<-rules)
      rule.appendRule(queryList)

    if(ruleType.compareTo("Some")==0)
      return new BasicDBObject("$or",queryList)

    return new BasicDBObject("$and",queryList)
  }

  //Grouping job groups according to their priority.
  def getPriorityMap(publishers:List[List[Publisher]],jobs:List[Seq[Job]],jobGroups: List[JobGroup]):
  Map[Int,List[(List[Publisher],List[Job],String)]]={

    val createdDates=jobGroups.map(group=>group.createdDate)
    val pubToJobs:List[(List[Publisher], List[Job],String)]=
      (for{index<-0 until publishers.size} yield (publishers(index),List(jobs(index)).flatten,createdDates(index))).toList

    val map=(for{index<-0 until jobGroups.size} yield (jobGroups(index).priority->pubToJobs(index))).toList.groupBy(_._1).
            mapValues(value=>value.map(Val=>Val._2)).toMap
    
    return map
  }

  def sortByCreatedDate(jobGroup1:(List[Publisher],List[Job],String),jobGroup2:(List[Publisher],List[Job],String)):Boolean={

    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

    val jobGroup1Date=LocalDateTime.parse(jobGroup1._3,format)
    val jobGroup2Date=LocalDateTime.parse(jobGroup2._3,format)

    if(jobGroup1Date.isBefore(jobGroup2Date))
      return true

    return false
  }

  //Eliminating duplicate jobs with respective to job group priority and created date.
  def duplicateJobEliminationUsingPrioritiesAndCreatedDate(groupedMap:Map[Int,List[(List[Publisher],List[Job],String)]]):
  Map[Int,List[(List[Publisher],List[Job],String)]]={

    val sortedMapByCreatedDate=groupedMap.map(data=>(data._1,data._2.sortWith((group1,group2)=>sortByCreatedDate(group1,group2))))

    val keys=sortedMapByCreatedDate.keys

    val perPriorityDuplicateJobEliminatedMap=sortedMapByCreatedDate.map(data=>(data._1->data._2.map(values=>(values._1,values._2.filter(job=>{
        !Set(sortedMapByCreatedDate.get(data._1) match {
        case Some(groupList)=> {
          groupList.filter(group =>(!(group.equals(values)))).map(group => group._2).flatten
        }
      }).flatten.contains(job)
    }),values._3))))

    val duplicateJobEliminatedMap=perPriorityDuplicateJobEliminatedMap.map(data=>(data._1->data._2.map(values=>(values._1,values._2.filter(job=>{
      !Set(for{key<-keys if(key<data._1)} yield perPriorityDuplicateJobEliminatedMap.get(key) match {
        case Some(value)=>value.map(group=>group._2).flatten
      }).flatten.flatten.contains(job)
    }),values._3))))

    return duplicateJobEliminatedMap
  }

  //Function to sort jobs by their date.
  def sortByDate(job1:Job,job2:Job):Boolean={

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

    job1.date match {
      case Some(date1)=>{
        job2.date match {
          case Some(date2)=>{
            val job1Date=format.parse(date1)
            val job2Date=format.parse(date2)

            if(job1Date.before(job2Date))
              return true
          }
          case None=>return true
        }
      }
      case None=> return false
    }

    return false
  }

}
