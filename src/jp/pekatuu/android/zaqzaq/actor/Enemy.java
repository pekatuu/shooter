package jp.pekatuu.android.zaqzaq.actor;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.Actor;
import jp.pekatuu.android.util.BufferAllocator;
import jp.pekatuu.android.util.geometory.Vector3D;

public class Enemy extends Actor{
	@Override
	public void move() {
		Vector3D.scale(vec, 0.9f);
		Vector3D.add(p, vec);
		if (-100f > p.x){
			p.x = -100f;
		} else if (p.x > 100f){
			p.x = 100f;
		}
		
		if (-100f > p.y){
			p.y = -100f;
		} else if (p.y > 100f){
			p.y = 100f;
		}
		
	}
	
	public void draw(GL10 gl){
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
		gl.glColor4f(1f, 0.8f, 0.8f, 1f);

		gl.glPushMatrix();
		gl.glTranslatef(p.x, p.y, p.z);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
	}
	
	private static FloatBuffer vb;
	static {
		float SIZE = 5f;
		float[] buffer = {
				-SIZE, -SIZE, 
				SIZE, -SIZE, 
				-SIZE, SIZE, 
				SIZE, SIZE,
		};
		vb = BufferAllocator.allocateFloatBuffer(buffer);
	}
}
