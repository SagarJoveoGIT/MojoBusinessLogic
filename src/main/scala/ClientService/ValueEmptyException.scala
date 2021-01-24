package ClientService

//Not Used in code.
case class ValueEmptyException(data:String) extends Exception(data)
case class ClientNotFoundException(data:String) extends Exception(data)
case class JobsEmptyURLException(data:String) extends Exception(data)