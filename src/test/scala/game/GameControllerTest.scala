
package game
import org.scalatest.matchers._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._
import org.mockito.stubbing._
import org.mockito.invocation._
import org.mockito.Matchers._

import game.util._

class RunNumTimesRunner(var numRuns: Int) extends DelayedRunner {
	override def runDelayedOnce(delay: Int, func: () => Unit):Unit = {
		if (numRuns <= 0) {
			return
		}
		
		numRuns = numRuns - 1
		func()
	}
	
	override def runDelayedRepeating(delay: Int, func: () => Unit) = runDelayedOnce(delay, func)
	
	override def clearPendingEvents() = {}
}

class GameControllerTest extends FunSuite with ShouldMatchers {
	
	test("delays an initial period, then highlights any square") {
	    val mockView = mock(classOf[GameView])

	    // (NOTE: I'd rather mock the runner to run things immediately, but it's a pain with mockito -just call it directly instead)

	    val controller = new GameController(mockView, 1, 2, 1, new RunNumTimesRunner(1))
	    controller.startGame()

	    verify(mockView).highlightCell(anyInt(), anyInt())
	}

	test("delays an initial period, then plays a sound") {
	    val mockView = mock(classOf[GameView])

	    val controller = new GameController(mockView, 1, 2, 1, new RunNumTimesRunner(1))
	    controller.startGame()

	    verify(mockView).playSound(anyObject())
	}

	test("selecting a position match notifies the user of success when it matches") {
		val mockView = mock(classOf[GameView])
		
	    val controller = new GameController(mockView, 1, 2, 1, mock(classOf[DelayedRunner])) {
			moveHistory = new MoveHistory(Nil)
				.addMove(new Move(new Location(1,1), Sound.Q))
				.addMove(new Move(new Location(1,1), Sound.Q))
		}
		
		controller.positionMatchFromView()
		verify(mockView).successfulPositionMatch()
	}

	test("selecting a position match notifies the user of failure when it doesnt match") {
		val mockView = mock(classOf[GameView])
		
	    val controller = new GameController(mockView, 1, 2, 1, mock(classOf[DelayedRunner])) {
			moveHistory = new MoveHistory(Nil)
		}
		
		controller.positionMatchFromView()
		verify(mockView).unsuccessfulPositionMatch()
	}
	
	test("pausing game clears events from runner") {
		// Too techie - what is the behaviour expressed here?
	    val mockView = mock(classOf[GameView])
	    val mockRunner = mock(classOf[DelayedRunner])

	    val controller = new GameController(mockView, 1, 2, 1, mockRunner)
	    controller.startGame()
		controller.pauseGame()

	    verify(mockRunner).clearPendingEvents()
	}

	test("updates view at end of game with statistics") {
		val mockView = mock(classOf[GameView])
		val numberOfPlays = 2
		val nBack = 1
		
	    val controller = new GameController(mockView, nBack, numberOfPlays, 1, new RunNumTimesRunner(4))
	    controller.startGame()

	    verify(mockView).showSuccessRate(anyDouble())
	}
}

