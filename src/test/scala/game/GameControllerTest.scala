package game
import org.scalatest.matchers._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._
import org.mockito.stubbing._
import org.mockito.invocation._
import org.mockito.Matchers._

import game.util._

class RunTaskAnswer extends Answer[Object] {
	override def answer(invocation: InvocationOnMock): Object = {
          val args = invocation.getArguments();
          args(1).asInstanceOf[() => Unit]()
          return null;
    }
}

class GameControllerTest extends FunSuite with ShouldMatchers {
	
	test("delays an initial period, then highlights any square") {
	    val mockView = mock(classOf[GameView])
	    val mockRunner = mock(classOf[DelayedRunner])

	    // Run the scheduled task immediately when scheduled
	    doAnswer(new RunTaskAnswer()).when(mockRunner).runDelayedRepeating(anyObject(), anyObject())

	    val controller = new GameController(mockView, 1, 1, mockRunner)
	    controller.startGame()

	    verify(mockView).highlightCell(anyInt(), anyInt())
	}

	test("delays an initial period, then plays a sound") {
	    val mockView = mock(classOf[GameView])
	    val mockRunner = mock(classOf[DelayedRunner])

	    // Run the scheduled task immediately when scheduled
	    doAnswer(new RunTaskAnswer()).when(mockRunner).runDelayedRepeating(anyObject(), anyObject())

	    val controller = new GameController(mockView, 1, 1, mockRunner)
	    controller.startGame()

	    verify(mockView).playSound(anyObject())
	}

	test("selecting a position match notifies the user of success when it matches") {
		val mockView = mock(classOf[GameView])
		
	    val controller = new GameController(mockView, 1, 1, mock(classOf[DelayedRunner])) {
			moveHistory = new MoveHistory(Nil)
				.addMove(new Move(new Location(1,1), Sound.Q))
				.addMove(new Move(new Location(1,1), Sound.Q))
		}
		
		controller.positionMatchFromView()
		verify(mockView).successfulPositionMatch()
	}

	test("selecting a position match notifies the user of failure when it doesnt match") {
		val mockView = mock(classOf[GameView])
		
	    val controller = new GameController(mockView, 1, 1, mock(classOf[DelayedRunner])) {
			moveHistory = new MoveHistory(Nil)
		}
		
		controller.positionMatchFromView()
		verify(mockView).unsuccessfulPositionMatch()
	}
	
	test("pausing game clears events from runner") {
		// Too techie - what is the behaviour expressed here?
	    val mockView = mock(classOf[GameView])
	    val mockRunner = mock(classOf[DelayedRunner])

	    val controller = new GameController(mockView, 1, 1, mockRunner)
	    controller.startGame()
		controller.pauseGame()

	    verify(mockRunner).clearPendingEvents()
	}
}

