package ClientService

case class Operator(attribute:String,operator:String,value:List[String])
case class RulePubAssociate(operators:List[Operator],publishers: List[Publisher],priority:Int=10,ruleType:String="All")
case class ClientData(id: String, name: String, inboundFeedUrl: String,rulePubAssociates: List[RulePubAssociate])
