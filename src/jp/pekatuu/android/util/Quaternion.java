package jp.pekatuu.android.util;

import jp.pekatuu.android.util.geometory.Vector3D;
import android.util.FloatMath;

public class Quaternion {
	public float w;
	public Vector3D v;
//	private static float[] result = new float[9];
	
//	private static Quaternion tempQ = new Quaternion();
	
	public Quaternion(){
		this(1f, 0f, 0f, 0f);
	}

	public Quaternion(float w, float x, float y, float z){
		this(w, new Vector3D(x, y, z));
	}
	
	public Quaternion(float w, Vector3D v){
		this.w = w;
		this.v = new Vector3D(v);
	}
	
	public void set(float w, float x, float y, float z){
		this.w = w;
		this.v.set(x, y, z);
	}
	
	public void set(float w, Vector3D v){
		this.set(w, v.x, v.y, v.z);
	}
	
	public void set(Quaternion q){
		this.set(q.w, q.v);
	}
	
	public static void getRotationMatrix(float[] result, Quaternion q){
		float x2 = q.v.x * q.v.x * 2f;
		float y2 = q.v.y * q.v.y * 2f;
		float z2 = q.v.z * q.v.z * 2f;
		float xy = q.v.x * q.v.y * 2f;
		float yz = q.v.y * q.v.z * 2f;
		float zx = q.v.z * q.v.x * 2f;
		float xw = q.v.x * q.w * 2f;
		float yw = q.v.y * q.w * 2f;
		float zw = q.v.z * q.w * 2f;
		result[ 0] = 1.0f - y2 - z2;
		result[ 1] = xy + zw;
		result[ 2] = zx - yw;
		result[ 4] = xy - zw;
		result[ 5] = 1.0f - z2 - x2;
		result[ 6] = yz + xw;
		result[ 8] = zx + yw;
		result[ 9] = yz - xw;
		result[10] = 1.0f - x2 - y2;
		result[15] = 1.0f;
	}
	
	public static void mult(Quaternion out, Quaternion p, Quaternion q){
		out.set(p.w * q.w - p.v.x * q.v.x - p.v.y * q.v.y - p.v.z * q.v.z,
				p.w * q.v.x + p.v.x * q.w + p.v.y * q.v.z - p.v.z * q.v.y,
				p.w * q.v.y - p.v.x * q.v.z + p.v.y * q.w + p.v.z * q.v.x,
				p.w * q.v.z + p.v.x * q.v.y - p.v.y * q.v.x + p.v.z * q.w);
	}

	/**
	 * rotate vector around vector n. v is modified to rotated position
	 */
	public static void rotate(Vector3D v, Vector3D n, float angle){
		float sin = FloatMath.sin(angle / 2.0f);
		float w = FloatMath.cos(angle / 2.0f);
		float x = n.x * sin;
		float y = n.y * sin;
		float z = n.z * sin;
		float xSq2 = 2 * x * x;
		float ySq2 = 2 * y * y;
		float zSq2 = 2 * z * z;
		float wx2 = 2 * x * w;
		float wy2 = 2 * y * w;
		float wz2 = 2 * z * w;
	/*	
		result[0] = 1 - ySq2 - zSq2;
		result[1] = 2 * x * y + wz2;
		result[2] = 2 * x * z - wy2;
		result[3] = 2 * x * y - wz2;
		result[4] = 1 - xSq2 - zSq2;
		result[5] = 2 * y * z + wx2;
		result[6] = 2 * x * z + wy2;
		result[7] = 2 * y * z - wx2;
		result[8] = 1 - xSq2 - ySq2;
		v.set(
				v.x * result[0] + v.y * result[3] + v.z * result[6],
				v.x * result[1] + v.y * result[4] + v.z * result[7],
				v.x * result[2] + v.y * result[5] + v.z * result[8]
		);*/
		v.set(
				v.x * (1 - ySq2 - zSq2) + v.y * (2 * x * y - wz2) + v.z * (2 * x * z + wy2),
				v.x * (2 * x * y + wz2) + v.y * (1 - xSq2 - zSq2) + v.z * (2 * y * z - wx2),
				v.x * (2 * x * z - wy2) + v.y * (2 * y * z + wx2) + v.z * (1 - xSq2 - ySq2)
				);
	}
	
	
}
