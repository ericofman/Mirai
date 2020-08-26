package com.org.game.sound;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundStream {  
	private static HashMap<Long, Sound> sounds = new HashMap<Long, Sound>();
	
	public static void loadSound(String dir, long soundId) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + dir));
		sounds.put(soundId, sound);
	}
	
	public static void setVolume(long soundId, float vol) {
		sounds.get(soundId).setVolume(soundId, vol);
	}
	
	public static void stopSound(long soundId) {
		sounds.get(soundId).stop();
	}
	
	public static void playSound(long soundId) {
		sounds.get(soundId).play();
	}
	
	public static void pauseSound(long soundId) {
		sounds.get(soundId).pause();
	}
	
	public static void loopSound(long soundId, boolean loop) {
		sounds.get(soundId).setLooping(soundId, loop);
	}
	
	public static void disposeSound(long soundId) {
		sounds.get(soundId).dispose();
	}
}