package game
import game.util._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

// TODO: create a new controller for each game - parameterize input
class GameController(
		view: GameView, 
		nBack: Int, 
		numPlays: Int, 
		numMatches: Int, 
		delayedRunner: DelayedRunner) {
	
	protected var moveHistory = new MoveHistory(Nil)
	val creator = new RandomizedPairCreator()
	
	def startGame() = {
		val availablePositions = for(row <- 0 until 3; col <- 0 until 3)
			yield (row, col)
		
		val sounds = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, Sound.elements.toList.toArray) // TODO: find easier way to convert the sound elements
		val positions = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, availablePositions.toArray)

		delayedRunner.runDelayedOnce(1000, () => makeRandomPlay(0, sounds, positions))
		moveHistory = new MoveHistory(Nil)
	}
	
	def pauseGame() = {
		delayedRunner.clearPendingEvents()
	}
  
	def makeRandomPlay(playIndex: Int, sounds: List[Sound.Value], positions: List[(Int, Int)]): Unit = {
		if (playIndex >= numPlays) {
			pauseGame()
			return
		}
		
		val position = positions.head
		val sound =  sounds.head
		
		moveHistory = moveHistory.addMove(new Move(new Location(position._1, position._2), sound))
		
		view.highlightCell(position._1, position._2)
		view.playSound(sound)
		
		delayedRunner.runDelayedOnce(1000, () => makeRandomPlay(playIndex + 1, sounds.tail, positions.tail))
	}
	
	// TODO: factor out duplication in these two approaches - sound/view
	// TODO: how to react to view here - we're updating things on the fly.  Move history is ok I guess.
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