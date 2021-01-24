package ClientService

import de.heikoseeberger.akkahttpjson4s.Json4sSupport

//Json support for case class.
trait JsonSupport extends Json4sSupport {
  implicit val Serialization = org.json4s.native.Serialization
  implicit val Json4sFormats = org.json4s.DefaultFormats
}
