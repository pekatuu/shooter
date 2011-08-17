package jp.pekatuu.android.zaqzaq.actor;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.ActorFactory;
import jp.pekatuu.android.util.BufferAllocator;
import jp.pekatuu.android.util.geometory.Vector3D;
import jp.pekatuu.android.zaqzaq.GameData;

public class Shots extends ActorFactory<Shot>{

	public Shots(int capacity) {
		super(capacity);
	}

	@Override
	protected Shot[] initActors(int capacity) {
		Shot[] result = new Shot[capacity];
		for (int i = 0; i < capacity; i++){
			result[i] = new Shot();
		}
		return result;
	}
	
	public void draw(GL10 gl){
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
		gl.glColor4f(1f, 0.8f, 0.8f, 1f);
		for (int i = 0; i < capacity; i++){
			if (!actors[i].isAlive) continue;
			Vector3D p = actors[i].p;
			gl.glPushMatrix();
			gl.glTranslatef(p.x, p.y, p.z);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		}
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
	public void move() {
		for (int i = 0; i < capacity; i++){
			Vector3D p = actors[i].p;
			if (p.x < -GameData.width/2 || GameData.width/2 < p.x || p.y < -GameData.height/2 || GameData.height/2 < p.y ) {
				actors[i].destroy();
				continue;
			}
			Vector3D.addScaled(p, actors[i].vec, actors[i].vel);
		}
	}
}
