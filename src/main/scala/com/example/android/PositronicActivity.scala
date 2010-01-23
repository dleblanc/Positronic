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

class PositronicActivity extends Activity {
	
	var gameView:AndroidGameView = null
	
    override def onCreate(savedInstanceState: Bundle) {
	    super.onCreate(savedInstanceState)
		val mainView = getLayoutInflater().inflate(R.layout.positronic, null)
		gameView = new AndroidGameView(this, mainView)
		setContentView(mainView)
    }

	override def onPause() = {
		gameView.activityPaused()
		super.onPause()
	}
}

