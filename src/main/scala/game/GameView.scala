package game

trait GameView {
	def startGame(): Unit
	
	def highlightCell(x: Int, y:Int)
	
	def playSound(sound: Sound.Value)
	
	def successfulPositionMatch()
	def unsuccessfulPositionMatch()	

	def successfulSoundMatch()
	def unsuccessfulSoundMatch()
	
	def showSuccessRate(successful: Double)
}