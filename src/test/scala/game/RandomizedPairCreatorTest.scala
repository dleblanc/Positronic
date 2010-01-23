package game

import org.scalatest.matchers._
import org.scalatest._

// BIG TODO: use scalacheck here, perfectly suited to this (no?)

class RandomizedPairCreatorTest extends FunSuite with ShouldMatchers {
	val creator = new RandomizedPairCreator()

	// TODO: I don't think I need this any more
	test("Gets exaustive list of random elements returns all members") {
		val fromSequence = List("a", "b")
		val pairCount = 2
		
		val result = creator.pickRandomElements(fromSequence, pairCount)
		val sortedResult = result sort((a, b) => a < b)
		
		sortedResult should equal (List("a", "b"))
	}
	
	// I think I should test the pickRandomElements method more, but not sure how to.

	 object ExampleEnumeration extends Enumeration {
		 val FIRST, SECOND, THIRD = Value
	 }

	 test("getRandomlyFromListExcludingElement returns everything but the one element") {

		 // Don't like random testing!
		 var randomEnums = Set[ExampleEnumeration.Value]()
		 for (i <- 0 to 30) {
			 val result = creator.getRandomlyFromListExcludingElement(ExampleEnumeration.SECOND, ExampleEnumeration.elements.toList.toArray) // How silly is that?
			 randomEnums = randomEnums + result
		 }
		 randomEnums should contain (ExampleEnumeration.FIRST)
		 randomEnums should not contain (ExampleEnumeration.SECOND)
		 randomEnums should contain (ExampleEnumeration.THIRD)
	 }
	 
	 test("fillOutRandomlyWithNonMatchesNBack does not contain any matches - repeated many times") {
		 val listSize = 20
		 val nBack = 3
		 
		 val testFunc = () => {
			 val randomWithoutMatches = creator.fillOutRandomlyWithNonMatchesNBack(listSize, nBack, Array("a", "b", "c"))
			 randomWithoutMatches.size should equal(listSize)
			 
			 for (i <- nBack until listSize) {
				 if (randomWithoutMatches(i) == randomWithoutMatches(i - nBack)) {
					 fail("index at i=" + i + " matches i=" + (i - nBack))
				 }
			 }
		 }
		 
		 for (i <- 1 to 30) testFunc()
	 }
	 
	 test("insertNRandomMatches puts two matches with nback=1 into the list") {
		 val fromCollection = List("1", "2")
		 val nonMatchingList = List("a", "b", "c")
		 val nBack = 1
		 val numberOfMatches = 2
		 
		 val result = creator.insertNRandomMatches(nonMatchingList, nBack, numberOfMatches, fromCollection)
		 result(0) should equal (result(1))
		 result(1) should equal (result(2))
	 }
}