package jp.pekatuu.android.util.gui;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.geometory.Rectangle;
import jp.pekatuu.android.zaqzaq.GameData;
import android.hardware.SensorEvent;

public abstract class GUIWidget{
	public enum GUI_BASE_POSITION {
		CENTER, NORTH, NORTH_WEST, NORTH_EAST, SOUTH, SOUTH_EAST, SOUTH_WEST, WEST, EAST;
	}

	protected Rectangle bb;// bounding box
	protected final String name;

	public GUIWidget(float width, float height, String name,
			GUI_BASE_POSITION basePos, float _offsetX, float _offsetY) {
		this.bb = this.setPlace(width, height, basePos, _offsetX, _offsetY);
		this.name = name;
	}

	public GUIWidget(float width, float height, String name,
			GUIWidget baseWidget, GUI_BASE_POSITION orientation, float offset) {
		this.bb = this.setPlace(width, height, baseWidget, orientation, offset);
		this.name = name;
	}

	public boolean isTouched(float x, float y) {
		return this.bb.isCollideWith(x, y);
	}

	public abstract void draw(GL10 gl);
	
	protected abstract void processEvent(final float x, final float y, final int type);

	protected abstract void processSensorEvent(final SensorEvent event,
			GUIWidget[] locks);

	private Rectangle setPlace(float _width, float _height,
			GUI_BASE_POSITION basePos, float _offsetX, float _offsetY) {
		float maxX = GameData.width;
		float maxY = GameData.height;
		float width = maxY * _width;
		float height = maxY * _height;
		float offsetX = _offsetX * maxY;
		float offsetY = _offsetY * maxY;
		float centerX = maxX / 2.0f;
		float centerY = maxY / 2.0f;
		float bbCenterX = width / 2.0f;
		float bbCenterY = height / 2.0f;

		Rectangle result = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);

		switch (basePos) {
		case CENTER:
			result.minX = centerX - bbCenterX + offsetX;
			result.minY = centerY - bbCenterY + offsetY;
			result.maxX = centerX + bbCenterX + offsetX;
			result.maxY = centerY + bbCenterY + offsetY;
			break;
		case NORTH_EAST:
			result.minX = maxX - width + offsetX;
			result.minY = offsetY;
			result.maxX = maxX + offsetX;
			result.maxY = height + offsetY;
			break;
		case SOUTH_EAST:
			result.minX = maxX - width + offsetX;
			result.minY = maxY - height + offsetY;
			result.maxX = maxX + offsetX;
			result.maxY = maxY + offsetY;
			break;
		case SOUTH_WEST:
			result.minX = offsetX;
			result.minY = maxY - height + offsetY;
			result.maxX = width + offsetX;
			result.maxY = maxY + offsetY;
			break;
		default:
			break;
		}

		return result;
	}

	private Rectangle setPlace(float _width, float _height,
			GUIWidget baseWidget, GUI_BASE_POSITION orientation, float _offset) {
		Rectangle baseBB = baseWidget.bb;
		/*
		 * float baseWidth = baseBB.getWidth(); float baseHeight =
		 * baseBB.getHeight(); float bbCenterX = width / 2.0f; float bbCenterY =
		 * height / 2.0f;
		 */
		float width = GameData.height * _width;
		float height = GameData.height * _height;
		float offset = _offset * GameData.height;

		Rectangle result = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);

		switch (orientation) {
		case NORTH:
			result.minX = baseBB.minX;
			result.maxX = result.minX + width;
			result.minY = baseBB.minY - height - offset;
			result.maxY = result.minY + height;
			break;
		case EAST:
			result.minX = baseBB.maxX + offset;
			result.maxX = result.minX + width;
			result.minY = baseBB.minY;
			result.maxY = result.minY + height;
			break;
		case WEST:
			result.maxX = baseBB.minX - offset;
			result.minX = result.maxX - width;
			result.minY = baseBB.minY;
			result.maxY = result.minY + height;
			break;
		case SOUTH:
			result.minX = baseBB.minX;
			result.maxX = result.minX + width;
			result.minY = baseBB.maxY + offset;
			result.maxY = result.minY + height;
			break;
		default:
			break;
		}

		return result;
	}

	protected abstract void reset();
}
