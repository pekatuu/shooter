package jp.pekatuu.android.util.gui;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.hardware.SensorEvent;
import android.view.MotionEvent;

public class GUIManager {
	private final ArrayList<GUIWidget> widgets;
	// FIXME quick impl
	public static GUIWidget locks[];
	public static int lockIDs[];

	public GUIManager() {
		this.widgets = new ArrayList<GUIWidget>();
		locks = new GUIWidget[2];
		locks[0] = null;
		locks[1] = null;
		lockIDs = new int[] { -1, -1 };
	}

	public GUIManager add(GUIWidget widget) {
		this.widgets.add(widget);
		return this;
	}

	public void clear() {
		this.widgets.clear();
	}
	
	public void draw(GL10 gl){
		for (int i = 0, max = widgets.size(); i < max; i++){
			widgets.get(i).draw(gl);
		}		
	}
	
	public void unlockAll(){
		if (locks[0] != null){
			unlock(0);
		}
		if (locks[1] != null){
			unlock(1);
		}
	}
	
	public void unlock(int i){
		locks[i].reset();
		locks[i] = null;
		lockIDs[i] = -1;
	}
	public int x1, y1, x2, y2; 
	public void processTouchEvent(MotionEvent event) {
		final int motion = event.getAction();
		final int newIndex = (motion & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		final int newID = event.getPointerId(newIndex);
		final int type = motion & MotionEvent.ACTION_MASK;
		final boolean isLock0Free = null == locks[0];
		final int touchCount = event.getPointerCount();

		switch (type) {
		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < touchCount; i++) {
				final int currentID = event.getPointerId(i);
				if (currentID == lockIDs[0]) {
					x1 = (int) event.getX(i); y1 = (int) event.getY(i);
					locks[0].processEvent(event.getX(i), event.getY(i), type);
				} else if (currentID == lockIDs[1]) {
					x2 = (int) event.getX(i); y2 = (int) event.getY(i);
					locks[1].processEvent(event.getX(i), event.getY(i), type);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (newID == lockIDs[0]) {
				unlock(0);
			} else if (newID == lockIDs[1]) {
				unlock(1);
			} 
			break;
		case MotionEvent.ACTION_POINTER_UP:
			for (int i = 0; i < touchCount; i++) {
				final int currentID = event.getPointerId(i);
				if (currentID == newID) {
					if (newID == lockIDs[0]) {
						unlock(0);
					} else if (newID == lockIDs[1]) {
						unlock(1);
					}
				} else {
					if (currentID == lockIDs[0]) {
						locks[0].processEvent(event.getX(i), event.getY(i), type);
					} else if (currentID == lockIDs[1]) {
						locks[1].processEvent(event.getX(i), event.getY(i), type);
					}
				}
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			for (int i = 0; i < touchCount; i++) {
				final int currentID = event.getPointerId(i);
				if (currentID == newID) {
					for (final GUIWidget widget : widgets) {
						if (widget.isTouched(event.getX(i), event.getY(i))) {
							final int newLockIndex = isLock0Free ? 0 : 1;
							locks[newLockIndex] = widget;
							locks[newLockIndex].processEvent(event.getX(i),
									event.getY(i), type);
							lockIDs[newLockIndex] = newID;
						}
					}
				} else {
					if (currentID == lockIDs[0]) {
						locks[0].processEvent(event.getX(i), event.getY(i), type);
					} else if (currentID == lockIDs[1]) {
						locks[1].processEvent(event.getX(i), event.getY(i), type);
					}
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
			for (final GUIWidget widget : widgets) {
				if (widget.isTouched(event.getX(), event.getY())) {
					final int newLockIndex = isLock0Free ? 0 : 1;
					locks[newLockIndex] = widget;
					locks[newLockIndex]
							.processEvent(event.getX(), event.getY(), type);
					lockIDs[newLockIndex] = newID;
				}
			}
			break;
		default:
			break;
		}
	}

	public static void processDown() {

	}

	public void processSensorEvent(SensorEvent event) {
		for (final GUIWidget widget : widgets) {
			widget.processSensorEvent(event, locks);
		}
	}
}
