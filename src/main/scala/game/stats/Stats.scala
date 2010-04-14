package game.stats

case class Selection(soundSelected: Boolean, positionSelected: Boolean) {
	def withPositionMatch() = Selection(soundSelected, true)
	
	def withSoundMatch() = Selection(true, positionSelected)
	
	def reset() = Selection(false, false)
}

class Stats(soundSelections: List[Boolean], positionSelections: List[Boolean]) {
	
	def getUpdatedStats(userSelection: Selection, expectedSelection: Selection): Stats = {
		val isSoundMatch = userSelection.soundSelected == expectedSelection.soundSelected
		val isPositionMatch = userSelection.positionSelected == expectedSelection.positionSelected
		
		new Stats(isSoundMatch :: soundSelections, isPositionMatch :: positionSelections)
	}

	def successRate: Double = {
		(getMatchPercentage(soundSelections) + getMatchPercentage(positionSelections)) / 2.0 * 100.0
	}
	
	def getMatchPercentage(list : List[Boolean]) = {
		list.count(_ == true) / list.size.asInstanceOf[Double]
	}
	
	override def toString: String = {
		val matches = soundSelections.zip(positionSelections)
		"success %: + " + successRate + matches.toString
	}
}