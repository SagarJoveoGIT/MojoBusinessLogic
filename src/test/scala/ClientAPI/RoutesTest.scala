package ClientAPI

import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/*
Class to test Routes.
*/

class RoutesTest extends AnyWordSpec with Matchers with ScalatestRouteTest{

  val clientOnboardRoute=BusinessAPI.inboundFeedRoute()

  val jsonClientData =
    """
      {
      |  "id": "Client_1",
      |  "name": "BLI1",
      |  "inboundFeedUrl": "/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/src/main/scala/Main/jsonExample.json",
      |  "rulePubAssociates": [
      |    {
      |      "operators":[
      |        {
      |          "attribute": "country",
      |          "operator": "Equals",
      |          "value": ["India"]
      |        },
      |        {
      |          "attribute": "category",
      |          "operator": "Equals",
      |          "value": ["Recruitment"]
      |        }
      |      ],
      |      "publishers":[
      |        {
      |          "id": "Stack-OverFlow",
      |          "isActive": true
      |        }
      |      ],
      |      "priority":2
      |    },
      |    {
      |      "operators":[
      |        {
      |          "attribute": "country",
      |          "operator": "NotEquals",
      |          "value": ["India"]
      |        }
      |      ],
      |      "publishers":[
      |        {
      |          "id": "Linked-In",
      |          "isActive": true
      |        }
      |      ],
      |      "priority":1
      |    }
      |  ]
      |}""".stripMargin

  "inbound feed route" should {
    "On-boarding client using the inbound feed route using /inbound-feed path." in {

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "http://localhost:8080/inbound-feed",
        entity = HttpEntity(MediaTypes.`application/json`, jsonClientData))

      postRequest~>clientOnboardRoute~>check {
        responseAs[String] shouldEqual "\"Client_1 is on-boarded.\""
      }
    }
  }

  val renderJbGroupsRoute=BusinessAPI.renderJobGroups()

  "Render Job Groups Route" should {
    "Client View using /Job-Groups?id=Client_1" in {
      Get("http://localhost:8080/Job-Groups?id=Client_1") ~> renderJbGroupsRoute ~> check {
        responseAs[String] shouldEqual "\"Client_1's Job Groups are rendered.\""
      }
    }
  }

}
