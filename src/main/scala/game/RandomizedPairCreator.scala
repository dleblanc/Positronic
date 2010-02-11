package game

import java.util.Random

class RandomizedPairCreator {
	
	private[this] val randomGenerator = new Random()

	def createRandomizedListWithNMatches[T](size: Int, nBack:Int, numberOfMatches: Int, fromCollection: Array[T]) : List[T] = {
		val randomIndexes = pickRandomElements((0 until size - nBack).toList, numberOfMatches)
		val sortedIndexes = randomIndexes sort((a, b) => a < b)
		
		makeWithDupesAt(sortedIndexes, size, nBack, fromCollection, 0)
	}

	// @TailRec
	def pickRandomElements[T](fromSequence: List[T], pairCount: Int): List[T] = {
		if (pairCount == 0) {
			return Nil
		}
		else if (fromSequence.isEmpty) {
			throw new RuntimeException("ran out of elements before we got the requested number of pairs")
		}
		else {
			val index = randomGenerator.nextInt(fromSequence.size)
		
			return fromSequence(index) :: pickRandomElements(fromSequence.slice(0, index) ::: fromSequence.slice(index + 1, fromSequence.length), pairCount - 1)
		}
	}
	
	def getRandomlyFromListExcludingElement[T](dontInclude: T, fromList: Array[T]):T = {
		val values = fromList.filter(element => element != dontInclude)
		return values(randomGenerator.nextInt(values.size))
	}
	
	def getRandomElement[T](fromArray: Array[T]) = fromArray(randomGenerator.nextInt(fromArray.size))
	
	// @TailRec
  	 def makeWithDupesAt[T](sortedIndexes: List[Int], length: Int, nBack: Int, fromCollection: Array[T], currentIndex: Int): List[T] = length match {
		// TODO: see if I can do this with foldLeft and friends?
	  		 case 0 =>
	  		 	Nil
	  		 
	  		 case x if (x <= nBack) => {
	  		 	getRandomElement(fromCollection) :: makeWithDupesAt(Nil, length - 1, nBack, fromCollection, currentIndex + 1)
	  		 }
	  		 
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