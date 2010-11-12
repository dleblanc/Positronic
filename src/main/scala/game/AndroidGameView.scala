package game

import scala.util._
import android.content._
import android.os._
import android.os.PowerManager._
import android.app.Activity
import android.os.Bundle
import android.widget._
import android.view._
import android.view.animation._
import android.graphics.drawable._
import android.graphics.Color
import android.graphics.drawable.shapes._
import android.graphics.RectF
import android.util._
import android.view.View._
import android.view.KeyEvent
import android.media._


import com.example.android._
import game._
import game.util._
import game.sound._

class AndroidGameView(context: Context, mainView: View) extends GameView {
	val POSITION_MATCH_KEY = KeyEvent.KEYCODE_A
	val SOUND_MATCH_KEY = KeyEvent.KEYCODE_L
	
	val viewDelayedRunner = new AndroidDelayedRunner()
	val soundPlayer = new SoundPlayer(context)
	
	// TODO: use the DI-like stuff for injecting a default delayed runner here	
	val controller = new GameController(this, 20, 5, new AndroidDelayedRunner())
	
	// List[row - 0 to 3][col - 0 to 3] of Buttons
	val squaresByRow = (for (row <- 0 until 3) yield
							(for (col <- 0 until 3) yield {
								val button = new Button(context)
								button.setText("     ")
								button
							}).toList
						).toList
	
	//val positionSuccessTextField = mainView.findViewById(R.id.positionSucessTextField).asInstanceOf[TextView]
	val nBackSpinner = mainView.findViewById(R.id.nBackSpinner).asInstanceOf[Spinner]
	
	addButtonsToRows()

	invokeWhenButtonClicked(R.id.startGameButton, () => controller.startGame)

	invokeWhenButtonClicked(R.id.positionMatchButton, () => controller.positionMatchFromView())
	invokeWhenButtonClicked(R.id.soundMatchButton, () => controller.soundMatchFromView())
	
	invokeWhenButtonClicked(R.id.bothMatchButton, () => {
		controller.positionMatchFromView()
		controller.soundMatchFromView()
	})
	
	mainView.findViewById(R.id.positionMatchStatus).setBackgroundColor(Color.GRAY)
	mainView.findViewById(R.id.soundMatchStatus).setBackgroundColor(Color.GRAY)
	
	override def highlightCell(x: Int, y:Int) = {
		val square = squaresByRow(x)(y)
				
		val animation = new AlphaAnimation(0.0f, 1.0f)
		animation.setDuration(500)
		square.startAnimation(animation)
	}
	
	override def playSound(sound: Sound.Value) = soundPlayer.playSound(sound)
	
	override def successfulPositionMatch() = showStatusWithColour(R.id.positionMatchStatus, Color.GREEN)
	override def unsuccessfulPositionMatch() = showStatusWithColour(R.id.positionMatchStatus, Color.RED)

	override def successfulSoundMatch() = showStatusWithColour(R.id.soundMatchStatus, Color.GREEN)
	override def unsuccessfulSoundMatch() = showStatusWithColour(R.id.soundMatchStatus, Color.RED)
	
	override def showSuccessRate(successful: Double) = {
		// FIXME: use a dedicated text field for this one
		//showMomentaryText(positionSuccessTextField, "You scored " + successful + "%", 10000)
		()
	}
	
	override def getNBack(): Int = {
		val stringNBack = nBackSpinner.getSelectedItem().asInstanceOf[String]
		return Integer.parseInt(stringNBack)
	}
	
	// Called directly from the activity
	def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = {
		keyCode match {
			case POSITION_MATCH_KEY => controller.positionMatchFromView()
			case SOUND_MATCH_KEY => controller.soundMatchFromView()
			case _ => {} // Ignore default case
		}
		return false
	}
	
	// Called directly from the activity
	def activityPaused() = {
		controller.pauseGame()
		soundPlayer.stop()
	}

	private[this] def showMomentaryText(textField: TextView, value: String):Unit = showMomentaryText(textField, value, 800)

	private[this] def showMomentaryText(textField: TextView, value: String, delay: Int):Unit = {
		Log.d("Positronic", value)
		textField.setText(value)
		viewDelayedRunner.runDelayedOnce(delay, () => textField.setText(""))
	}
	
	private[this] def addButtonsToRows() = {
		// Get the row ViewGroup instance for each row (from the resource)
		val buttonRows = List(R.id.buttonRow0, R.id.buttonRow1, R.id.buttonRow2)
			.map(buttonRowId => mainView.findViewById(buttonRowId).asInstanceOf[ViewGroup])

		for (row <- 0 until 3; col <- 0 until 3) {
			buttonRows(row).addView(squaresByRow(row)(col))	
		}		
		
	}
	
    private[this] def invokeWhenButtonClicked(buttonId: Int, action: () => Unit):Unit = {
		mainView.findViewById(buttonId).setOnClickListener(new OnClickListener() {
			override def onClick(view: View): Unit = {
				action()
			}
		})
	}
    
	private[this] def showStatusWithColour(statusViewId: Int, colour: Int) = {
		val positionButton = mainView.findViewById(statusViewId)
		positionButton.setBackgroundColor(colour);

		val animation = new AlphaAnimation(0.8f, 0.1f)
		animation.setDuration(800)
		positionButton.startAnimation(animation)
	}
}
