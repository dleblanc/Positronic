package game
import game.util._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

class GameController(val view: GameView, val width: Int, val height: Int, val delayedRunner: DelayedRunner) {
	protected val randomGenerator = new Random()
	protected var moveHistory = new MoveHistory(Nil)
	
	private[this] val numberBack = 1
	private[this] val sounds = Sound.elements.toList
	
	def startGame() = {
		delayedRunner.runDelayedRepeating(1000, makeRandomPlay)
		moveHistory = new MoveHistory(Nil)
	}
	
	def pauseGame() = {
		delayedRunner.clearPendingEvents()
	}
  
	def makeRandomPlay(): Unit = {
		val x = randomGenerator.nextInt(width)
		val y = randomGenerator.nextInt(height)
		
		val sound =  sounds(randomGenerator.nextInt(sounds.size))
		
		moveHistory = moveHistory.addMove(new Move(new Location(x, y), sound))
		
		view.highlightCell(x, y)
		view.playSound(sound)
	}
	
	// TODO: factor out duplication in these two methods
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