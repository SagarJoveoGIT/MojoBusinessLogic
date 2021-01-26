package ClientService

import com.mongodb.BasicDBList
import org.mongodb.scala.model.Filters

import java.util


sealed trait Rule{
  def appendRule(dbList: BasicDBList): Unit
}

final case class Equals(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.eq(attribute,value))
  }
}


final case class NotEquals(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.ne(attribute,value))
  }
}

final case class In(attribute:String, value:List[String]) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    val data=new util.ArrayList[String]()

    for(v<-value)
      data.add(v)

    dbList.add(Filters.in(attribute,data))
  }
}

final case class NotIn(attribute:String, value:List[String]) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    val data=new util.ArrayList[String]()

    for(v<-value)
      data.add(v)

    dbList.add(Filters.nin(attribute,value))
  }
}

final case class Contains(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.regex(attribute,value,"i"))
  }
}

final case class NotContains(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.nor(Filters.regex(attribute,value,"i")))
  }
}


final case class StartsWith(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.regex(attribute,"^"+value,"i"))
  }
}

final case class NotStartsWith(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.not(Filters.regex(attribute,"^"+value,"i")))
  }
}


final case class EndsWith(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.regex(attribute,value+"$","i"))
  }
}

final case class NotEndsWith(attribute:String, value:String) extends Rule{
  override def appendRule(dbList: BasicDBList): Unit = {
    dbList.add(Filters.not(Filters.regex(attribute,value+"$","i")))
  }
}

//Factory pattern for creating object of type rule base operator string.
object Rule{

  def apply(attribute:String,operator:String,value:List[String]):Rule= {

    if(value.isEmpty)
      throw ValueEmptyException("Rules value can not be empty.")

    if(operator.toLowerCase.compareTo("notequals")==0)
      return new NotEquals(attribute,value(0))
    else if(operator.toLowerCase.compareTo("in")==0)
      return new In(attribute,value)
    else if(operator.toLowerCase.compareTo("notin")==0)
      return new NotIn(attribute,value)
    else if(operator.toLowerCase.compareTo("contains")==0)
      return new Contains(attribute,value(0))
    else if(operator.toLowerCase.compareTo("notcontains")==0)
      return new NotContains(attribute,value(0))
    else if(operator.toLowerCase.compareTo("startswith")==0)
      return new StartsWith(attribute,value(0))
    else if(operator.toLowerCase.compareTo("notstartswith")==0)
      return new NotStartsWith(attribute,value(0))
    else if(operator.toLowerCase.compareTo("endswith")==0)
      return new EndsWith(attribute,value(0))
    else if(operator.toLowerCase.compareTo("notendswith")==0)
      return new NotEndsWith(attribute,value(0))

    return new Equals(attribute,value(0))
  }

}