package game.util

import _root_.android.os.Handler

class AndroidDelayedRunner extends DelayedRunner {
  val handler = new Handler()
	
  def runDelayed(delay: Int, func: () => Unit) = {
    
      val timertask = new Runnable() {
            def run(): Unit = {
            	func()
                handler.postDelayed(this, delay)
            }
        };
    handler.removeCallbacks(timertask)
    handler.postDelayed(timertask, delay)
  }
}