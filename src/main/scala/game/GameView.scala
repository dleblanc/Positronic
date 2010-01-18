package game

trait GameView {
	def startGame(): Unit
	
	def highlightCell(x: Int, y:Int)
	
	def successfulPositionMatch()
}