package jp.pekatuu.android.zaqzaq;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.GLMatrix;
import jp.pekatuu.android.util.GLText;
import jp.pekatuu.android.util.GLTextureFactory;
import jp.pekatuu.android.util.Time;
import jp.pekatuu.android.util.geometory.Vector3D;
import jp.pekatuu.android.util.gui.GUIManager;
import jp.pekatuu.android.util.gui.VirtualStick;
import jp.pekatuu.android.zaqzaq.actor.Shot;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.FloatMath;

public class ZaqZaqRenderer implements Renderer {
	private Context mContext;
	private GLText testText = new GLText("1234567890".toCharArray(),
			0.12f * GameData.height, GameData.width / 2f,
			0.25f * GameData.height, 0f, 1, 1);
	public GUIManager gui;
	private VirtualStick moveStick;
	private VirtualStick aimStick;
	private Vector3D aimVec = new Vector3D();
	private long nextTimeShoot = 0;
	private long currentTime;

	public ZaqZaqRenderer(Context mContext){
		super();
		this.mContext = mContext;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		currentTime = System.currentTimeMillis();
		// move
		movePlayer();
		GameData.shots.move();
		shot();

		// / draw
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		beforeDrawWorld(gl);
		GameData.player.draw(gl);
		GameData.enemy.draw(gl);
		GameData.shots.draw(gl);

		beforeDrawHUD(gl);

		// text
		GLText.beforeDrawText(gl);
		testText.draw(gl);
		GLText.afterDrawText(gl);

		gui.draw(gl);

		gl.glPopMatrix();
		gl.glPopMatrix();
		sleep();
	}

	private void shot() {
		if (nextTimeShoot > currentTime)
			return;
		float size2 = aimStick.x * aimStick.x + aimStick.y * aimStick.y;
		if (size2 < 0.1f)
			return;

		float size = FloatMath.sqrt(size2);
		aimVec.set(aimStick.x / size, -aimStick.y / size, 0f);

		Shot s = GameData.shots.newActor();
		if (s == null)
			return;

		s.init(GameData.player.p, aimVec, 5f);
		nextTimeShoot = currentTime + 100;
	}

	private void movePlayer() {
		Vector3D p = GameData.player.p;
		p.set(p.x + moveStick.x * 3f, p.y - moveStick.y * 3f, 0f);
	}

	private void sleep() {
		final int timeToSleep = (int) (System.currentTimeMillis() - currentTime);

		if (timeToSleep > 0 && timeToSleep < 17) {
			try {
				Thread.sleep(timeToSleep);
				Time.updateCurrent();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GameData.updateScreenSize(width, height);
		GLMatrix.viewPort[2] = width;
		GLMatrix.viewPort[3] = height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float) width / (float) height;
		GLMatrix.updateProjectionMatrix(-1f, 1f, -ratio, ratio,
				GameConstants.NEAR_CLIP, GameConstants.FAR_CLIP);
		gl.glMultMatrixf(GLMatrix.projectionMatrix, 0);

		gui = new GUIManager();
		moveStick = new VirtualStick(0f, 0f);
		aimStick = new VirtualStick(ratio - VirtualStick.SIZE, 0f/*-1f + VirtualStick.SIZE*/);
		gui.add(moveStick).add(aimStick);
		testText = new GLText("TEST".toCharArray(), 0.12f * GameData.height,
				GameData.width / 2f, 0.25f * GameData.height, 0f, 1, 1);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glEnable(GL10.GL_CULL_FACE);

		GLTextureFactory.allocateTextures(gl, mContext);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static void beforeDrawWorld(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		// gl.glOrthof(0f, 1f, 0f, GameData.height / GameData.width, -1000f,
		// 1000f);
		final float ratio = GameData.height / GameData.width;
		GLMatrix.updateProjectionMatrix(-1f, 1f, -ratio, ratio,
				GameConstants.NEAR_CLIP, GameConstants.FAR_CLIP);
		gl.glMultMatrixf(GLMatrix.projectionMatrix, 0);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// gluLookAt
		gl.glDisable(GL10.GL_BLEND);
		GLU.gluLookAt(gl, 0f, 0f, 100f, 0f, 0f, 0f, 0f, 1f, 0f);
	}

	public static void beforeDrawHUD(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, GameData.width, GameData.height, 0.0f, -1.0f, 1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		// gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glColor4f(0.0f, 1.0f, 0.0f, 0.9f);
	}
}
