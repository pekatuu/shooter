package jp.pekatuu.android.util;

import jp.pekatuu.android.util.geometory.Vector3D;

public final class GLMatrix {
	public static void updateModelViewMatrix(float eyeX, float eyeY,
			float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
		F.set(centerX - eyeX, centerY -eyeY, centerZ - eyeZ);
		Vector3D.normalize(F);
		UP.set(upX, upY, upZ);
		Vector3D.normalize(UP);
		
		UP.set(F.cross(UP)); // as s
		Vector3D u = UP.cross(F);
		
		modelViewMatrix[0] = UP.x;
		modelViewMatrix[1] = u.x;
		modelViewMatrix[2] = -F.x;
		modelViewMatrix[3] = 0f;

		modelViewMatrix[4] = UP.y;
		modelViewMatrix[5] = u.y;
		modelViewMatrix[6] = -F.y;
		modelViewMatrix[7] = 0f;
		
		modelViewMatrix[8] = UP.z;
		modelViewMatrix[9] = u.z;
		modelViewMatrix[10] = -F.z;
		modelViewMatrix[11] = 0f;
		
		modelViewMatrix[12] = 0f;
		modelViewMatrix[13] = 0f;
		modelViewMatrix[14] = 0f;
		modelViewMatrix[15] = 1f;
		
		translateMatrix[12] = -eyeX;
		translateMatrix[13] = -eyeY;
		translateMatrix[14] = -eyeZ;
		
		Matrix4x4.mult(modelViewMatrix, modelViewMatrix, translateMatrix);
	}

	public static void updateProjectionMatrix(float left, float right, float bottom, float top,
			float zNear, float zFar) {
		float topMinusBottom = top - bottom;
		float rightMinuxLeft = right - left;
		projectionMatrix[0] = (2 * zNear) / rightMinuxLeft;
		projectionMatrix[5] = (2 * zNear) / topMinusBottom;
		projectionMatrix[8] = (right + left) / rightMinuxLeft;
		projectionMatrix[9] = (top + bottom) / topMinusBottom;
		projectionMatrix[10] = - (zFar + zNear)/(zFar - zNear);
		projectionMatrix[14] = - (2 * zFar * zNear) / (zFar - zNear);
	}

	private static final Vector3D F = new Vector3D();
	private static final Vector3D UP = new Vector3D();
	public static final int[] viewPort= {0, 0, 0, 0};
	private static final float[] translateMatrix ={
		1f, 0f, 0f, 0f,
		0f, 1f, 0f, 0f,
		0f, 0f, 1f, 0f,
		0f, 0f, 0f, 1f
	};
	public static final float[] projectionMatrix = {
		0f, 0f, 0f, 0f,
		0f, 0f, 0f, 0f,
		0f, 0f, 0f, -1f,
		0f, 0f, 0f, 0f
	};
	public static final float[] modelViewMatrix = {
		0f, 0f, 0f, 0f,
		0f, 0f, 0f, 0f,
		0f, 0f, 0f, 0f,
		0f, 0f, 0f, 1f
	};
}
