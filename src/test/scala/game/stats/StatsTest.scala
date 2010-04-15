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

// NOTE: we should only mark the user when they make a guess (audible/visual or both).  So if they never guess, they get 0%.
// They should get partial value for a 1/2 correct guess.
// total should be: # right guesses / # total in sequence

class StatsTest extends FunSuite with ShouldMatchers {
	val emptyStat = new Stats(Nil, Nil)

	test("one both-successful guess of a sequence of 1 yields 100%") {
		val userSelection = Selection(true, true)
		val computerSelection = Selection(true, true)

		winPercentage(userSelection, computerSelection) should equal (100.0)
	}
	
	test("one both-unsuccessful guess of a sequence of 1 yields 0%") {
		val userSelection = Selection(true, true)
		val computerSelection = Selection(false, false)

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}
	
	test("one non-guess against both an audible and visual match yields 0%") {
		val userSelection = Selection(false, false)
		val computerSelection = Selection(true, true)

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}	

	test("one position guess against and audible match yields 0%") {
		val userSelection = Selection(true, false)
		val computerSelection = Selection(false, true)

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}	

	test("one audible guess against a position match yields 0%") {
		val userSelection = Selection(false, true)
		val computerSelection = Selection(true, false)

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}	

	test("all non-guesses against all non-matches yields 0%") {
		val userSelection = Selection(false, false)
		val computerSelection = Selection(false, false)

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}	
		
	test("a guess of both against just a position match should yield 50%") {
		val userSelection = Selection(true, true)
		val computerSelection = Selection(false, true)

		winPercentage(userSelection, computerSelection) should equal (50.0)
	}	

	test("one both-successful and one unsuccessful guess in a sequence of 2 yields 50%") {
		val userSelection = List(Selection(true, true), Selection(false, false))
		val computerSelection = List(Selection(true, true), Selection(true, true))
		
		winPercentage(userSelection, computerSelection) should equal (50.0)
	}

	test("one sound match and no position matches of two selections yields 25%") {
		
		val userSelection = List(Selection(true, true), Selection(true, true))
		val computerSelection = List(Selection(false, false), Selection(true, false))

		winPercentage(userSelection, computerSelection) should equal (25.0)
	}

	test("one both-match followed by one non-guessed match") {
		
		val userSelection = List(Selection(true, true), Selection(false, false))
		val computerSelection = List(Selection(true, true), Selection(true, true))

		winPercentage(userSelection, computerSelection) should equal (50.0)
	}

	test("one both-match followed by one non-guessed, non-match yields 100% success rate") {
		
		val userSelection = List(Selection(true, true), Selection(false, false))
		val computerSelection = List(Selection(true, true), Selection(false, false))

		winPercentage(userSelection, computerSelection) should equal (100.0)
	}

	test("two incorrect guesses in a row of matches yields 0%") {
		
		val userSelection = List(Selection(true, false), Selection(true, false))
		val computerSelection = List(Selection(false, true), Selection(false, true))

		winPercentage(userSelection, computerSelection) should equal (0.0)
	}

	test("only one correct guess followed by non-guessed matches results in 25%") {
		
		val userSelection = List(Selection(true, true), Selection(false, false), Selection(false, false), Selection(false, false))
		val computerSelection = List(Selection(true, true), Selection(true, true), Selection(true, true), Selection(true, true))

		winPercentage(userSelection, computerSelection) should equal (25.0)
	}
	
	test("getMatchPercentage returns 1.0 for complete matches") {
		emptyStat.getMatchPercentage(List(false, false, true, false), List(false, false, true, false)) should equal (1.0)
	}

	test("getMatchPercentage returns 1/3 of union of guesses and expected matches for complete matches") {
		// only matched 1 of 3 guesses (the 2nd index)
		emptyStat.getMatchPercentage(List(true, true, true, false), List(false, false, true, false)) should equal (1/3.0)
	}

	test("getMatchPercentage returns 0% with no guesses") {
		emptyStat.getMatchPercentage(List(false, false), List(true, true)) should equal (0.0)
	}
	
	test("getMatchPercentage returns 0% with no matches or guesses") {
		emptyStat.getMatchPercentage(List(false, false), List(false, false)) should equal (0.0)
	}
	
	def winPercentage(userSelections: List[Selection], computerSelections: List[Selection]): Double = {
		val initialStats = new Stats(Nil, Nil)
		
		val resultantStats = userSelections.zip(computerSelections).foldLeft(initialStats) {
			(stats, selections) => {
				val (userSelection, computerSelection) = selections
				stats.getUpdatedStats(userSelection, computerSelection)
			}
		}
		resultantStats.successRate
	}

	def winPercentage(userSelection: Selection, computerSelection: Selection): Double = winPercentage(List(userSelection), List(computerSelection))
	
}