package jp.pekatuu.android.zaqzaq.actor;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.Actor;
import jp.pekatuu.android.util.BufferAllocator;

public class Player extends Actor{

	public Player(){
	}

	@Override
	public void move() {


	}
	
	public void draw(GL10 gl){
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
		gl.glColor4f(1f, 1f, 1f, 1f);
		// player body
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
