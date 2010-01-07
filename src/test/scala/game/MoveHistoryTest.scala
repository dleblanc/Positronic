package game
import org.scalatest.matchers._
import org.scalatest._

// TODO: rename to move history test
class MoveHistoryTest extends FunSuite with ShouldMatchers {

  // NOTE: wouldn't it be awesome to be able to express these as story tests? -- cucumber + jruby

    test("matches right location one move ago with different sound") {
	  val history = 
		new MoveHistory(Nil)
	  	.addMove(new Move(Location(0,0), Sound.Q))
	  	.addMove(new Move(Location(0,0), Sound.L))
   
	  history.matchesLocationNMovesAgo(1) should equal (true)
	}

	test("checking 3 ago with only one move in the history returns false") {
	  val history = 
		new MoveHistory(new Move(Location(0,0), Sound.Q))
   
	  history.matchesLocationNMovesAgo(3) should equal (false)
	}

	test("matches right sound one move ago") {
	  val history = 
		new MoveHistory(Nil)
	  	.addMove(new Move(Location(0,0), Sound.Q))
	  	.addMove(new Move(Location(0,0), Sound.Q))
	  history.matchesSoundNMovesAgo(1) should equal (true)
	}
}