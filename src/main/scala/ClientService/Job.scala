package ClientService


case class Job(referencenumber: Int, company: Option[String] = None, category: Option[String] = None, department: Option[String] = None,
               title: Option[String] = None, description: Option[String] = None, city: Option[String] = None, state: Option[String] = None, country: Option[String] = None,
               url: Option[String] = None, date:Option[String] =None,text: Option[String] = None, valid: Option[Boolean] = Some(true))
{

  def printJobDetails(): String = {
    return "Reference number: " + referencenumber + "\n" +
      "Company: " + company + "\n" +
      "Category: " + category + "\n" +
      "Department: " + department + "\n" +
      "Title: " + title + "\n" +
      "Description: " + description + "\n" +
      "City: " + city + "\n" +
      "State: " + state + "\n" +
      "Country: " + country + "\n" +
      "URL: " + url + "\n" +
      "Date: " + date + "\n" +
      "Text: " + text + "\n" +
      "Valid: " + valid + "\n"
  }

}


