package ClientRepo

import ClientService.{Client, Job}
import com.mongodb.BasicDBObject
import org.mongodb.scala.model.Filters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/*
Database operations.
1. Inserting and fetching clients.
2. Inserting and fetching jobs.
*/

object DatabaseOperations {

  //Inserting client data.
  def insertClientDetail(client: Client):Future[Client]={
    Database.clientDetails.insertOne(client).toFuture()
    Future(client)
  }

  //Inserting all jobs.
  def insertJobDetails(jobs:Array[Job]):Future[Array[Job]]={
    Database.jobDetails.insertMany(jobs.toSeq).toFuture()
    Future(jobs)
  }

  //Fetch client by client id.
  def fetchClient(id:String):Future[Seq[Client]]={
    val client=Database.clientDetails.find(Filters.eq("id",s"$id")).toFuture()
    client
  }

  //Fetch jobs by given Filters DB object.
  def fetchQuery(dbObject:BasicDBObject):Future[Seq[Job]]={
    val data=Database.jobDetails.find(dbObject).toFuture()
    data
  }
}
