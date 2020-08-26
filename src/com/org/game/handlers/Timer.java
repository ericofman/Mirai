package com.org.game.handlers;

public class Timer {

	public long miliSeconds, targetTime;
	public long now;
	
	private boolean done = false;
	private boolean start = false;
	
	public Timer(double sec) {
		miliSeconds = (long) (sec * 1000);
	}
	
	public void tick() {
		if(System.currentTimeMillis() >= targetTime && start) {
			done = true;
		}
	}
	
	public void start() {
		now = System.currentTimeMillis();
		targetTime = now + miliSeconds;
		start = true;
	}
	
	public void reset() {
		now = System.currentTimeMillis();
		targetTime = now + miliSeconds;
		done = false;
	}
	
	public boolean isDone() {
		return done;
	}
}
