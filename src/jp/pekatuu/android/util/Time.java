package jp.pekatuu.android.util;

public class Time {
	public static int interval;
	public static int prevInterval;
	public static int current = 0;
	public static int previous = 0;
	private static long previousAbsoluteMillis = System.currentTimeMillis();
	private static long currentAbsoluteMillis = System.currentTimeMillis();
	
	public static int minSecPerFrame = 25;
	public static int maxSecPerFrame = 50;
	
	public static void update(){
		previous = current; 
		previousAbsoluteMillis = currentAbsoluteMillis;
		currentAbsoluteMillis = System.currentTimeMillis();
		current += currentAbsoluteMillis - previousAbsoluteMillis;
		
		prevInterval = interval;
		interval = (int) (current - previous);
		if (interval > maxSecPerFrame){
			interval = maxSecPerFrame;
			current = previous + maxSecPerFrame;
		}
	}
	
	public static void stop(){
		interval = 0;
		current = previous;
		previousAbsoluteMillis = System.currentTimeMillis();
	}
	
	public static void updateCurrent(){
		currentAbsoluteMillis = System.currentTimeMillis();
		current = previous + minSecPerFrame;

		interval = minSecPerFrame;
	}
}
