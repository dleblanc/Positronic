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

import scala.util._
import game._
import game.util._


class MainActivity extends Activity {
	val delayedRunner = new AndroidDelayedRunner()
	val randomGenerator = new Random()
	
	// TODO: try this instead? val res = 1 to 3 map (i => 1 to 3 map (j => i * j)).toList
	// var squaresByRow =		
	// 	for (row <- 0 until 3)
	// 	  yield for (col <- 0 until 3)
	// 	    yield new Button(this)
	var squaresByRow = new scala.collection.mutable.ListBuffer[scala.collection.mutable.ListBuffer[Button]]

    override def onCreate(savedInstanceState: Bundle) {
	
	    super.onCreate(savedInstanceState)

		val layout = new TableLayout(this)
		layout.setStretchAllColumns(true)
	
		val mainView = getLayoutInflater().inflate(R.layout.positronic, null)
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
	
		val gameView = new GameView {
		  def startGame(): Unit = {}

		  def highlightCell(x: Int, y:Int) = {
			val square = squaresByRow(x)(y)

			// Not working - just makes the patch black (go full 2d instead?)
			// val selectedDrawable = new ShapeDrawable(new RoundRectShape(Array(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f), new RectF(), null))
			// selectedDrawable.getPaint().setColor(0xFF000000)
			// square.setBackgroundDrawable(selectedDrawable)
					
			val animation = new AlphaAnimation(0.0f, 1.0f)
			animation.setDuration(500)
			square.startAnimation(animation)
		  }
		
		  def successfulPositionMatch() = {
			Log.d("Positronic", "matched position")
		  }
		}
	
	    // TODO: use the DI-like stuff for injecting a default delayed runner here	
	    val controller = new GameController(gameView, 3, 3, new AndroidDelayedRunner())
	  
	    controller.startGame()
	
		val positionMatchButton = mainView.findViewById(R.id.positionMatchButton)
		positionMatchButton.setOnClickListener(new OnClickListener() {
			override def onClick(view: View): Unit = {
				controller.positionMatchFromView()
			}
		})
    }


}



