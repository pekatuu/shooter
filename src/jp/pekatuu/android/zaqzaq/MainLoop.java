package jp.pekatuu.android.zaqzaq;

public class MainLoop extends Thread{
	private boolean done;
		
	MainLoop(){
		done = false;
	}
	
	public void done(){
		this.done = true;
	}
	
	@Override
	public void run(){
		while (!done){
			final long lastTime = System.currentTimeMillis();
			
			// game logic
			GameData.player.move();
			GameData.enemy.move();
			
			int timeToSleep = (int) (GameConstants.TIME_PER_FRAME - System.currentTimeMillis() + lastTime);
			if (0 < timeToSleep){
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					// through exception
				}
			}
		}
	}
}
