package jp.pekatuu.android.util;

import static android.opengl.GLES10.GL_NEAREST;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexParameterf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GLTextureFactory {
	public static int BUILDING1;
	public static int BUILDING2;
	public static int ROAD1;
	public static int FONT1;
	public static int HUD;
	public static int SHIP;

	public static void allocateTextures(GL10 gl, Context c) {
		/*BUILDING1 = loadTextureAlt(gl, R.drawable.wall1, c);
		ROAD1= loadTextureAlt(gl, R.drawable.road, c);
		FONT1 = loadTextureAlt(gl, R.drawable.fonts, c);
		HUD = loadTextureAlt(gl, R.drawable.hud, c);
		SHIP = loadTextureAlt(gl, R.drawable.ship, c);*/
	}

	/**
	 * this method is from GLTutorialBase. http://code.google.com/p/android-gl/
	 * 
	 * @param gl
	 * @param bmp
	 * @param reverseRGB
	 * @return
	 */
	protected static int loadTexture(GL10 gl, Bitmap bmp, boolean reverseRGB) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight()
				* bmp.getWidth() * 4);
		bb.order(ByteOrder.BIG_ENDIAN);
		IntBuffer ib = bb.asIntBuffer();

		for (int y = bmp.getHeight() - 1; y > -1; y--)
			for (int x = 0; x < bmp.getWidth(); x++) {
				int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
				// Convert ARGB -> RGBA
				@SuppressWarnings("unused")
				byte alpha = (byte) ((pix >> 24) & 0xFF);
				byte red = (byte) ((pix >> 16) & 0xFF);
				byte green = (byte) ((pix >> 8) & 0xFF);
				byte blue = (byte) ((pix) & 0xFF);

				// It seems like alpha is currently broken in Android...
				ib.put(red << 24 | green << 16 | blue << 8 | 0xFF);// 255-alpha);
			}
		ib.position(0);
		bb.position(0);

		int[] tmp_tex = new int[1];

		gl.glGenTextures(1, tmp_tex, 0);
		int tex = tmp_tex[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(),
				bmp.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
		// GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		return tex;
	}

	protected static int loadTextureAlt(GL10 gl, int resID, Context c) {
		int[] textures = new int[1];
		glGenTextures(1, textures, 0);

		int tex = textures[0];
		glBindTexture(GL_TEXTURE_2D, tex);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		//glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

		InputStream is = c.getResources().openRawResource(
				resID);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore.
			}
		}

		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

		return tex;
	}
}
