package game.stats

case class Selection(soundSelected: Boolean, positionSelected: Boolean) {
	def withPositionMatch() = Selection(soundSelected, true)
	
	def withSoundMatch() = Selection(true, positionSelected)
	
	def reset() = Selection(false, false)
	
	def eitherSelected = soundSelected || positionSelected
}

class Stats(userSelections: List[Selection], expectedSelections: List[Selection]) {
	
	def getUpdatedStats(userSelection: Selection, expectedSelection: Selection): Stats = {
		new Stats(userSelection :: userSelections, expectedSelection :: expectedSelections)
	}

	def successRate: Double = {
		val userSoundSelections = userSelections.map(_.soundSelected)
		val expectedSoundSelections = expectedSelections.map(_.soundSelected)

		val userPositionSelections = userSelections.map(_.positionSelected)
		val expectedPositionSelections = expectedSelections.map(_.positionSelected)

		(getMatchPercentage(userSoundSelections, expectedSoundSelections) + getMatchPercentage(userPositionSelections, expectedPositionSelections)) / 2.0
	}
	
	def getMatchPercentage(userList : List[Boolean], expectedList: List[Boolean]): Double = {

		val userAndExpectedPairs = userList.zip(expectedList)
		
		val guessesOrExpectedMatchCount = userAndExpectedPairs.count(pair => pair._1 || pair._2)
		
		val correctUserGuesses = userAndExpectedPairs.count(pair => pair._1 && pair._2) // There was a user guess, and it was correct
		
		
		guessesOrExpectedMatchCount match {
			case 0 => 0.0
			case _ => (correctUserGuesses / guessesOrExpectedMatchCount.asInstanceOf[Double]) * 100.0
		}
	}
	
	override def toString: String = {
		"success %: + " + successRate + userSelections.toString
	}
}