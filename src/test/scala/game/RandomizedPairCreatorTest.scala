package game

import org.scalatest.matchers._
import org.scalatest.prop.Checkers
import org.scalatest._

import org.scalacheck._
import org.scalacheck.Prop._

// NOTES: Scalacheck should be able to provide better visibility (besides collect), on a per-check basis.

class RandomizedPairCreatorTest extends FunSuite with ShouldMatchers {
	val creator = new RandomizedPairCreator()
	
 	 test("getRandomlyFromListExcludingElement returns everything but the one element with scalacheck") {
 		 // TODO: it'd be nice to relate the size of the generated sequence to the pickCount, but this looks ok with the implication (contraint)
		 val prop = forAll(Gen.posInt, Gen.listOfN(100, Gen.posInt)) { (pickCount: Int, fromSequence: List[Int]) => (fromSequence.size >= pickCount) ==>
	 		{
	 			val result = creator.pickRandomElements(fromSequence, pickCount)
				result.forall(x => fromSequence contains x) && result.size == pickCount
 			}
		 }
		 
		 check(prop)
	 }
 
 	 test("fillOutRandomlyWithNonMatchesNBack does not contain any matches - repeated many times with scalacheck") {
		 val prop = forAll(Gen.choose(1,20), Gen.choose(1,100)) { (nBack: Int, listSize: Int) => (nBack < listSize) ==>
	 		{
	 			val randomWithoutMatches = creator.fillOutRandomlyWithNonMatchesNBack(listSize, nBack, Array("a", "b", "c"))
	 			
	 			val pairs = (randomWithoutMatches drop nBack) zip randomWithoutMatches
	 			
	 			pairs.forall(pair => pair._1 != pair._2) &&
	 				randomWithoutMatches.size == listSize
 			}
		 }
		 
		 check(prop)
	 }

	 test("insertNRandomMatches puts only the specified number of matches in the output with scalacheck") {
		 val fromSequence = List(1, 2, 3)
		 
		 val prop = forAll(Gen.choose(1,20), Gen.choose(1,100), Gen.choose(0, 75)) { (nBack: Int, listSize: Int, numberOfMatches: Int) => 
		 	(nBack < listSize &&
 			nBack > 0 && 
 			listSize > 0 && 
 			numberOfMatches <= (listSize - nBack)) ==> {
 				
	 			val result = creator.insertNRandomMatches(1 to listSize toList, nBack, numberOfMatches)
	 			
	 			val pairedWithNBack = (result drop nBack) zip result
	 			
	 			pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (numberOfMatches)
	 			
	 			(pairedWithNBack.filter(pair => pair._1 == pair._2).size == numberOfMatches) 	:| "number of matches are equal"  &&
 				(result.size == listSize) 														:| "list sizes are equal"
 			}
		 }
		 
		 check(prop)
	 }

 	 test("pickRandomElements should contain all input elements just once with scalacheck") {
 		 
	 	val prop = forAll(Gen.choose(1,20), Gen.choose(10, 40)) { (pickCount: Int, sequenceSize: Int) => 
		 	(pickCount >= 0 && 
 			sequenceSize > 0 && 
 			sequenceSize < 30 &&
 			pickCount < sequenceSize) ==> {
 				
		 		val fromList = 1 to sequenceSize toList
	 			val randomElementList = creator.pickRandomElements(fromList, pickCount)
	 			
	 			val sortedVals = randomElementList.sort(_ < _)
	 			val dupesFound = List.exists2(sortedVals, sortedVals drop 1) (_ == _)
	 			
	 			!dupesFound &&
	 			randomElementList.forall(fromList contains _) 												:| "random elements are all from the input list" &&
	 			(randomElementList.size == pickCount) 														:| "list sizes are equal"
 			}
		 }
		 
		 check(prop)
	 }
// 	 
//  	 test("sanity test for fillOutRandomlyWithNonMatchesNBack") {
// 		 for(i <- 1 to 10) {
// 			 val randomStuff = creator.fillOutRandomlyWithNonMatchesNBack(5, 1, Array("a", "b"))
//
//	 		 println(randomStuff)
//	 		 
// 			val pairedWithNBack = (randomStuff drop 1) zip randomStuff
//	 			
// 			pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (0)
// 		 }
// 	 }
//  	 
//  	 test("sanity test for insertNRandomMatches") {
// 		 for(i <- 1 to 10) {
// 			 val randomStuff = creator.insertNRandomMatches(List("a", "b", "a", "b"), 1, 2)
//
//	 		 println(randomStuff)
//	 		 
// 			val pairedWithNBack = (randomStuff drop 1) zip randomStuff
//	 			
// 			pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (2)
// 		 }
// 	 }
//
//  	 test("sanity test for createRandomizedListWithNMatches") {
// 		 for(i <- 1 to 10) {
// 			 val randomStuff = creator.createRandomizedListWithNMatches(5, 1, 2, Array("a", "b"))
//
//	 		 println(randomStuff)
//	 		 
// 			 val pairedWithNBack = (randomStuff drop 1) zip randomStuff
//	 			
// 			 pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (2)
// 		 }
// 	 }
  	 
  	 test("make n dupes with one element make a list one element long") {
  		 val result = creator.makeWithDupesAt(Nil, 1, 1, Array("a"), 0)
  		 
  		 result should equal(List("a"))
  	 }

   	 test("make n dupes with two elements has two duplicated elements") {
  		 val result = creator.makeWithDupesAt(List(0), 2, 1, Array("a", "b"), 0)
  		 
  		 result(0) should equal(result(1))
  	 }

   	 
   	 test("make n dupes with 4 elements 1 back and no random indexes has no duplicates") {
  		 val result = creator.makeWithDupesAt(Nil, 4, 1, Array("a", "b"), 0)
  		 
  		 // TODO: remove the duplication here
		val pairedWithNBack = (result drop 1) zip result
		pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (0)
  	 }

	 test("make n dupes with 4 elements 2 back at index 1 has only one dupe") {
  		val result = creator.makeWithDupesAt(List(1), 4, 2, Array("a", "b"), 0)

  		result(1) should equal(result(3))

  		val pairedWithNBack = (result drop 2) zip result
		pairedWithNBack.filter(pair => pair._1 == pair._2).size should equal (1)
  	 }

 	 test("createRandomizedListWithNMatches puts only the specified number of matches in the output with scalacheck") {
 		 // TODO: test failures kind of hard to read with generic-ass tests
		 val fromSequence = Array(1, 2, 3)
		 
		 val prop = forAll(Gen.choose(1,20), Gen.choose(1,100), Gen.choose(0, 75)) { (nBack: Int, listSize: Int, numberOfMatches: Int) => 
		 	(nBack < listSize &&
 			nBack > 0 && 
 			listSize > 0 && 
 			numberOfMatches <= (listSize - nBack) &&
 			numberOfMatches > 0
 			) ==> {
 				
 				val result = creator.createRandomizedListWithNMatches(listSize, nBack, numberOfMatches, 1 to 5 toArray)
	 			
	 			val pairedWithNBack = (result drop nBack) zip result
 				val equalMatches = pairedWithNBack.filter(pair => pair._1 == pair._2)
	 			//equalMatches.size should equal (numberOfMatches)
 				if (equalMatches.size != numberOfMatches) {
 					println("\n# of matches: " + equalMatches.size + " != to: " + numberOfMatches + "\n")
 				}
	 			
	 			(equalMatches.size == numberOfMatches) 	:| "number of matches are equal"  &&
 				(result.size == listSize) 				:| "list sizes are equal"
 			}
		 }
		 
		 check(prop)
	 }
 	 
 	 test("createRandomizedListWithNMatches with three matches at random indexes yields correct result") {
 		 val nBack = 1
 		 val numberOfMatches = 5
 		 val listSize = 10
		val result = creator.createRandomizedListWithNMatches(listSize, nBack, numberOfMatches, 1 to 5 toArray)
		
		val pairedWithNBack = (result drop nBack) zip result
		val equalMatches = pairedWithNBack.filter(pair => pair._1 == pair._2)
		
		println("result: " + result)
		
		equalMatches.size should equal (numberOfMatches)
		result.size should equal (listSize)
	 }
 	 
	 def check(property: Prop) = {
		 if (!Test.check(property).passed) {
			 fail("scalacheck failure, see stdout for details")
		 }
	 }
}