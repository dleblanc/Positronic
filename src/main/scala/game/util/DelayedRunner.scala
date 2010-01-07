package game.util

trait DelayedRunner {
  def runDelayed(delay: Int, func: () => Unit)
}