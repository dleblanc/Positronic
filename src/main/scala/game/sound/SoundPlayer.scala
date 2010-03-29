package game.sound

import android.media._
import android.content._

import com.example.android.R
import game._

class SoundPlayer(context: Context) {
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