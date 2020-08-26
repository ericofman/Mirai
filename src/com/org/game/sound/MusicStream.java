package com.org.game.sound;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicStream {
	private static HashMap<String, Music> musicData = new HashMap<String, Music>();
	
	public static void loadMusic(String dir, String name) {
		Music music = Gdx.audio.newMusic(Gdx.files.internal("music/" + dir));
		musicData.put(name, music);
	}
	
	public static void stopSound(String name) {
		musicData.get(name).stop();
	}
	
	public static void playSound(String name) {
		musicData.get(name).play();
	}
	
	public static void pauseSound(String name) {
		musicData.get(name).pause();
	}
	
	public static void getVolume(String name) {
		musicData.get(name).getVolume();
	}
	
	public static void setVolume(String name, float vol) {
		musicData.get(name).setVolume(vol);
	}
	
	public static void setPanning(String name, float vol, float pan) {
		musicData.get(name).setPan(pan, vol);
	}
	
	public static void loopSound(String name, boolean loop) {
		musicData.get(name).setLooping(loop);
	}
	
	public static void disposeSound(String name) {
		musicData.get(name).dispose();
	}
	
	public static boolean isPlaying(String name) {
		return musicData.get(name).isPlaying();
	}
	
	public static boolean isLooping(String name) {
		return musicData.get(name).isLooping();
	}
	
	public static float getPosition(String name) {
		return musicData.get(name).getPosition();
	}
}