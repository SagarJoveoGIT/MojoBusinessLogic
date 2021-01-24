package ClientRepo

import ClientService.{Client, Job}
import com.mongodb.BasicDBObject
import org.mongodb.scala.Completed
import org.mongodb.scala.model.Filters

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object DatabaseOperations {

  //Inserting client data.
  def insertClientDetail(client: Client):Future[Completed]={
    val data=Database.clientDetails.insertOne(client).toFuture()
    Await.result(data,Duration.Inf)
    data
  }

  //Inserting all jobs.
  def insertJobDetails(jobs:Array[Job]):Future[Completed]={
    val data=Database.jobDetails.insertMany(jobs.toSeq).toFuture()
    Await.result(data,Duration.Inf)
    data
  }

  //Fetching all jobs.
  def fetchAllJobs():Future[Seq[Job]]={
    val data=Database.jobDetails.find().toFuture()
    Await.result(data,Duration.Inf)
    data
  }

  //Fetch client by client id.
  def fetchClient(id:String):Future[Seq[Client]]={
    val client=Database.clientDetails.find(Filters.eq("id",s"$id")).toFuture()
    Await.result(client,Duration.Inf)
    client
  }

  //Fetch jobs by given Filters DB object.
  def fetchQuery(dbObject:BasicDBObject):Future[Seq[Job]]={
    val data=Database.jobDetails.find(dbObject).toFuture()
    Await.result(data,Duration.Inf)
    data
  }
}