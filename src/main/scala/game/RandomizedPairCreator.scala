package game

import java.util.Random

class RandomizedPairCreator {
	
	private[this] val randomGenerator = new Random()
	
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
	
	def getRandomlyFromListExcludingElement[T <: Any](dontInclude: T, fromList: Array[T]):T = {
		val values = fromList.filter(element => element != dontInclude)
		return values(randomGenerator.nextInt(values.size))
	}

	//@TailRec
	def fillOutRandomlyWithNonMatchesNBack[T](listSize: Int, nBack: Int, fromElements: Array[T]):List[T] = listSize match {
		case 0 => Nil
		
		case x if (1 to nBack contains x) => {
			fromElements(randomGenerator.nextInt(fromElements.size)) :: fillOutRandomlyWithNonMatchesNBack(listSize - 1, nBack, fromElements)
		}
		
		case _ => {
			val shorterList = fillOutRandomlyWithNonMatchesNBack(listSize - 1, nBack, fromElements)
			val nonMatch = getRandomlyFromListExcludingElement(shorterList(nBack - 1), fromElements)
			nonMatch :: shorterList
		}
	}
	
	def insertNRandomMatches[T](originalList: List[T], nBack: Int, numberOfMatches: Int, fromCollection: List[T]): List[T] = {
		val randomIndexes = pickRandomElements((0 until originalList.size - nBack).toList, numberOfMatches)
		val sortedIndexes = randomIndexes sort((a, b) => a < b)
		
		introduceMatchesAtIndexes(originalList, sortedIndexes, fromCollection, 0, nBack)
	}
	
	// @TailRec
	def introduceMatchesAtIndexes[T](originalList: List[T], sortedIndexes: List[Int], fromCollection: List[T], currentIndex: Int, nBack: Int): List[T] = {
		println("originalList: " + originalList)
		println("sortedIndexes: " + sortedIndexes)
		
		if (sortedIndexes.size == 0) {
			originalList
		}
		else if (sortedIndexes.head == currentIndex) {
			// Shitballs, have to append to the start of this shit
			
			val smallerList = introduceMatchesAtIndexes((originalList.tail take nBack - 1) ::: List(originalList.head) ::: (originalList drop nBack + 1), sortedIndexes.tail, fromCollection, currentIndex + 1, nBack)
			val result = originalList.head :: smallerList
			println("combined to: " + result)
			result
		}
		else {
			originalList.head :: introduceMatchesAtIndexes(originalList.tail, sortedIndexes, fromCollection, currentIndex + 1, nBack) 
		}
	}
}