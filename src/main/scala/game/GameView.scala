package game

import android.view.KeyEvent

trait GameView {
	def highlightCell(x: Int, y:Int)
	
	def playSound(sound: Sound.Value)
	
	def successfulPositionMatch()
	def unsuccessfulPositionMatch()	

	def successfulSoundMatch()
	def unsuccessfulSoundMatch()
	
	def showSuccessRate(successful: Double)
	
	def getNBack(): Int
}