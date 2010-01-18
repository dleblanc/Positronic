package game.util

import _root_.android.os.Handler

class AndroidDelayedRunner extends DelayedRunner {
  val handler = new Handler()
	
  def runDelayedOnce(delay: Int, func: () => Unit) = {
    
      val timertask = new Runnable() {
            def run(): Unit = {
            	func()
            }
        };
    handler.removeCallbacks(timertask)
    handler.postDelayed(timertask, delay)
  }

  def runDelayedRepeating(delay: Int, func: () => Unit) = {
 
	lazy val repeatFunc:() => Unit = () => {
	    func()
		runDelayedOnce(delay, repeatFunc)
	}
     runDelayedOnce(delay, repeatFunc)
  }
}