package game.util

import _root_.android.os._

class AndroidDelayedRunner extends DelayedRunner {
	val MSG_TOKEN = new Object()
	
	val handler = new Handler()
	
	
	def runDelayedOnce(delay: Int, func: () => Unit) = {
    
	    val timertask = new Runnable() {
	    	def run(): Unit = {
				func()
	        }
	    };
		handler.postAtTime(timertask, MSG_TOKEN, SystemClock.uptimeMillis() + delay)
	}

	def runDelayedRepeating(delay: Int, func: () => Unit) = {
 
		lazy val repeatFunc:() => Unit = () => {
	    	func()
			runDelayedOnce(delay, repeatFunc)
		}
		
		runDelayedOnce(delay, repeatFunc)
	}

	def clearPendingEvents() = {
		handler.removeCallbacksAndMessages(MSG_TOKEN)
	}
}