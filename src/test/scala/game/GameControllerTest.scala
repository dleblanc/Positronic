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
    doAnswer(new RunTaskAnswer()).when(mockRunner).runDelayed(anyObject(), anyObject())

    //val controller = new GameController(mockView, 1, 1, new RunNowRunner(1))
    val controller = new GameController(mockView, 1, 1, mockRunner)
    controller.startGame()
    
    verify(mockRunner).runDelayed(any(), any())    
    verify(mockView).highlightCell(anyInt(), anyInt())
  }
}

