package jp.pekatuu.android.util.geometory;

import android.util.FloatMath;

public class Vector3D {
	public static Vector3D result = new Vector3D(0f, 0f, 0f);
	public float x, y, z;

	public Vector3D() {
		this.set(0f, 0f, 0f);
	}
	
	public Vector3D(float x, float y, float z) {
		this.set(x, y, z);
	}

	public Vector3D(Vector3D p) {
		this.set(p);
	}

	public void set(Vector3D p) {
		this.set(p.x, p.y, p.z);
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float size(){
		return FloatMath.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3D sub(Vector3D o) {
		result.x = x - o.x;
		result.y = y - o.y;
		result.z = z - o.z;
		return result;
	}

	public Vector3D add(Vector3D o) {
		result.x = x + o.x;
		result.y = y + o.y;
		result.z = z + o.z;
		return result;
	}
	
	public static void add(Vector3D a, Vector3D b){
		a.set(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static void add(Vector3D a, float x, float y, float z){
		a.set(a.x + x, a.y + y, a.z + z);
	}

	public float getDistance2(Vector3D o) {
		return (this.x - o.x) * (this.x - o.x) + (this.y - o.y)
				* (this.y - o.y) + (this.z - o.z) * (this.z - o.z);
	}

	public float dot(Vector3D v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public Vector3D cross(Vector3D v) {
		result.x = y * v.z - z * v.y;
		result.y = z * v.x - x * v.z;
		result.z = x * v.y - y * v.x;
		return result;
	}

	public Vector3D scale(float factor) {
		result.set(x * factor, y * factor, z * factor);
		return result;
	}

	public Vector3D normalize() {
		float sizeSq = FloatMath.sqrt(x * x + y * y + z * z);
		result.set(x / sizeSq, y / sizeSq, z / sizeSq);
		return result;
	}

	public static void normalize(Vector3D v) {
		float sizeSq = FloatMath.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
		v.x /= sizeSq;
		v.y /= sizeSq;
		v.z /= sizeSq;
	}

	public static void scale(Vector3D v, float factor) {
		v.set(v.x * factor, v.y * factor, v.z * factor);
	}
	
	public static Vector3D addScaledUnMod(Vector3D a, Vector3D b, float scale){
		result.set(a.x + b.x * scale, a.y + b.y * scale, a.z + b.z * scale);
		return result;
	}
	
	public static void addScaled(Vector3D a, Vector3D b, float scale){
		a.set(a.x + b.x * scale, a.y + b.y * scale, a.z + b.z * scale);
	}

	public Vector3D sub(float x, float y, float z) {
		result.x = this.x - x;
		result.y = this.x - y;
		result.z = this.x - z;
		return result;
	}
	
	public static void setInterporated(Vector3D src, Vector3D dst, float r){
		Vector3D.scale(src, r);
		Vector3D.addScaled(src, dst, 1f - r);
	}

	public static void setInterporated(Vector3D result, Vector3D src, Vector3D dst, float r){
		result.set(src);
		Vector3D.scale(result, 1f - r);
		Vector3D.addScaled(result, dst, r);
	}
}
