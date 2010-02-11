package game.stats

import org.scalatest.matchers._
import org.scalatest.prop.Checkers
import org.scalatest._

import org.scalacheck._
import org.scalacheck.Prop._

/*
Whether the user selects a match or not, this is always dumped into a stats object

audioStats = new Stats(numMoves = 20) [or just dump in the actual match/non-match list]

audioStats.setSelected(move = 3)

audioStats.getSelections() =>
  List(false, false, true, false, true, true), etc...
  
  
controller - creates game
 - pumps results into Stats
 - compares game results to stats (via stats)
*/
// NOTES: Scalacheck should be able to provide better visibility (besides collect), on a per-check basis.

// TODO: can I create some kind of object that has a 'give me all pairs' method? I'm going a little procedural here

class StatsTest extends FunSuite with ShouldMatchers {
	class Stats(size: Int) {
		val selections = List.make(size, false)
		
		// Do this immutably? (return a new stats every time?)
		def setSelected(index: Int): Stats = {
			//selections
			this
		}
	}
	
	def getMatchPercentage(matches: List[Boolean], userSelections: List[Boolean]): Double = {
		val pairs = matches zip userSelections
		val correctSelections = pairs.filter(pair => pair._1 == pair._2)
		(correctSelections.size.asInstanceOf[Double] / matches.size ) * 100.0
	}
	
	test("one successful guess of a sequence of 1 yields 100%") {
		val matches = List(true)
		val userSelections = List(true)
		
		getMatchPercentage(matches, userSelections) should equal (100.0)
	}
	
	test("one unsuccessful guess of a sequence of 1 yields 0%") {
		val matches = List(false)
		val userSelections = List(true)
		
		getMatchPercentage(matches, userSelections) should equal (0.0)
	}
	
	test("one successful and one unsuccessful guess in a sequence of 2 yields 50%") {
		val matches = List(false, true)
		val userSelections = List(true, true)
		
		getMatchPercentage(matches, userSelections) should equal (50.0)
	}	
}