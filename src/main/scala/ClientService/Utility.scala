package ClientService

import com.mongodb.{BasicDBList, BasicDBObject}
import org.json4s.jackson.JsonMethods.parse

import java.text.SimpleDateFormat
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

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
  mutable.HashMap[Int,ListBuffer[(ListBuffer[Publisher],ListBuffer[Job],String)]]={

    val pubToJobs:ListBuffer[(ListBuffer[Publisher], ListBuffer[Job],String)]=new ListBuffer[(ListBuffer[Publisher], ListBuffer[Job],String)]
    val createdDates=jobGroups.map(group=>group.createdDate)

    for(index<-0 until publishers.size) {
      val d1=ListBuffer(publishers(index)).flatten
      val d2= ListBuffer(jobs(index)).flatten
      val d3=createdDates(index)
      pubToJobs.append((d1,d2,d3))
    }

    val map:mutable.HashMap[Int,ListBuffer[(ListBuffer[Publisher],ListBuffer[Job],String)]]=
      scala.collection.mutable.HashMap[Int,ListBuffer[(ListBuffer[Publisher],ListBuffer[Job],String)]]()

    for(index<-0 until jobGroups.size){
      if(!map.contains(jobGroups(index).priority))
        map.put(jobGroups(index).priority,new ListBuffer[(ListBuffer[Publisher],ListBuffer[Job],String)]())

      val data=map.get(jobGroups(index).priority)
      data.get+=pubToJobs(index)
    }

    return map
  }

  //Eliminating duplicate jobs with respective to job group priority and created date.
  def duplicateJobEliminationUsingPrioritiesAndCreatedDate(map:mutable.HashMap[Int,ListBuffer[(ListBuffer[Publisher],ListBuffer[Job],String)]]):Unit={
    val priorities=map.keys.toList.sorted.reverse
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S")


    for(index<-0 until priorities.size){
      val currentGroup=map.get(priorities(index))

      currentGroup match {
        case Some(value)=>{
          for(i<-0 until value.size){
            for(j<-i+1 until value.size){

              val duplicateJobs1:ListBuffer[Job]=new ListBuffer[Job]()

              for(job1<-value(i)._2){

                val duplicateJobs2:ListBuffer[Job]=new ListBuffer[Job]()

                for(job2<-value(j)._2){
                  if(job1.referencenumber==job2.referencenumber) {
                    val d1=format.parse(value(i)._3)
                    val d2=format.parse(value(j)._3)

                    if(d1.compareTo(d2)>=0)
                      duplicateJobs1.append(job1)
                    else
                      duplicateJobs2.append(job2)
                  }
                }
                value(j)._2--=duplicateJobs2
              }
              value(i)._2--=duplicateJobs1
            }

            for(nextIndex<-index+1 until priorities.size){
              val nextGroup=map.get(priorities(nextIndex))

              nextGroup match{
                case Some(nextValue)=>{
                  for(k<-0 until nextValue.size){

                    val duplicateJobs:ListBuffer[Job]=new ListBuffer[Job]
                    for(job1<-value(i)._2){
                      for(job2<-nextValue(k)._2){
                        if(job1.referencenumber==job2.referencenumber)
                          duplicateJobs.append(job1)
                      }
                    }
                    value(i)._2--=duplicateJobs
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  //Function to sort jobs by their date.
  def sortByDate(job1:Job,job2:Job):Boolean={

    if(job1.date==None)
      return false

    if(job2.date==None)
      return true

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

    val date1=format.parse(job1.date.get)
    val date2=format.parse(job2.date.get)

    if(date1.before(date2))
      return true

    return false
  }

}
