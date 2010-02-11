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
		delayedRunner: DelayedRunner, 
		creator: RandomizedPairCreator) {
	
	protected var moveHistory = new MoveHistory(Nil)
	
	//private[this] val sounds = Sound.elements.toList
	val availablePositions = for(row <- 0 until 3; col <- 0 until 3) yield (row, col)
	
	var playIndex = 0 // How to do this functionally?
	val sounds = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, Sound.elements.toList.toArray) // TODO: find easier way to convert the sound elements
	val positions = creator.createRandomizedListWithNMatches(numPlays, nBack, numMatches, availablePositions.toArray) 
	
	def startGame() = {
		delayedRunner.runDelayedRepeating(1000, makeRandomPlay)
		moveHistory = new MoveHistory(Nil)
	}
	
	def pauseGame() = {
		delayedRunner.clearPendingEvents()
	}
  
	def makeRandomPlay(): Unit = {
		// I could recurse on this method, handing in the sublist of moves left, returning a 'statistics' object (or None, if they quit).
		// OR: I could schedule another function, handing it a parameter (+1) every time, no? (partially applied function?)
		if (playIndex >= numPlays) {
			pauseGame()
			return
		}
		
		val position = positions(playIndex)
		val sound =  sounds(playIndex)
		
		moveHistory = moveHistory.addMove(new Move(new Location(position._1, position._2), sound))
		
		view.highlightCell(position._1, position._2)
		view.playSound(sound)
		
		playIndex = playIndex + 1
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