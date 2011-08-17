package jp.pekatuu.android.util.gui;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.BufferAllocator;
import android.hardware.SensorEvent;

public class GUIButton extends GUIWidget {
	public boolean isPressed;

	private final FloatBuffer bbVB;

	public GUIButton(float width, float height, String name,
			GUI_BASE_POSITION basePos, float _offsetX, float _offsetY) {
		super(width, height, name, basePos, _offsetX, _offsetY);
		this.bbVB = this.allocateResource();
	}

	public GUIButton(float width, float height, String name,
			GUIWidget baseWidget, GUI_BASE_POSITION orientation, float offset) {
		super(width, height, name, baseWidget, orientation, offset);
		this.bbVB = this.allocateResource();
	}

	private FloatBuffer allocateResource() {
		float[] bbBuffer = {
				this.bb.minX, this.bb.minY, 
				this.bb.minX, this.bb.maxY, 
				this.bb.maxX, this.bb.minY,
				this.bb.maxX, this.bb.maxY
				};
		return BufferAllocator.allocateFloatBuffer(bbBuffer);
	}

	public void draw(GL10 gl) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bbVB);
		if (isPressed) {
			gl.glColor4f(0f, 1f, 0f, 0.65f);
		} else {
			gl.glColor4f(0f, 1f, 0f, 0.35f);
		}
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	protected void processEvent(final float x, final float y, final int type) {
		isPressed = true;
	}

	@Override
	protected void reset() {
		isPressed = false;
	}

	@Override
	protected void processSensorEvent(SensorEvent event, GUIWidget[] locks) {

	}
}
