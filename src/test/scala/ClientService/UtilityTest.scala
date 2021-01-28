package ClientService

import Main.DataUtility
import org.scalatest.funsuite.AnyFunSuite

/*
Class to test Utility functions.
*/

class UtilityTest extends AnyFunSuite{

  test("Get Job Entities from in-bound url file path."){
    val jobs=Utility.getEntity[Job](DataUtility.jsonJobPath)

    for(index<-0 until jobs.size) {
      assert(jobs(index).printJobDetails().compareTo(DataUtility.jobs(index).printJobDetails())===0)
    }
  }

  test("Building Query using rules and rule type."){

    val basicDBObject1=DataUtility.basicDBObject1
    val basicDBObject2=Utility.buildQuery(DataUtility.rules,DataUtility.ruleType)

    assert(basicDBObject1.keySet().size()===basicDBObject2.keySet().size())
    assert(basicDBObject1.keySet().containsAll(basicDBObject2.keySet())===true)

    for(key<-basicDBObject1.keySet().toArray()){
      assert(basicDBObject1.get(key).equals(basicDBObject2.get(key))==true)
    }
  }

  test("Get priority wise grouped job groups map using publishers,jobs and job groups."){
    val map=Utility.getPriorityMap(DataUtility.publishers,DataUtility.jobSeqList,DataUtility.jobGroups)
    assert(map.equals(DataUtility.priorityGroupedMap)===true)
  }

  test("Sorting by Created Date."){
    val jobGroupData1=(DataUtility.pubsList1,DataUtility.filtJobs1,java.time.LocalDateTime.now().toString)
    val jobGroupData2=(DataUtility.pubsList2,DataUtility.filtJob2,java.time.LocalDateTime.now().toString)

    assert(Utility.sortByCreatedDate(jobGroupData1,jobGroupData2)==true)
  }

  test("Duplicate jobs elimination using priorities and created date"){
    val map=Utility.getPriorityMap(DataUtility.publishers,DataUtility.jobSeqList,DataUtility.jobGroups)
    val duplicateEliminatedMap=Utility.duplicateJobEliminationUsingPrioritiesAndCreatedDate(map)
    assert(duplicateEliminatedMap.equals(DataUtility.priorityGroupedMapDuplicateEliminated)===true)
  }

  test("Sorting jobs By date."){
    assert(Utility.sortByDate(DataUtility.Job1,DataUtility.Job2)===false)
  }
}
