package jp.pekatuu.android.zaqzaq;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class ZaqZaqActivity extends Activity {
	private long lastTime;
	private GLSurfaceView mSurfaceView;
	private ZaqZaqRenderer renderer;
	
	private MainLoop loop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSurfaceView = new GLSurfaceView(this);
		renderer = new ZaqZaqRenderer();
		mSurfaceView.setRenderer(renderer);
		setContentView(mSurfaceView);
		
		this.loop = new MainLoop();
		this.loop.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSurfaceView.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		mSurfaceView.onPause();
	}
	
	@Override 
	protected void onDestroy(){
		super.onDestroy();
		this.loop.done();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		renderer.gui.processTouchEvent(event);

		// sleep if possible
		final long timeToWake = lastTime + GameConstants.TOUCH_POLL_INTERVAL;
		lastTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < timeToWake) {
			;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}