package jp.pekatuu.android.util;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.util.geometory.Vector3D;

public class GLText {
	private final FloatBuffer vb;
	private final FloatBuffer tb;
	private final int verticiesCount;
	public final float height;
	public final float width;
	private final float halfW;
	private final float halfH;
	public final Vector3D offset;

	private static int FONT_ARRAY_SIZE = 8;
	private static float FONT_SIZE = 1f / (float) FONT_ARRAY_SIZE;

	private static final int MAX_DIGITS = 8;

	public GLText(final char[] text, final int gX, int gY) {
		this(text, 1f, gX, gY);
	}

	public GLText(final char[] text, final float size, int gX, int gY) {
		this(text, size, 0f, 0f, 0f, gX, gY);
	}

	public GLText(final char[] text, final float size, final float x,
			final float y, final float z, final int gX, final int gY) {
		this.offset = new Vector3D(x, y, z);
		final int length = text.length;
		this.width = size * length;
		this.height = size;
		this.halfW = this.width / 2f;
		this.halfH = this.height / 2f;
		this.verticiesCount = 4 * length;
		final float[] vertex = new float[4 * 2 * length];
		final float[] texcorrd = new float[4 * 2 * length];
		float offX = (gX == 0 ? 0f : gX == 1 ? -this.halfW : -this.width) + x;
		float offY = (gY == 0 ? 0f : gY == 1 ? -this.halfH : -this.height) + y;

		for (int i = 0; i < length; i++) {
			setTexCoord(texcorrd, text[i], 4 * 2 * i);

			final float offU = size * i;
			final int idx = 4 * 2 * i;
			// 2 4
			// | \ |
			// 1 3
			vertex[idx + 0] = offX + offU;
			vertex[idx + 1] = offY;
			vertex[idx + 2] = offX + offU;
			vertex[idx + 3] = offY + size;
			vertex[idx + 4] = offX + offU + size;
			vertex[idx + 5] = offY;
			vertex[idx + 6] = offX + offU + size;
			vertex[idx + 7] = offY + size;
		}
		vb = BufferAllocator.allocateFloatBuffer(vertex);
		tb = BufferAllocator.allocateFloatBuffer(texcorrd);
	}

	public void draw(GL10 gl) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tb);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, verticiesCount);
	}

	private static void setTexCoord(float[] texcoord, char c, int offset) {
		// 2 4
		// | \ |
		// 1 3
		int fixedC = c - 32;
		float u = (fixedC % FONT_ARRAY_SIZE) * FONT_SIZE;
		float v = (fixedC / FONT_ARRAY_SIZE) * FONT_SIZE;
		texcoord[offset] = u;
		texcoord[offset + 1] = v;
		texcoord[offset + 2] = u;
		texcoord[offset + 3] = v + FONT_SIZE;
		texcoord[offset + 4] = u + FONT_SIZE;
		texcoord[offset + 5] = v;
		texcoord[offset + 6] = u + FONT_SIZE;
		texcoord[offset + 7] = v + FONT_SIZE;
	}

	public static void beforeDrawText(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, GLTextureFactory.FONT1);
	}

	public static void afterDrawText(GL10 gl) {
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public static void beforeDrawTextNum(GL10 gl) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, numVB);
	}

	public static void drawNum(GL10 gl, int num, float x, float y, float z,
			float size, int yAlign) {
		gl.glPushMatrix();
		if (yAlign == 2){
			gl.glTranslatef(x - size * MAX_DIGITS, y, z);
		} else if (yAlign == 0){
			gl.glTranslatef(x - size * MAX_DIGITS, y - size, z);
		} else if (yAlign == 1) {
			gl.glTranslatef(x - size * MAX_DIGITS, y - size / 2f, z);
		}
		gl.glScalef(size, size, 0f);

		int charCount = 0;
		int n;
		int temp = num;

		if (num < 0) {
			temp = -temp;
		}
		do {
			n = temp % 10;
			charCount++;
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, numTBs[n]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,
					(MAX_DIGITS - charCount) * 4, 4);
			temp /= 10;
		} while (temp > 0);
		if (num < 0) {
			charCount++;
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, numTBs[10]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,
					(MAX_DIGITS - charCount) * 4, 4);
		}
		gl.glPopMatrix();
	}

	private static final FloatBuffer numVB;
	private static final FloatBuffer[] numTBs;
	static {
		numTBs = new FloatBuffer[11];
		for (int i = 0; i < 10 + 1/* for minus */; i++) {
			float[] numCoords = new float[MAX_DIGITS * 4 * 2];
			if (i != 10) {
				for (int j = 0; j < MAX_DIGITS; j++) {
					setTexCoord(numCoords, (char) (i + 48), j * 8);
				}
			} else {
				for (int j = 0; j < MAX_DIGITS; j++) {
					setTexCoord(numCoords, '-', j * 8);
				}
			}
			numTBs[i] = BufferAllocator.allocateFloatBuffer(numCoords);
		}

		final float[] vertex = new float[MAX_DIGITS * 4 * 2];
		for (int i = 0; i < MAX_DIGITS; i++) {
			int off = i * 8;
			vertex[off + 0] = i;
			vertex[off + 1] = 0f;
			vertex[off + 2] = i;
			vertex[off + 3] = 1f;
			vertex[off + 4] = i + 1f;
			vertex[off + 5] = 0f;
			vertex[off + 6] = i + 1f;
			vertex[off + 7] = 1f;

		}
		numVB = BufferAllocator.allocateFloatBuffer(vertex);
	}
}
