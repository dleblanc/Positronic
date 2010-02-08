package game

import java.util.Random

class RandomizedPairCreator {
	
	private[this] val randomGenerator = new Random()

	def createRandomizedListWithNMatches[T](size: Int, nBack:Int, numberOfMatches: Int, fromCollection: Array[T]) : List[T] = {
		val randomIndexes = pickRandomElements((0 until size - nBack).toList, numberOfMatches)
		val sortedIndexes = randomIndexes sort((a, b) => a < b)
		
		makeWithDupesAt(sortedIndexes, size, nBack, fromCollection, 0)
	}

	//@TailRec
	def fillOutRandomlyWithNonMatchesNBack[T](listSize: Int, nBack: Int, fromElements: Array[T]):List[T] = listSize match {
		case 0 => Nil
		
		case x if (1 to nBack contains x) => { // TODO: this test is probably slow (is there a bug here?)
			getRandomElement(fromElements) :: fillOutRandomlyWithNonMatchesNBack(listSize - 1, nBack, fromElements)
		}
		
		case _ => {
			val shorterList = fillOutRandomlyWithNonMatchesNBack(listSize - 1, nBack, fromElements)
			val nonMatch = getRandomlyFromListExcludingElement(shorterList(nBack - 1), fromElements)
			nonMatch :: shorterList
		}
	}
	
	
	def insertNRandomMatches[T](originalList: List[T], nBack: Int, numberOfMatches: Int): List[T] = {
		val randomIndexes = pickRandomElements((0 until originalList.size - nBack).toList, numberOfMatches)
		val sortedIndexes = randomIndexes sort((a, b) => a < b)
		
		introduceMatchesAtIndexes(originalList, sortedIndexes, 0, nBack)
	}
	
	// @TailRec
	def introduceMatchesAtIndexes[T](originalList: List[T], sortedIndexes: List[Int], currentIndex: Int, nBack: Int): List[T] = {

		if (sortedIndexes.size == 0) {
			originalList
		}
		else if (sortedIndexes.head == currentIndex) {
			// Shitballs, have to append to the start of this shit
			
			val smallerList = introduceMatchesAtIndexes((originalList.tail take nBack - 1) ::: List(originalList.head) ::: (originalList drop nBack + 1), sortedIndexes.tail, currentIndex + 1, nBack)
			val result = originalList.head :: smallerList
			result
		}
		else {
			originalList.head :: introduceMatchesAtIndexes(originalList.tail, sortedIndexes, currentIndex + 1, nBack) 
		}
	}
	
	// used
	// @TailRec
	def pickRandomElements[T](fromSequence: List[T], pairCount: Int): List[T] = {
		if (pairCount == 0) {
			return Nil
		}
		else {
			val index = randomGenerator.nextInt(fromSequence.size)
		
			return fromSequence(index) :: pickRandomElements(fromSequence.slice(0, index) ::: fromSequence.slice(index + 1, fromSequence.length), pairCount - 1)
		}
	}
	
	// used
	def getRandomlyFromListExcludingElement[T](dontInclude: T, fromList: Array[T]):T = {
		val values = fromList.filter(element => element != dontInclude)
		return values(randomGenerator.nextInt(values.size))
	}
	
	// used
	def getRandomElement[T](fromArray: Array[T]) = fromArray(randomGenerator.nextInt(fromArray.size))
	
	// used
  	 def makeWithDupesAt[T](sortedIndexes: List[Int], length: Int, nBack: Int, fromCollection: Array[T], currentIndex: Int): List[T] = length match {
	  		 case 0 => Nil
	  		 
	  		 case x if (x <= nBack) => getRandomElement(fromCollection) :: makeWithDupesAt(Nil, length - 1, nBack, fromCollection, currentIndex + 1)
	  		 
	  		 case x if (!sortedIndexes.isEmpty && sortedIndexes.head == currentIndex && x > nBack) => {
	  			 val subList = makeWithDupesAt(sortedIndexes.tail, length-1, nBack, fromCollection, currentIndex + 1)
	  			 subList(nBack - 1) :: subList
	  		 }
	  		 
	  		 case _ => {
	  			 val subList = makeWithDupesAt(sortedIndexes, length-1, nBack, fromCollection, currentIndex + 1)
	  			 getRandomlyFromListExcludingElement(subList(nBack - 1), fromCollection) :: subList
	  		 }
  		 }
}