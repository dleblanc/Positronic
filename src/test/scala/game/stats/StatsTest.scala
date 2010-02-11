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
	
	test("one both-successful guess of a sequence of 1 yields 100%") {
		val userSelection = Selection(true, true)
		val generatedSelection = Selection(true, true)
		
		val stats = new Stats(Nil, Nil).getUpdatedStats(userSelection, generatedSelection)
			
		stats.successRate should equal (100.0)
	}
	
	test("one both-unsuccessful guess of a sequence of 1 yields 0%") {
		val userSelection = Selection(true, true)
		val generatedSelection = Selection(false, false)
		
		val stats = new Stats(Nil, Nil).getUpdatedStats(userSelection, generatedSelection)
			
		stats.successRate should equal (0.0)
	}
	
	test("one both-successful and one unsuccessful guess in a sequence of 2 yields 50%") {
		val userSelection = List(Selection(true, true), Selection(false, false))
		val generatedSelection = List(Selection(true, true), Selection(true, true))
		
		val stats = new Stats(Nil, Nil).
			getUpdatedStats(userSelection(0), generatedSelection(0)).
			getUpdatedStats(userSelection(1), generatedSelection(1))
			
		stats.successRate should equal (50.0)
	}

	test("one sound match and no position matches of two selections yields 25%") {
		val userSelection = List(Selection(true, true), Selection(true, true))
		val generatedSelection = List(Selection(false, false), Selection(true, false))
		
		val stats = new Stats(Nil, Nil).
			getUpdatedStats(userSelection(0), generatedSelection(0)).
			getUpdatedStats(userSelection(1), generatedSelection(1))
			
		stats.successRate should equal (25.0)
	}
}