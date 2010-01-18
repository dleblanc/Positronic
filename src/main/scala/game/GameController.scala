package game
import game.util._

// NOTE: scala.util.Random was not seeming very random - using Java's instead
import java.util.Random

class GameController(val view: GameView, val width: Int, val height: Int, val delayedRunner: DelayedRunner) {
	protected val randomGenerator = new Random()
	protected var moveHistory = new MoveHistory(Nil)
	private[this] val numberBack = 1
	
	def startGame() = {
		delayedRunner.runDelayedRepeating(1000, highlightCell)
		moveHistory = new MoveHistory(Nil)
	}
  
	def highlightCell(): Unit = {
		val x = randomGenerator.nextInt(width)
		val y = randomGenerator.nextInt(height)
		
		moveHistory = moveHistory.addMove(new Move(new Location(x, y), Sound.Q))
		view.highlightCell(x, y)
	}
	
	def positionMatchFromView() = {
		if (moveHistory.matchesLocationNMovesAgo(numberBack)) {
			view.successfulPositionMatch()
		}
	}
}