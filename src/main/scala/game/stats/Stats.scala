package game.stats

case class Selection(soundSelected: Boolean, positionSelected: Boolean)

class Stats(soundSelections: List[Boolean], positionSelections: List[Boolean]) {
	
	// Do this immutably? (return a new stats every time?)
	def getUpdatedStats(userSelection: Selection, expectedSelection: Selection): Stats = {
		val isSoundMatch = userSelection.soundSelected == expectedSelection.soundSelected
		val isPositionMatch = userSelection.positionSelected == expectedSelection.positionSelected
		
		new Stats(isSoundMatch :: soundSelections, isPositionMatch :: positionSelections)
	}

	def successRate: Double = {
		(getMatchPercentage(soundSelections) + getMatchPercentage(positionSelections)) / 2.0 * 100.0
	}
	
	def getMatchPercentage(list : List[Boolean]) = list.count(_ == true) / list.size.asInstanceOf[Double]
}