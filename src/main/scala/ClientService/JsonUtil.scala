package ClientService

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

//Used to write Jobs to out bound feed file.
object JsonUtil {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    def toJson(value: Map[Symbol, Any]): String = {
      toJson(value map { case (k, v) => k.name -> v })
    }

    def toJson(value: Any): String = {
      mapper.writeValueAsString(value)
    }
}

