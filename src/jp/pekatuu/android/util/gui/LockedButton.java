package jp.pekatuu.android.util.gui;

import android.view.MotionEvent;


/**
 * this button does not permit multilock
 * @author goro
 *
 */
public class LockedButton extends GUIButton {
	private MotionEventAdapter adapter;
	public LockedButton(float width, float height, String name,
			GUI_BASE_POSITION basePos, float _offsetX, float _offsetY) {
		super(width, height, name, basePos, _offsetX, _offsetY);
	}

	public LockedButton(float width, float height, String name,
			GUIWidget baseWidget, GUI_BASE_POSITION orientation, float offset) {
		super(width, height, name, baseWidget, orientation, offset);
	}
	
	public void setAdapter(final MotionEventAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	protected void processEvent(final float x, final float y, final int type) {
		if (adapter != null){
			switch (type){
			case MotionEvent.ACTION_DOWN:
				adapter.down(x, y);
				break;
			case MotionEvent.ACTION_UP:
				adapter.up(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				adapter.move(x, y);
				break;
			}
		}
		if (type != MotionEvent.ACTION_DOWN){
			isPressed = false;
			return;
		}
		isPressed = true;
	}

}
