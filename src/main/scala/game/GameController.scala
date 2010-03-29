package game
import game.util._
import game.stats._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

// TODO: create a new controller for each game - parameterize input (?), not doing this now. 
class GameController(
		view: GameView, 
		numPlays: Int, 
		numMatches: Int, 
		delayedRunner: DelayedRunner) {
	
	val INITIAL_DELAY = 500
	
	protected var moveHistory = new MoveHistory(Nil)
	var currentSelection = new Selection(false, false)
	var nBack = 0
	
	def startGame() = {
		// rename to restartGame?
		nBack = view.getNBack()
		
		val availablePositions = for(row <- 0 until 3; col <- 0 until 3)
			yield (row, col)
		
		val creator = new RandomizedPairCreator()
		
		val sounds = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, Sound.elements.toList.toArray) // TODO: find easier way to convert the sound elements
		val positions = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, availablePositions.toArray)

		delayedRunner.runDelayedOnce(INITIAL_DELAY, () => makeRandomPlay(sounds, positions, new Stats(Nil, Nil)))
		moveHistory = new MoveHistory(Nil)
	}
	
	def makeRandomPlay(sounds: List[Sound.Value], positions: List[(Int, Int)], stats: Stats): Unit = {
		if (positions.isEmpty || sounds.isEmpty) {
			finishGame(stats)
			return
		}
		
		val position = positions.head
		val sound = sounds.head
		
		moveHistory = moveHistory.addMove(new Move(new Location(position._1, position._2), sound))
		
		view.highlightCell(position._1, position._2)
		view.playSound(sound)
		
		delayedRunner.runDelayedOnce(1000, () => {
			val expectedSelection = Selection(moveHistory.matchesSoundNMovesAgo(nBack), moveHistory.matchesLocationNMovesAgo(nBack))
			val updatedStats = stats.getUpdatedStats(currentSelection, expectedSelection)

			makeRandomPlay(sounds.tail, positions.tail, updatedStats)
		})
	}
	
	
	def finishGame(stats: Stats) = {
		pauseGame()
		view.showSuccessRate(stats.successRate)
	}
	
	def pauseGame() = {
		delayedRunner.clearPendingEvents()
	}
		
	// TODO: factor out duplication in these two approaches - sound/view
	def positionMatchFromView() = {
		if (moveHistory.matchesLocationNMovesAgo(nBack)) {
			view.successfulPositionMatch()
		}
		else {
			view.unsuccessfulPositionMatch()
		}
	}
	
	def soundMatchFromView() = {
		if (moveHistory.matchesSoundNMovesAgo(nBack)) {
			view.successfulSoundMatch()
		}
		else {
			view.unsuccessfulSoundMatch()
		}
	}
}