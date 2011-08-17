package jp.pekatuu.android.util;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import jp.pekatuu.android.zaqzaq.GameData;

public class GLLineText {
	private static final int[][] offsets; // offset, element count
	private static final FloatBuffer verticies;
	private static final float offset = 1.3f;

	public static void beforeDrawing(GL10 gl){
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticies);
	}

	public static void drawInt(GL10 gl, int num, float x, float y, float scale, boolean isCenter){
		int charCount = 0;
		float size = scale * GameData.height;
		gl.glPushMatrix();
		gl.glTranslatef(x * GameData.height, y * GameData.height, 0f);
		gl.glScalef(size, size, 0f);

		int n;
		int temp = num;
		
		// process minus
		if (num < 0){
			temp = -temp;
		}
		do {
			charCount++;
			n = temp % 10;
			gl.glDrawArrays(GL10.GL_LINES, offsets[n + 3][0], offsets[n + 3][1]);
			gl.glTranslatef(-offset, 0f, 0f);
			temp /= 10;
		} while ( temp > 0);
		if (num < 0){
			charCount++;
			gl.glDrawArrays(GL10.GL_LINES, offsets[0][0], offsets[0][1]);
		}
		if (isCenter){
			gl.glTranslatef(-offset * charCount / 2, 0f, 0f);
		}
		gl.glPopMatrix();
	}
	
	public static void drawString(GL10 gl, char[] str, float x, float y, float scale, boolean isCenter){
		float size = scale * GameData.height;
		gl.glPushMatrix();
		gl.glTranslatef(x * GameData.height, y * GameData.height, 0);
		gl.glScalef(size, size, 0f);
		if (isCenter){
			gl.glTranslatef(-offset * str.length / 2, 0f, 0f);
		}
		for (int i = 0; i < str.length; i++){
			if (str[i] != 32){
				gl.glDrawArrays(GL10.GL_LINES, offsets[str[i] - 45][0], offsets[str[i] - 45][1]);
			}
			gl.glTranslatef(offset, 0f, 0f);
		}
		gl.glPopMatrix();
	}
	
	public static void drawStringLeft(GL10 gl, char[] str, float x, float y, float scale){
		float size = scale * GameData.height;
		gl.glPushMatrix();
		gl.glTranslatef(x * GameData.height, y * GameData.height, 0);
		gl.glScalef(size, size, 0f);
		gl.glTranslatef(-offset * str.length, 0f, 0f);
		for (int i = 0; i < str.length; i++){
			if (str[i] != 32){
				gl.glDrawArrays(GL10.GL_LINES, offsets[str[i] - 45][0], offsets[str[i] - 45][1]);
			}
			gl.glTranslatef(offset, 0f, 0f);
		}
		gl.glPopMatrix();
	}
	
	static {
		final float[][] rawchars = {
			   //1       2       3       4       5       6
				{0,4,4,4},//-
				{0,4,4,4,2,2,2,6},//. for plus '+'
				{0,0,8,8},// /
				{0,0,0,8,0,8,8,8,8,8,8,0,8,0,0,0,0,0,8,8},//0
				{4,0,6,0,4,0,4,8,2,8,6,8},//1
				{0,0,8,0,0,0,0,4,0,4,8,4,8,4,8,8,8,8,0,8},//2
				{0,0,8,0,0,0,0,8,8,8,0,8,8,4,0,4},//3
				{0,0,0,8,0,4,8,4,8,4,8,0},//4
				{0,0,8,0,8,0,8,4,8,4,0,4,0,4,0,8,0,8,8,8},//5
				{0,0,8,0,8,0,8,8,8,4,0,4,0,4,0,8,0,8,8,8},//6
				{0,0,8,0,8,0,8,4,0,0,0,8,0,0,0,0,0,0,0,0},//7
				{0,0,8,0,8,0,8,8,8,8,0,8,0,0,0,8,0,4,8,4},//8
				{0,0,8,0,8,0,8,4,8,8,0,8,0,0,0,8,0,4,8,4},//9
				{0,0,0,0},//:
				{0,0,0,0},//;
				{0,0,0,0},//<
				{0,0,0,0},//=
				{0,0,0,0},//>
				{0,0,0,0},//?
				{0,0,0,0},//@
				{0,0,0,8,0,0,8,0,8,8,8,0,0,4,8,4},//a
				{8,0,8,8,8,8,0,8,0,8,0,4,0,4,4,0,4,0,8,0,0,4,8,4},
				{0,0,8,0,8,0,8,8,8,8,0,8},//c
				{4,0,8,0,8,0,8,8,8,8,0,8,0,8,0,4,0,4,4,0},//d
				{0,0,8,0,8,0,8,8,8,8,0,8,8,4,0,4},//e
				{0,0,8,0,8,0,8,8,8,4,0,4},//f
				{0,0,8,0,8,0,8,8,8,8,0,8,0,8,0,4,0,4,4,4},//g
				{0,0,0,8,0,4,8,4,8,8,8,0},//h
				{2,0,6,0,4,0,4,8,2,8,6,8},//i
				{0,0,0,8,0,8,8,8,8,8,8,4},//j
				{8,0,8,8,8,8,0,0,4,4,0,8},//k
				{8,0,8,8,8,8,0,8},//l
				{0,0,0,8,0,0,4,4,4,4,8,0,8,0,8,8},//m
				{0,0,0,8,0,8,8,0,8,8,8,0},//n
				{0,0,0,8,0,8,8,8,8,8,8,0,8,0,0,0},//o
				{0,0,0,4,0,0,8,0,8,8,8,0,0,4,8,4},//p
				{0,0,0,8,0,8,8,8,8,8,8,0,8,0,0,0,0,8,4,4},//q
				{0,0,0,4,0,0,8,0,8,8,8,0,0,4,8,4,4,4,0,8},//r
				{0,0,8,0,8,0,8,4,8,4,0,4,0,4,0,8,0,8,8,8},//s
				{0,0,8,0,4,0,4,8},//t
				{0,0,0,8,0,8,8,8,8,8,8,0},//u
				{0,0,0,4,0,4,4,8,4,8,8,4,8,4,8,0},//v
				{0,0,0,8,0,8,8,8,8,8,8,0,4,8,4,4},//w
				{0,0,8,8,8,0,0,8},//x
				{0,0,4,4,4,4,8,0,4,4,4,8},//y
				{0,0,8,0,0,0,8,8,8,8,0,8},//z
				{2,8,6,4,6,8,2,4},//small x 
		};
		
		int totalOffset = 0;
		final List<Float> list = new LinkedList<Float>();
		offsets = new int[rawchars.length][2];
		for (int i = 0; i < rawchars.length; i++){
			final float[] vertecies = rawchars[i];
			for (int j = 0; j < vertecies.length / 2; j++){
				list.add(-vertecies[2 * j] + 8);
				list.add(vertecies[2 * j + 1]);
			}
			offsets[i][0] = totalOffset;
			offsets[i][1] = rawchars[i].length / 2;
			totalOffset += rawchars[i].length / 2;
		}
		
		final float[] chars = new float[list.size()];
		for (int i = 0; i < list.size(); i++){
			chars[i] = list.get(i) / 8.0f;
		}
		verticies = BufferAllocator.allocateFloatBuffer(chars);
	};
	

	
}
