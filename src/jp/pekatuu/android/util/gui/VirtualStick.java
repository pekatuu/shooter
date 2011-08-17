package jp.pekatuu.android.util.gui;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.BufferAllocator;
import jp.pekatuu.android.zaqzaq.GameData;
import android.hardware.SensorEvent;

public class VirtualStick extends GUIWidget{
	public static final float SIZE = 0.45f;
	private final FloatBuffer stickVB;
	private float limit;
	
	public float x, y;
	public float centerX, centerY;
	

	public VirtualStick(float offx, float offy) {
		super(SIZE, SIZE, "VSTICK", GUI_BASE_POSITION.SOUTH_WEST, offx, offy);
		x = 0.0f;
		y = 0.0f;
		limit = SIZE * GameData.height / 4.0f;
		
		this.centerX = (this.bb.maxX + this.bb.minX) / 2.0f;
		this.centerY = (this.bb.maxY + this.bb.minY) / 2.0f;
		/*float crossSize = GameData.height * 0.025f;
		float[] centerCrossBuffer = {centerX - crossSize, centerY,
				centerX + crossSize, centerY,
				centerX, centerY - crossSize,
				centerX, centerY + crossSize
		};
		this.centerCrossVB = BufferAllocator.allocateFloatBuffer(centerCrossBuffer);
		
		float[] bbBuffer = {this.bb.minX, this.bb.minY,
				//this.bb.minX, this.bb.maxY,
				//this.bb.maxX, this.bb.maxY,
				//this.bb.maxX, this.bb.minY,
				//this.bb.minX, this.bb.minY
		};
		this.bondingBoxVB = BufferAllocator.allocateFloatBuffer(bbBuffer);*/
		
		float[] stickBuffer = {
				bb.minX + limit, bb.minY + limit,
				bb.minX + limit, bb.maxY - limit,
				bb.maxX - limit, bb.minY + limit,
				bb.maxX - limit, bb.maxY - limit,
		};
		this.stickVB = BufferAllocator.allocateFloatBuffer(stickBuffer);
	}

	public void draw(GL10 gl) {
//		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, centerCrossVB);
//		gl.glDrawArrays(GL10.GL_LINES, 0, 4); 
//		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bondingBoxVB);
//		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 5);
		gl.glPushMatrix();
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, stickVB);
		gl.glTranslatef(x * limit, y * limit, 0.0f);
		gl.glColor4f(0f, 1f, 0f, 0.35f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
	}

	public void set(float _x, float _y){
		// clamp x and y to -1.0 - 1.0
		x = _x;
		y = _y;
		if (x > 1.0f){
			x = 1.0f;
		} else if (x < -1.0f){
			x = -1.0f;
		}
		if (y > 1.0f){
			y = 1.0f;
		} else if (y < -1.0f){
			y = -1.0f;
		}
	}
	
	protected void allocateGLResource(){
		
	}
	
	@Override
	protected void processEvent(final float x, final float y, final int type) {
		set((x - centerX) / limit,
				(y - centerY) / limit);
	}
	
	@Override
	protected void processSensorEvent(SensorEvent event, GUIWidget[] locks){
		if (locks[0] == this || locks[1] == this){
			return;
		}
		final float sensi = 1f;;
		float roll = event.values[1] / sensi;
		float pitch= event.values[2] / sensi ;
		set(-roll, pitch);
	}

	@Override
	protected void reset() {
		this.x = 0.0f;
		this.y = 0.0f;
	}
}
