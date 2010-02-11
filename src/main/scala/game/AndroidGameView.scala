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
import android.graphics.drawable.shapes._
import android.graphics.RectF
import android.util._
import android.view.View._
import android.media._


import com.example.android._
import game._
import game.util._

class AndroidGameView(context: Context, mainView: View) extends GameView {
	val viewDelayedRunner = new AndroidDelayedRunner()
	val soundPlayer = new SoundPlayer()
	
	// TODO: use the DI-like stuff for injecting a default delayed runner here	
	val controller = new GameController(this, 1, 20, 5, new AndroidDelayedRunner())
	
	// List[row - 0 to 3][col - 0 to 3] of Buttons
	val squaresByRow = (for (row <- 0 until 3) yield
							(for (col <- 0 until 3) yield {
								val button = new Button(context)
								button.setText("     ")
								button
							}).toList
						).toList
	
	val positionSuccessTextField = mainView.findViewById(R.id.positionSucessTextField).asInstanceOf[TextView]
	val soundSuccessTextField = mainView.findViewById(R.id.soundSucessTextField).asInstanceOf[TextView]
	
	
	addButtonsToRows()

    controller.startGame()

	mainView.findViewById(R.id.positionMatchButton).setOnClickListener(new OnClickListener() {
		override def onClick(view: View): Unit = {
			controller.positionMatchFromView()
		}
	})
	
	mainView.findViewById(R.id.soundMatchButton).setOnClickListener(new OnClickListener() {
		override def onClick(view: View): Unit = {
			controller.soundMatchFromView()
		}
	})
		
	def activityPaused() = {
		controller.pauseGame()
		soundPlayer.stop()
	}
	
	override def startGame(): Unit = {}

	override def highlightCell(x: Int, y:Int) = {
		val square = squaresByRow(x)(y)

		// Not working - just makes the patch black (go full 2d instead?)
		// val selectedDrawable = new ShapeDrawable(new RoundRectShape(Array(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f), new RectF(), null))
		// selectedDrawable.getPaint().setColor(0xFF000000)
		// square.setBackgroundDrawable(selectedDrawable)
				
		val animation = new AlphaAnimation(0.0f, 1.0f)
		animation.setDuration(500)
		square.startAnimation(animation)
		
	}
	
	class SoundPlayer { // TODO: pump out to separate file
		val mediaPlayer = new MediaPlayer()
		val soundsToResources = Map(
			Sound.C -> R.raw.c,
			Sound.H -> R.raw.h,
			Sound.K -> R.raw.k,
			Sound.L -> R.raw.l,
			Sound.Q -> R.raw.q,
			Sound.R -> R.raw.r,
			Sound.S -> R.raw.s,
			Sound.T -> R.raw.t)
		
		def playSound(sound: Sound.Value) = {
			
			mediaPlayer.reset()
			val soundFile = context.getResources().openRawResourceFd(soundsToResources(sound));
			
			mediaPlayer.setDataSource(soundFile.getFileDescriptor(), soundFile.getStartOffset(), soundFile.getLength());
			mediaPlayer.prepare()
			mediaPlayer.start();
		}
		
		def stop() = mediaPlayer.stop()
	}
	
	override def playSound(sound: Sound.Value) = soundPlayer.playSound(sound)
	
	override def successfulPositionMatch() = showMomentaryText(positionSuccessTextField, "position match")
	override def unsuccessfulPositionMatch() = showMomentaryText(positionSuccessTextField, "no position match")

	override def successfulSoundMatch() = showMomentaryText(soundSuccessTextField, "sound match")
	override def unsuccessfulSoundMatch() = showMomentaryText(soundSuccessTextField, "no sound match")
	
	private[this] def showMomentaryText(textField: TextView, value: String) = {
		Log.d("Positronic", value)
		textField.setText(value)
		viewDelayedRunner.runDelayedOnce(800, () => textField.setText(""))
	}
	
	private[this] def addButtonsToRows() = {
		// Get the row ViewGroup instance for each row (from the resource)
		val buttonRows = List(R.id.buttonRow0, R.id.buttonRow1, R.id.buttonRow2)
			.map(buttonRowId => mainView.findViewById(buttonRowId).asInstanceOf[ViewGroup])

		for (row <- 0 until 3; col <- 0 until 3) {
			buttonRows(row).addView(squaresByRow(row)(col))	
		}		
	}
	
	// private[this] def getLockForScreenDimming(): = {
	// 	val wakeLock = getLockForScreenDimming() // it says it's finalizing it while it's held.
	// 	val powerManager = context.getSystemService(Context.POWER_SERVICE).asInstanceOf[PowerManager]
	// 	val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Positronic Wake Lock")
	// 	return wakeLock.acquire()
	// 	// TODO: Release this lock when the display is paused
	// }
	
	
	def showSuccessRate(successful: Double) = {
		// FIXME: use a dedicated text field for this one
		showMomentaryText(positionSuccessTextField, "You scored " + successful + "%")
		()
	}
}
