package ClientAPI

import ClientRepo._
import ClientService._
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.LazyLogging

import java.io.{File, PrintWriter}
import java.nio.file.Files
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/*
Routes for Http requests.
1. inboundFeed Route: For client on-boarding.
2. render jobGroups: Displaying job group of particular client.
*/

object BusinessAPI extends LazyLogging with JsonSupport with Directives {

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()

  //http://localhost:8080/inbound-feed

  def inboundFeedRoute(): Route ={
    path("inbound-feed") {
      post {
        entity(as[ClientData]) { clientData => {

          //Reading all jobs as Job case class.
          val jobs = Utility.getEntity[Job](clientData.inboundFeedUrl)

          //If jobs are available in in-bound feed Url.
          if(!jobs.isEmpty) {

            //Extracting rules for job groups and respective job group publishers.
            val rulePubAssociate: List[RulePubAssociate] = clientData.rulePubAssociates

            //If job groups are available.
            if(!rulePubAssociate.isEmpty){

              //Inserting all jobs in Job details collection.
              DatabaseOperations.insertJobDetails(jobs)

              //Extracting publishers,rules and priorities of job groups.
              val publishers = rulePubAssociate.map(data => data.publishers).map(pubs => {
                pubs.map(pub => pub.copy(outboundFileName = Some(new java.io.File(".").getCanonicalPath + "/" + pub.id + ".json")))
              })

              val priorities = rulePubAssociate.map(data => data.priority)

              val rules = rulePubAssociate.map(data => (data.operators,data.ruleType)).map(ops => {
                (ops._1.map(operator => Rule.apply(operator.attribute, operator.operator, operator.value)),ops._2)
              })

              //Cross checking the job groups are available or not.

              if(!priorities.isEmpty) {

                //Forming job group details.
                val groupsData = for {index <- 0 until rules.size} yield new JobGroup(rules(index)._1, publishers(index), priorities(index), java.time.LocalDateTime.now().toString,rules(index)._2)
                val jobGroups = groupsData.toList

                //Inserting client data.
                DatabaseOperations.insertClientDetail(new Client(clientData.id, clientData.name, clientData.inboundFeedUrl, Some(jobGroups)))

                //Building database queries for each job group.
                val queries = rules.map(data => Utility.buildQuery(data._1,data._2))

                //Fetching jobs for each job group.
                val data = Future.sequence(queries.map(query => DatabaseOperations.fetchQuery(query)))

                onSuccess(data) { jobs => {

                  //Creating map for grouping jobGroups with respect to priorities.
                  val map: mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]] = Utility.getPriorityMap(publishers, jobs, jobGroups)

                  //Eliminating duplicate jobs with respective with priorities and created date of job groups.
                  Utility.duplicateJobEliminationUsingPrioritiesAndCreatedDate(map)

                  //Writing jobs to out bound feed file of publisher which are need to be published by respective publisher.
                  for (value <- map.values) {
                    for (data <- value) {
                      for (pub <- data._1) {
                        val file = new File(pub.outboundFileName.get)

                        if (!Files.exists(file.toPath)) {
                          Files.createFile(file.toPath)
                        }

                        val writer = new PrintWriter(file)
                        writer.write(JsonUtil.toJson(data._2))
                        writer.close()
                      }
                    }
                  }

                  complete(s"${clientData.id} is on-boarded.")
                }
                }
              }
              else {
                complete("Job groups details are not complete.")
              }
            }
            else {
              complete(s"${clientData.name} does not contain job group data.")
            }
          } else{
            complete(s"${clientData.inboundFeedUrl} does not contains jobs.")
          }
        }
        }
      }
    }
  }

  //http://localhost:8080/Job-Groups?id=Client_1

  def renderJobGroups():Route={
    path("Job-Groups"){
      parameters("id".withDefault("client")){id=>{
        get{
          onSuccess(DatabaseOperations.fetchClient(id)){result=>{

            //Checking client exist in DB data or not.
            if(!result.isEmpty){
              val client=result.head

              //Extracting the job groups of client.
              val jobGroups:Option[List[JobGroup]]=client.jobGroups

              //Checking job groups are available or not.
              jobGroups match {
                case Some(value)=>
                case None=>complete("Job groups are not available.")
              }

              //Eliminating option type.
              val groups=List(jobGroups).flatten.flatten

              //Extracting the publishers of job groups.
              val pubs=groups.map(group=>group.sponsoredPublishers)

              //Building queries for rules of respective jog groups.
              val queries=groups.map(group=>Utility.buildQuery(group.rules,group.ruleType))

              //Fetching the jobs for respective job groups.
              val data=Future.sequence(queries.map(query=>DatabaseOperations.fetchQuery(query)))

              //Sorting jobs by date of jobs.
              val jobs=data.map(value=>value.map(data=>data.sortWith((job1,job2)=>Utility.sortByDate(job1,job2))))

              onSuccess(jobs) { group => {

                //Creating map for grouping jobGroups with respect to priorities.
                val map: mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]] = Utility.getPriorityMap(pubs, group, groups)

                //Eliminating duplicate jobs with respective with priorities and created date of job groups.
                Utility.duplicateJobEliminationUsingPrioritiesAndCreatedDate(map)

                //Storing the publishers id's with respective to job groups.
                val publishers = ListBuffer(map.values.map(data1 => data1.map(data2 => data2._1.map(pubs => pubs.id)))).flatten.flatten

                //Storing job group details along with publisher id's and job group sequence number.
                val jobGroupsWithPubs: ListBuffer[(String, ListBuffer[String], Seq[Job])] = new ListBuffer[(String, ListBuffer[String], Seq[Job])]()

                for (index <- 0 until group.size)
                  jobGroupsWithPubs.append(("Job-Group - " + (index+1).toString, publishers(index), group(index)))

                //println(jobGroupsWithPubs)
                //complete(jobGroupsWithPubs)
                complete(s"$id's Job Groups are rendered.")
              }
              }
            }
            else{
              complete(s"$id is not found.")
            }
          }
          }
        }
      }
      }
    }
  }
}
