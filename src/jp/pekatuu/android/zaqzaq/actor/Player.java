package jp.pekatuu.android.zaqzaq.actor;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.Actor;
import jp.pekatuu.android.util.BufferAllocator;
import jp.pekatuu.android.util.geometory.Vector3D;
import jp.pekatuu.android.zaqzaq.GameData;

public class Player extends Actor{
	//constants
	private static final float CUTTER_HEAD_DEADZONE = 10f;
	private static final float CUTTER_HEAD_ACC_SCALE = 0.07f;
	private static final float CUTTER_HEAD_REGISTANCE = 0.9f;
	
	// vars
	//private float prevX, prevY;
	private Vector3D cutterHead;
	private Vector3D cutterHeadVec;
	private Vector3D cutterHeadPrev;
	private float accX, accY;
	
	private Vector3D v1, v2;
	
	public Player(){
		cutterHead = new Vector3D();
		cutterHeadVec = new Vector3D();
		cutterHeadPrev = new Vector3D();
		v1 = new Vector3D(); 
		v2 = new Vector3D(); 
	}

	@Override
	public void move() {
		cutterHeadPrev.set(cutterHead);

		// Player has been moved by touch event.
		///// Move cutter
		// clamp acceleration vector;
		final Vector3D temp = p.sub(cutterHead);
		final float dist = temp.size();
		Vector3D.normalize(temp);
		
		if (dist > CUTTER_HEAD_DEADZONE){
			accX = temp.x * (dist - 10f) * CUTTER_HEAD_ACC_SCALE;
			accY = temp.y * (dist - 10f) * CUTTER_HEAD_ACC_SCALE;
		}
		cutterHeadVec.x = cutterHeadVec.x * CUTTER_HEAD_REGISTANCE + accX;
		cutterHeadVec.y = cutterHeadVec.y * CUTTER_HEAD_REGISTANCE + accY;		
		Vector3D.add(cutterHead, cutterHeadVec);
		
		//check collision
		if (isCollideWithEnemy()){
			GameData.enemy.vec.set(cutterHeadVec);
		}		
	}
	
	private boolean isCollideWithEnemy(){
		int i = 0;
		v1.set(cutterHeadPrev.sub(p));
		v2.set(GameData.enemy.p.sub(p));
		i += hoge(v1,v2) > 0f ? 1 : -1;

		v1.set(p.sub(cutterHead));
		v2.set(GameData.enemy.p.sub(cutterHead));
		i += hoge(v1,v2) > 0f ? 2 : -2;
		
		v1.set(cutterHead.sub(cutterHeadPrev));
		v2.set(GameData.enemy.p.sub(cutterHeadPrev));
		i += hoge(v1,v2) > 0f ? 4 : -4;
			
		return i == 7 || i == -7;
	}
	
	private float hoge(Vector3D a, Vector3D b){
		return a.x * b.y - a.y * b.x;
	}
	
	public void draw(GL10 gl){
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
		gl.glColor4f(1f, 1f, 1f, 1f);
		// player body
		gl.glPushMatrix();
		gl.glTranslatef(p.x, p.y, p.z);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// cutter head
		cutterVBBuffer[0] = p.x;
		cutterVBBuffer[1] = p.y;
		cutterVBBuffer[2] = cutterHeadPrev.x;
		cutterVBBuffer[3] = cutterHeadPrev.y;
		cutterVBBuffer[4] = cutterHead.x;
		cutterVBBuffer[5] = cutterHead.y;
		cutterVBBuffer[6] = p.x;
		cutterVBBuffer[7] = p.y;
		
		cutterVB.put(cutterVBBuffer);
		cutterVB.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, cutterVB);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
	}
	
	private static FloatBuffer vb;
	private static FloatBuffer cutterVB;
	private static float[] cutterVBBuffer = new float[8];
	static {
		float SIZE = 5f;
		float[] buffer = {
				-SIZE, -SIZE, 
				SIZE, -SIZE, 
				-SIZE, SIZE, 
				SIZE, SIZE,
		};
		vb = BufferAllocator.allocateFloatBuffer(buffer);
		float[] buffer2 = {
				-SIZE, -SIZE, 
				SIZE, -SIZE, 
				-SIZE, SIZE, 
				SIZE, SIZE,
		};
		cutterVB = BufferAllocator.allocateFloatBuffer(buffer2);
	}
}
