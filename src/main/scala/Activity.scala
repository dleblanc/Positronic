package com.example.android
 
import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

import game._
import game.util._

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    // TODO: use declarative layout here instead

	val textView = new TextView(this)
    setContentView(textView)
    val controller = new GameController(new GameView {
            override def highlightCell(x: Int, y:Int) = {
                textView.setText("highlighting: " + x + ", " + y)
            }
        }
    , 3, 3, new AndroidDelayedRunner())
      // TODO: use the DI-like stuff for injecting a default delayed runner here
      
    controller.startGame()
  }
}



