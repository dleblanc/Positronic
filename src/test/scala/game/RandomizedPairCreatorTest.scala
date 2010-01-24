package game

import org.scalatest.matchers._
import org.scalatest.prop.Checkers
import org.scalatest._

import org.scalacheck._
import org.scalacheck.Prop._

class RandomizedPairCreatorTest extends FunSuite with ShouldMatchers {
	val creator = new RandomizedPairCreator()
	
 	 test("getRandomlyFromListExcludingElement returns everything but the one element with scalacheck") {
 		 // TODO: it'd be nice to relate the size of the generated sequence to the pickCount, but this looks ok with the implication (contraint)
		 val prop = forAll(Gen.posInt, Gen.listOfN(100, Gen.posInt)) { (pickCount: Int, fromSequence: List[Int]) => (fromSequence.size >= pickCount) ==>
	 		{
	 			val result = creator.pickRandomElements(fromSequence, pickCount)
				result.forall(x => fromSequence contains x)
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
 				
	 			val result = creator.insertNRandomMatches(1 to listSize toList, nBack, numberOfMatches, fromSequence)
	 			
	 			val pairedWithNBack = (result drop nBack) zip result
	 			
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
	 	 
	 def check(property: Prop) = {
		 if (!Test.check(property).passed) {
			 fail("scalacheck failure, see stdout for details")
		 }
	 }
}