package game

import java.util.Random

class RandomizedPairCreator {
	
	private[this] val randomGenerator = new Random()
	
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
}