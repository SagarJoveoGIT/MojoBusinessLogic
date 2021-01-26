package ClientService


case class Client(id: String, name: String, inboundFeedUrl: String, jobGroups: Option[List[JobGroup]] = None)
