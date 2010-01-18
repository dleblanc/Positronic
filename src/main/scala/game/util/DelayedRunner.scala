package game.util

trait DelayedRunner {
  def runDelayedOnce(delay: Int, func: () => Unit)
  def runDelayedRepeating(delay: Int, func: () => Unit)
}