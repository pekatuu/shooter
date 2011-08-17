package jp.pekatuu.android.util.geometory;

public class Rectangle {
	public float minX, minY, maxX, maxY;

	public Rectangle(float x1, float y1, float x2, float y2) {
		this.minX = x1 < x2 ? x1 : x2;
		this.minY = y1;
		this.maxX = x2;
		this.maxY = y2;
	}
	
	public Rectangle(Rectangle other){
		this(other.minX, other.minY, other.maxX, other.maxY);
	}

	public boolean isCollideWith(float x, float y) {
		return this.minX <= x && x <= this.maxX && this.minY <= y && y <= this.maxY;
	}
	
	public float getWidth(){
		return this.maxX - this.minX;
	}
	
	public float getHeight(){
		return this.maxY - this.minY;
	}
}
