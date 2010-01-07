package game
import game.util._
import scala.util._

class GameController(val view: GameView, val width: Int, val height: Int, val delayedRunner: DelayedRunner) {
	val randomGenerator = new Random()
	
  def startGame() = {
    delayedRunner.runDelayed(1000, highlightCell)
  }
  
  def highlightCell(): Unit = {
	
    view.highlightCell(randomGenerator.nextInt(width), randomGenerator.nextInt(height))
  }
}