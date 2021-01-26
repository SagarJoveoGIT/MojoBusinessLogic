package ClientService

import Main.DataUtility
import org.scalatest.funsuite.AnyFunSuite

/*
Class to test Utility functions.
*/

class UtilityTest extends AnyFunSuite{

  test("GetJobEntity"){
    val jobs=Utility.getEntity[Job](DataUtility.jsonJobPath)

    for(index<-0 until jobs.size) {
      val d1=jobs(index).printJobDetails()
      val d2=DataUtility.jobs(index).printJobDetails()

      assert(jobs(index).printJobDetails().compareTo(DataUtility.jobs(index).printJobDetails())===0)
    }
  }

  test("BuildQuery"){

    val basicDBObject1=DataUtility.basicDBObject1
    val basicDBObject2=Utility.buildQuery(DataUtility.rules,DataUtility.ruleType)

    assert(basicDBObject1.keySet().size()===basicDBObject2.keySet().size())
    assert(basicDBObject1.keySet().containsAll(basicDBObject2.keySet())===true)

    for(key<-basicDBObject1.keySet().toArray()){
      assert(basicDBObject1.get(key).equals(basicDBObject2.get(key))==true)
    }
  }

  test("GetPriorityMap"){
    val map=Utility.getPriorityMap(DataUtility.publishers,DataUtility.jobSeqList,DataUtility.jobGroups)
    assert(map.equals(DataUtility.priorityGroupedMap)===true)
  }

  test("Duplicate jobs elimination using priorities and created date"){
    val map=Utility.getPriorityMap(DataUtility.publishers,DataUtility.jobSeqList,DataUtility.jobGroups)
    Utility.duplicateJobEliminationUsingPrioritiesAndCreatedDate(map)
    assert(map.equals(DataUtility.priorityGroupedMapDuplicateEliminated)===true)
  }

  test("JobSortByDate"){
    assert(Utility.sortByDate(DataUtility.Job1,DataUtility.Job2)===false)
  }
}
