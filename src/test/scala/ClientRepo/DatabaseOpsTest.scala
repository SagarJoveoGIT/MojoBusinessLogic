package ClientRepo

import ClientService.Client
import org.mongodb.scala.Completed
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ClientService._
import Main.DataUtility

class DatabaseOpsTest extends AnyFunSuite{

  test("Inserting Client"){
    val data=DatabaseOperations.insertClientDetail(
      new Client("Client_1","BLI-1","/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/src/main/scala/Main/jsonExample.json",Option(DataUtility.jobGroups)))

    data.onComplete{
      case Success(value)=>(value===Completed)
      case Failure(exception)=>assert(exception===Completed)
    }
  }

  test("Inserting Jobs"){
    val data=DatabaseOperations.insertJobDetails(DataUtility.jobs.toArray)

   data.onComplete{
     case Success(value)=>(value===Completed)
     case Failure(exception)=>assert(exception===Completed)
   }
  }

  test("Fetch Client"){
    val clients=DatabaseOperations.fetchClient("Client_1")

    clients.onComplete{
      case Success(value)=>{
        for(client<-value)
          assert(client.id.compareTo("Client_1")===0)
      }
      case Failure(exception)=>assert(exception===Completed)
    }
  }

  test("Fetch Query"){
    val query=Utility.buildQuery(DataUtility.rulesList1,DataUtility.ruleType)
    val jobs=DatabaseOperations.fetchQuery(query)

    jobs.onComplete{
      case Success(value)=>assert(value.equals(DataUtility.jobsSeq1)==true)
      case Failure(exception)=>assert(exception===Completed)
    }
  }

}
