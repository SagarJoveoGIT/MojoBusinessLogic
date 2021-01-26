package Main

import ClientAPI.BusinessAPI
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn
import scala.util.{Failure, Success}

/*
Akka Http server.
*/

object AkkaHttpServer extends LazyLogging with App {

  implicit val system = ActorSystem("Akka-HTTP-REST-Server")
  implicit val materalizer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val host = "127.0.0.1"
  val port = 8080

  val serverUpdateRoute: Route = get {
    complete("Akka HTTP server is up.")
  }

  val inboundFeedRoute=BusinessAPI.inboundFeedRoute()
  val jobGroupRoute=BusinessAPI.renderJobGroups()

  val routes: Route = Directives.concat(jobGroupRoute,inboundFeedRoute, serverUpdateRoute)

    val httpServerFuture = Http().bindAndHandle(routes, host, port)

    httpServerFuture.onComplete {
      case Success(binding) =>
        logger.info(s"Akka Http server is up and is bound to ${binding.localAddress}.")
      case Failure(exception) =>
        logger.info(s"Akka Http server is failed to start.")
    }

  println("Press Enter to shutdown the server.")
  StdIn.readLine()
  httpServerFuture.flatMap(_.unbind()).onComplete(_=>system.terminate())
}
