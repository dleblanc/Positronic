package game

// Should probably put these accessory classes somewhere more general
object Sound extends Enumeration {
  val C, H, K, L, Q, R, S, T = Value
}

case class Location(x: Int, y: Int)

case class Move(location: Location, sound: Sound.Value)

class MoveHistory(val moves: List[Move]) {
	
    def this(move: Move) = this(List(move)) // Alt constructor with just one move
 
    // define as a :: operator?
	def addMove(move: Move): MoveHistory = {
	  return new MoveHistory(move :: moves);
	}
 
  	def matchesLocationNMovesAgo(numMovesAgo: Int) = matchesNMovesAgo(numMovesAgo, (current, old) => current.location == old.location)
   
    def matchesSoundNMovesAgo(numMovesAgo: Int) = matchesNMovesAgo(numMovesAgo, (current, old) => {
		current.sound == old.sound
	})
    
    private def matchesNMovesAgo(numMovesAgo: Int, matchFunc: (Move, Move) => Boolean): Boolean = {
  	  if (numMovesAgo >= moves.size) {
  	    return false
  	  }

      matchFunc(moves.head, moves(numMovesAgo))
    }
}
