package game
import game.util._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

class GameController(val view: GameView, width: Int, height: Int, val numberBack: Int, val delayedRunner: DelayedRunner) {
	val MAX_PLAYS = 20
	
	protected val randomGenerator = new Random()
	protected var moveHistory = new MoveHistory(Nil)
	
	//private[this] val sounds = Sound.elements.toList
	
	private[this] val creator = new RandomizedPairCreator()
	private[this] val sounds = creator.fillOutRandomlyWithNonMatchesNBack(MAX_PLAYS, 3, Sound.elements.toList.toArray) // TODO: find easier way to convert this
	private[this] var playIndex = 0 // How to do this functionally?
	
	
	def startGame() = {
		delayedRunner.runDelayedRepeating(1000, makeRandomPlay)
		moveHistory = new MoveHistory(Nil)
	}
	
	def pauseGame() = {
		delayedRunner.clearPendingEvents()
	}
  
	def makeRandomPlay(): Unit = {
		if (playIndex >= MAX_PLAYS) {
			pauseGame()
			return
		}
		
		val x = randomGenerator.nextInt(width)
		val y = randomGenerator.nextInt(height)
		
		val sound =  sounds(playIndex)
		
		moveHistory = moveHistory.addMove(new Move(new Location(x, y), sound))
		
		view.highlightCell(x, y)
		view.playSound(sound)
		
		playIndex = playIndex + 1
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