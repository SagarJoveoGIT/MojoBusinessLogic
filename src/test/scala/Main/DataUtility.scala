package Main

import ClientService.{Job, JobGroup, Publisher, Rule}
import com.mongodb.{BasicDBList, BasicDBObject}
import org.mongodb.scala.model.Filters

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/*
Data used to test the functionalities.
*/

object DataUtility {

  val jobs = Seq(
    new Job(
      title = Some("CLIENT SUCCESS MANAGER"),
      company = Some("Joveo"),
      city = Some("London"),
      state = Some("London"),
      country = Some("United Kingdom"),
      description = Some("Joveo is driving the next-generation of recruitment tech solutions globally. As our Senior Client Success Manager, you'll be a vital member of the founding team for Joveo Europe, based in London. You'll be earning a competitive salary, and we encourage flexible and remote working. \n\n            As our Senior Customer Success Manager, you'll be responsible for managing multiple customers and serve as their primary Joveo contact and advisor. You'll be empowered to succeed in your client focussed role while working collaboratively with the Global Head of CS and MD for Joveo UK on our regional Client Success strategy. \n           \n            With previous experience as a Client Success Manager you'll thrive in a fast-paced environment and be passionate about using creativity to collaboratively solve problems: helping our awesome customers win!"),
      referencenumber = 1,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-11-24"),
      category = Some("Recruitment"),
      department = Some("Recruitment")),
    new Job(
      title = Some("CUSTOMER SUCCESS MANAGER"),
      company = Some("Joveo"),
      city = Some("Redwood City"),
      state = Some("California"),
      country = Some("United States"),
      description = Some("With brilliant people and innovative AI/ML technologies, Joveo is driving the next-generation of recruitment tech solutions globally. As a Customer Success Manager, you will be a key member of our fast-growing Customer Success Team. You will be responsible for managing multiple customers and serve as their primary Joveo interface, advisor and advocate. The ideal candidate thrives in a fast-paced environment and is passionate about being a customer advocate and a trusted strategic advisor by constantly acquiring and upgrading the required skills and domain knowledge. You build strong and long-lasting relationships with your customers and your efforts will drive customer satisfaction and happiness."),
      referencenumber = 2,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-11-24"),
      category = Some("Recruitment"),
      department = Some("Recruitment")),
    new Job(
      title = Some("Senior Software Engineer"),
      company = Some("Joveo"),
      city = Some("Hyderabad"),
      state = Some("Telangana"),
      country = Some("India"),
      text = Some("s"),
      description = Some("Joveo is an exciting programmatic media start-up with grand ambitions; â€˜a job for everyoneâ€™. Harnessing machine learning and big data, our platform connects our clients with the right candidates for the right roles, faster and more efficiently than ever before. Through brilliant people and innovative technology, we are driving next generation recruitment technology solutions. \n    We have a unique culture which embodies the core values of the company - transparency, teamwork, integrity and ownership. \n     We believe in faster iterations, deploying code into production on a daily basis, heavy focus on customer delight, flat hierarchy, and fun environment.\n    Responsibilities\n    You will be part of a small team, with a large amount of ownership and responsibility for managing things directly.\n    You will ship high quality solutions with a sense of urgency and speed.\n    You will work closely with product managers, other teams, and both internal and external stakeholders, owning a large part of the process from problem understanding to shipping the solution.\n    You will have the freedom to suggest and drive organization-wide initiatives.\n    Requirements\n    4+ Years of Experience as a Development Engineer with Bachelorâ€™s degree in Computer Science, computer engineering, electrical engineering or equivalent work experience\n    Experience with at least one the following languages: Scala, Java, and/or any functional language. We code primarily in Scala, so youâ€™ll be excited to either ramp up on that or continue building awesome things with it.\n    Experience with software engineering best practices (e.g. unit testing, code reviews, design documentation).\n    A blend of product, system, and people knowledge that lets you jump into a fast paced environment and contribute from day one\n    An ability to balance a sense of urgency with shipping high quality and pragmatic solutions\n    Extensive experience working with a large codebase and cross functional team\n    Self-motivation and an enjoyment for a startup environment\n    \n    \n    Benefits \n    Talented and collaborative coworkers who will both push and support you\n    Market competitive salary\n    Stock options\n    Flat Hirerachy\n    26 weeks maternity leave so you can truly bond with your child\n    Paid Leaves\n    Work from home options\n    Sick leaves are never counted at Joveo\n    Every day lunch and snacks\n    Team outings, parties and Unlimited fun"),
      referencenumber = 3,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-11-26"),
      category = Some("Engineering")),
    new Job(
      title = Some("Data Scientist"),
      company = Some("Joveo"),
      city = Some("Hyderabad"),
      state = Some("Telangana"),
      country = Some("India"),
      text = Some("s"),
      description = Some("Joveo is an exciting programmatic media start-up with grand ambitions; â€˜a job for everyoneâ€™. Harnessing machine learning and big data, our platform connects our clients with the right candidates for the right roles, faster and more efficiently than ever before. Through brilliant people and innovative technology, we are driving next generation recruitment technology solutions. \n    We have a unique culture which embodies the core values of the company - transparency, teamwork, integrity and ownership. \n     We believe in faster iterations, deploying code into production on a daily basis, heavy focus on customer delight, flat hierarchy, and fun environment.\n    Responsibilities\n    You will be part of a small team, with a large amount of ownership and responsibility for managing things directly.\n    You will ship high quality solutions with a sense of urgency and speed.\n    You will work closely with product managers, other teams, and both internal and external stakeholders, owning a large part of the process from problem understanding to shipping the solution.\n    You will have the freedom to suggest and drive organization-wide initiatives.\n    Requirements\n    4+ Years of Experience as a Development Engineer with Bachelorâ€™s degree in Computer Science, computer engineering, electrical engineering or equivalent work experience\n    Experience with at least one the following languages: Scala, Java, and/or any functional language. We code primarily in Scala, so youâ€™ll be excited to either ramp up on that or continue building awesome things with it.\n    Experience with software engineering best practices (e.g. unit testing, code reviews, design documentation).\n    A blend of product, system, and people knowledge that lets you jump into a fast paced environment and contribute from day one\n    An ability to balance a sense of urgency with shipping high quality and pragmatic solutions\n    Extensive experience working with a large codebase and cross functional team\n    Self-motivation and an enjoyment for a startup environment\n    \n    \n    Benefits \n    Talented and collaborative coworkers who will both push and support you\n    Market competitive salary\n    Stock options\n    Flat Hirerachy\n    26 weeks maternity leave so you can truly bond with your child\n    Paid Leaves\n    Work from home options\n    Sick leaves are never counted at Joveo\n    Every day lunch and snacks\n    Team outings, parties and Unlimited fun"),
      referencenumber = 4,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-11-27"),
      category = Some("Recruitment")),
    new Job(
      title = Some("Product Manager"),
      company = Some("Joveo"),
      city = Some("Hyderabad"),
      state = Some("Telangana"),
      country = Some("India"),
      text = Some("s"),
      description = Some("Joveo is an exciting programmatic media start-up with grand ambitions; â€˜a job for everyoneâ€™. Harnessing machine learning and big data, our platform connects our clients with the right candidates for the right roles, faster and more efficiently than ever before. Through brilliant people and innovative technology, we are driving next generation recruitment technology solutions. \n    We have a unique culture which embodies the core values of the company - transparency, teamwork, integrity and ownership. \n     We believe in faster iterations, deploying code into production on a daily basis, heavy focus on customer delight, flat hierarchy, and fun environment.\n    Responsibilities\n    You will be part of a small team, with a large amount of ownership and responsibility for managing things directly.\n    You will ship high quality solutions with a sense of urgency and speed.\n    You will work closely with product managers, other teams, and both internal and external stakeholders, owning a large part of the process from problem understanding to shipping the solution.\n    You will have the freedom to suggest and drive organization-wide initiatives.\n    Requirements\n    4+ Years of Experience as a Development Engineer with Bachelorâ€™s degree in Computer Science, computer engineering, electrical engineering or equivalent work experience\n    Experience with at least one the following languages: Scala, Java, and/or any functional language. We code primarily in Scala, so youâ€™ll be excited to either ramp up on that or continue building awesome things with it.\n    Experience with software engineering best practices (e.g. unit testing, code reviews, design documentation).\n    A blend of product, system, and people knowledge that lets you jump into a fast paced environment and contribute from day one\n    An ability to balance a sense of urgency with shipping high quality and pragmatic solutions\n    Extensive experience working with a large codebase and cross functional team\n    Self-motivation and an enjoyment for a startup environment\n    \n    \n    Benefits \n    Talented and collaborative coworkers who will both push and support you\n    Market competitive salary\n    Stock options\n    Flat Hirerachy\n    26 weeks maternity leave so you can truly bond with your child\n    Paid Leaves\n    Work from home options\n    Sick leaves are never counted at Joveo\n    Every day lunch and snacks\n    Team outings, parties and Unlimited fun"),
      referencenumber = 5,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-15"),
      category = Some("Recruitment")),
    new Job(
      title = Some("UX DESIGNER"),
      company = Some("Joveo"),
      city = Some("Hyderabad"),
      state = Some("Telangana"),
      country = Some("India"),
      description = Some("Joveo delivers the most relevant hires in the shortest time to companies around the world. Providing real-time insights at every step of the job seeker journey from click to hire, our AI-powered job advertising platform dynamically manages and optimizes sourcing and applications across all online channels. Powering 20M+ job postings every day, our machine learning continuously identifies success, learns and improves to reach the talent you need, when you need it."),
      referencenumber = 6,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-16"),
      category = Some("Design"),
      department = Some("Design")),
    new Job(
      title = Some("Solutions Engineer"),
      company = Some("Joveo"),
      city = Some("Hyderabad"),
      state = Some("Telangana"),
      country = Some("India"),
      description = Some("Built for ease of use, Joveo replaces the guesswork, complexity,\n            and inefficiency of todayâ€™s recruiting with intelligence, transparency, and power, delivering more relevant candidates, more certainty, more success."),
      referencenumber = 7,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-17"),
      category = Some("Support"),
      department = Some("Support")),
    new Job(
      title = Some("Senior Product Manager"),
      company = Some("Joveo"),
      city = Some("London"),
      state = Some("London"),
      country = Some("United Kingdom"),
      text = Some("s"),
      description = Some("Manage end-to-end product design and development lifecycle: from research, concept formation, development, launch and validation."),
      referencenumber = 8,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-15"),
      category = Some("Support")),
    new Job(
      title = Some("DIGITAL MARKETING MANAGER"),
      company = Some("Joveo"),
      city = Some("Redwood City"),
      state = Some("California"),
      country = Some("United States"),
      text = Some("s"),
      description = Some("Grow new leads and support their journey through all stages of the marketing and sales funnels"),
      referencenumber = 9,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-15"),
      category = Some("Support")),
    new Job(
      title = Some("HR MANAGER"),
      company = Some("Joveo"),
      city = Some("Redwood City"),
      state = Some("California"),
      country = Some("United States"),
      description = Some("Create a process and manage employee on-boarding, assimilation program for US employees"),
      referencenumber = 10,
      url = Some("https://www.joveo.com/careers"),
      date = Some("2019-05-20"),
      category = Some("Support")))

  //Data for testing "get job entity".
  val jsonJobPath = "/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/src/main/scala/Main/jsonExample.json"

  //Data for testing "BuildQuery".
  val basicDBList = new BasicDBList()
  basicDBList.add(Filters.eq("category", "Recruitment"))
  basicDBList.add(Filters.regex("title", "Engineer", "i"))
  val basicDBObject1 = new BasicDBObject("$or", basicDBList)

  val rules = List(Rule.apply("category", "Equals", List("Recruitment")), Rule.apply("title", "Contains", List("Engineer")))
  val ruleType = "Some"

  //Data for testing "get priority map".
  val publishers = List(List(new Publisher("Linked-In", true, Some("/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/Linked-In.json")),
    new Publisher("StackOver-Flow", true, Some("/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/StackOver-Flow.json"))),
    List(new Publisher("GlassDoor", true, Some("/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/Linked-In.json")),
      new Publisher("Monster", true, Some("/Users/sagardevanna/IdeaProjects/MojoBusinessLogic/Monster.json"))))

  val jobsSeq1 = jobs.filter(job => (job.category.get.toLowerCase.compareTo("recruitment") == 0 || job.title.get.toLowerCase.contains("engineer") == true))
  val jobSeq2 = jobs.filter(job => job.category.get.toLowerCase.compareTo("recruitment") != 0)

  val jobSeqList = List(jobsSeq1, jobSeq2)

  val rulesList1 = List(Rule.apply("category", "Equals", List("Recruitment")), Rule.apply("title", "Contains", List("Engineer")))
  val pubsList1 = publishers(0)
  val ruleList2 = List(Rule.apply("category", "NotEquals", List("Recruitment")))
  val pubsList2 = publishers(1)

  val createdDateJobGroup1 = java.time.LocalDateTime.now().toString
  val createdDateJobGroup2 = java.time.LocalDateTime.now().toString

  val jobGroups = List(new JobGroup(rulesList1, pubsList1, 2, createdDateJobGroup1, "Some"),
    new JobGroup(ruleList2, pubsList2, 1, createdDateJobGroup2, "All"))

  val priorityGroupedMap: mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]] =
    scala.collection.mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]]()

  val filtJobs1 = ListBuffer(jobsSeq1).flatten
  val filtJob2 = ListBuffer(jobSeq2).flatten

  priorityGroupedMap.put(2, ListBuffer((ListBuffer(pubsList1).flatten, ListBuffer(filtJobs1).flatten, createdDateJobGroup1)))
  priorityGroupedMap.put(1, ListBuffer((ListBuffer(pubsList2).flatten, ListBuffer(filtJob2).flatten, createdDateJobGroup2)))

  //Data for testing "Duplicate jobs elimination using priorities and created date test".
  val jobsRefPriority1 = Set(3, 6, 7, 8, 9, 10)
  val jobsRefPriority2 = Set(1, 2, 4, 5)

  val uniqueFiltJob1 = jobs.filter(job => jobsRefPriority1.contains(job.referencenumber))
  val uniqueFiltJob2 = jobs.filter(job => jobsRefPriority2.contains(job.referencenumber))


  val priorityGroupedMapDuplicateEliminated: mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]] =
    scala.collection.mutable.HashMap[Int, ListBuffer[(ListBuffer[Publisher], ListBuffer[Job], String)]]()

  priorityGroupedMapDuplicateEliminated.put(2, ListBuffer((ListBuffer(pubsList1).flatten, ListBuffer(uniqueFiltJob2).flatten, createdDateJobGroup1)))
  priorityGroupedMapDuplicateEliminated.put(1, ListBuffer((ListBuffer(pubsList2).flatten, ListBuffer(uniqueFiltJob1).flatten, createdDateJobGroup2)))

  //Data for testing "sort by date".
  val Job1 = new Job(10, date = Some("2019-05-20"))
  val Job2 = new Job(10, date = Some("2019-05-19"))
}
