package com.example.android

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget._
import _root_.android.view._
import _root_.android.view.animation._
import _root_.android.graphics.drawable._
import _root_.android.graphics.drawable.shapes._
import _root_.android.graphics.RectF
import _root_.android.util._
import _root_.android.view.View._
import _root_.android.media._

import scala.util._
import game._
import game.util._


// TODO: instantiate and use a view separately (passing this as a context), because init must happen in onCreate (and we like vals)
class PositronicActivity extends Activity with GameView {
	
	val delayedRunner = new AndroidDelayedRunner()
	val randomGenerator = new Random()
	
	
	// TODO: try this instead? val res = 1 to 3 map (i => 1 to 3 map (j => i * j)).toList
	// var squaresByRow =		
	// 	for (row <- 0 until 3)
	// 	  yield for (col <- 0 until 3)
	// 	    yield new Button(this)
	var squaresByRow = new scala.collection.mutable.ListBuffer[scala.collection.mutable.ListBuffer[Button]]
	
	var positionSuccessTextField: TextView = null // Arrgh, how to make this a val
	var soundSuccessTextField: TextView = null
	
	var controller: GameController = null
		
    override def onCreate(savedInstanceState: Bundle) {
	    super.onCreate(savedInstanceState)
	
		val mainView = getLayoutInflater().inflate(R.layout.positronic, null)
		positionSuccessTextField = mainView.findViewById(R.id.positionSucessTextField).asInstanceOf[TextView]
		soundSuccessTextField = mainView.findViewById(R.id.soundSucessTextField).asInstanceOf[TextView]

		val layout = new TableLayout(this)
		layout.setStretchAllColumns(true)
	
		
		setContentView(mainView)
	
		val buttonRows = List(R.id.buttonRow0, R.id.buttonRow1, R.id.buttonRow2)
			.map(buttonRowId => mainView.findViewById(buttonRowId).asInstanceOf[ViewGroup])
			
			// TODO: do this with immutable semantics (val, not var) - yield was screwing us up
		for (row <- 0 until 3) {
			val squaresOnRow = new scala.collection.mutable.ListBuffer[Button]()
			for (col <- 0 until 3) {
				val button = new Button(this)
				button.setText("     ") // GAA - need a nicely sized, square button
				squaresOnRow + button
			}
			squaresByRow + squaresOnRow
		}
				
		for (row <- 0 until 3; col <- 0 until 3) {
			buttonRows(row).addView(squaresByRow(row)(col))	
		}
	
	
	    // TODO: use the DI-like stuff for injecting a default delayed runner here	
	    controller = new GameController(this, 3, 3, new AndroidDelayedRunner())
	  
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
    }

	override def onPause() = {
		controller.pauseGame()
		super.onPause()
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
	
	def playSound(sound: Sound.Value) = {
		val soundsToResources = Map(
			Sound.C -> R.raw.c,
			Sound.H -> R.raw.h,
			Sound.K -> R.raw.k,
			Sound.L -> R.raw.l,
			Sound.Q -> R.raw.q,
			Sound.R -> R.raw.r,
			Sound.S -> R.raw.s,
			Sound.T -> R.raw.t)

		val mediaPlayer = MediaPlayer.create(this, soundsToResources(sound)) // TODO: don't do this every time
		mediaPlayer.start();
		delayedRunner.runDelayedOnce(500, () => {
			mediaPlayer.stop()
			mediaPlayer.release()
		})
	}
	
	override def successfulPositionMatch() = showMomentaryText(positionSuccessTextField, "position match")
	override def unsuccessfulPositionMatch() = showMomentaryText(positionSuccessTextField, "no position match")

	override def successfulSoundMatch() = showMomentaryText(soundSuccessTextField, "sound match")
	override def unsuccessfulSoundMatch() = showMomentaryText(soundSuccessTextField, "no sound match")
	
	def showMomentaryText(textField: TextView, value: String) = {
		Log.d("Positronic", value)
		textField.setText(value)
		delayedRunner.runDelayedOnce(800, () => textField.setText(""))
	}
}
