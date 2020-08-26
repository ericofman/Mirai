package com.org.game.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

	private TextureRegion[] frames;
	private int currentFrame;
	private int numFrames;

	private int delay;
	private int time;

	private int playCount;

	public Animation() { }

	public Animation(TextureRegion[] frames, int delay) {
		setAnimation(frames, delay);
	}

	public void setAnimation(TextureRegion[] frames, int delay) {
		this.frames = frames;
		this.delay = delay;
		currentFrame = 0;
		numFrames = frames.length;
		time = 0;
		playCount = 0;
	}

	public void update() {
		if (delay <= 0)
			return;
		time++;
		while (time >= delay) {
			time -= delay;
			currentFrame++;
			if (currentFrame == numFrames) {
				currentFrame = 0;
				playCount++;
			}
		}
	}

	public void setNumFrames(int i) {
		numFrames = i;
	}

	public TextureRegion getImage() {
		return frames[currentFrame];
	}

	public TextureRegion[] getFrames() {
		return frames;
	}

	public int getFrame() {
		return currentFrame;
	}

	public int getTimer() {
		return time;
	}

	public int getPlayCount() {
		return playCount;
	}

	public boolean hasPlayedOnce() {
		return playCount > 0;
	} 
}
