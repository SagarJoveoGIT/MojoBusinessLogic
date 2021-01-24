package ClientRepo

import ClientService._
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.{DEFAULT_CODEC_REGISTRY, Macros}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

object Database {

  private val clientCodecProvider: CodecProvider = Macros.createCodecProvider(classOf[Client])
  private val jobGroupCodecProvider: CodecProvider = Macros.createCodecProvider(classOf[JobGroup])
  private val jobCodecProvider: CodecProvider = Macros.createCodecProvider(classOf[Job])
  private val publisherCodecProvider: CodecProvider = Macros.createCodecProvider(classOf[Publisher])
  private val clientDataCodecProvider:CodecProvider=Macros.createCodecProvider(classOf[ClientData])
  private val operationsCodecProvider:CodecProvider=Macros.createCodecProvider(classOf[Operator])
  private val rulePubAssociateCodecProvider:CodecProvider=Macros.createCodecProvider(classOf[RulePubAssociate])


  private val customCodec = fromProviders(rulePubAssociateCodecProvider,operationsCodecProvider,clientDataCodecProvider,Rule.codecProvider,clientCodecProvider, jobGroupCodecProvider, jobCodecProvider, publisherCodecProvider)

  private val codecRegistries = fromRegistries(customCodec, DEFAULT_CODEC_REGISTRY)

  private val DB: MongoDatabase = MongoClient().getDatabase("ClientManagement").withCodecRegistry(codecRegistries)
  val clientDetails: MongoCollection[Client] = DB.getCollection("ClientDetails")
  val jobDetails: MongoCollection[Job] = DB.getCollection("JobDetails")
}
