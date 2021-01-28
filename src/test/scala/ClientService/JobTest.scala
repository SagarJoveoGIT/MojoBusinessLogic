package ClientService

import org.scalatest.funsuite.AnyFunSuite

class JobTest extends AnyFunSuite{

  test("Printing Job Details."){

    val job=new Job(title = Some("HR MANAGER"),
    company = Some("Joveo"),
    city = Some("Redwood City"),
    state = Some("California"),
    country = Some("United States"),
    description = Some("Create a process and manage employee on-boarding, assimilation program for US employees"),
    referencenumber = 10,
    url = Some("https://www.joveo.com/careers"),
    date = Some("2019-05-20"),
    category = Some("Support"))

    val jobDetails="Reference number: 10"+"\n"+
    "Company: Some(Joveo)"+"\n"+
    "Category: Some(Support)"+"\n"+
    "Department: None"+"\n"+
    "Title: Some(HR MANAGER)"+"\n"+
    "Description: Some(Create a process and manage employee on-boarding, assimilation program for US employees)"+"\n"+
    "City: Some(Redwood City)"+"\n"+
    "State: Some(California)"+"\n"+
    "Country: Some(United States)"+"\n"+
    "URL: Some(https://www.joveo.com/careers)"+"\n"+
      "Date: Some(2019-05-20)"+"\n"+
    "Text: None"+"\n"+
    "Valid: Some(true)"+"\n"

    assert(job.printJobDetails().compareTo(jobDetails)===0)
  }
}
