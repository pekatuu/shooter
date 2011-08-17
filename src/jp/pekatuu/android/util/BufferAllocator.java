package jp.pekatuu.android.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BufferAllocator {
	public static FloatBuffer allocateFloatBuffer(float[] floatArray){
//		Log.d("allocator", "allocated " + floatArray.length * 4 + " bytes");
		FloatBuffer result;
		ByteBuffer tempBuffer = ByteBuffer.allocateDirect(floatArray.length * 4);
		tempBuffer.order(ByteOrder.nativeOrder());
		result = tempBuffer.asFloatBuffer();
		result.put(floatArray);
		result.position(0);
		return result;
	}
}
