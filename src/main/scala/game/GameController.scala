package game
import game.util._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

// TODO: create a new controller for each game - parameterize input
class GameController(val view: GameView, width: Int, height: Int, val numberBack: Int, val delayedRunner: DelayedRunner) {
	val MAX_PLAYS = 20
	val N_BACK = 1
	val NUM_MATCHES = 5
	
	protected val randomGenerator = new Random()
	protected var moveHistory = new MoveHistory(Nil)
	
	//private[this] val sounds = Sound.elements.toList
	val availablePositions = for(row <- 0 until 3; col <- 0 until 3) yield (row, col)
	
	var playIndex = 0 // How to do this functionally?
	val creator = new RandomizedPairCreator()
	val sounds = creator.createRandomizedListWithNMatches(MAX_PLAYS, N_BACK, NUM_MATCHES, Sound.elements.toList.toArray) // TODO: find easier way to convert the sound elements
	val positions = creator.createRandomizedListWithNMatches(MAX_PLAYS, N_BACK, NUM_MATCHES, availablePositions.toArray) 
	
	
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
		if (playIndex >= MAX_PLAYS) {
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
		if (moveHistory.matchesLocationNMovesAgo(numberBack)) {
			view.successfulPositionMatch()
		}
		else {
			view.unsuccessfulPositionMatch()
		}
	}
	
	def soundMatchFromView() = {
		if (moveHistory.matchesSoundNMovesAgo(numberBack)) {
			view.successfulSoundMatch()
		}
		else {
			view.unsuccessfulSoundMatch()
		}
	}
}