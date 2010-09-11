package de.siteof.jdink.graphics;

public interface JDinkColorMixer {

	void mixColors(int[] source, int sourceOffset, int color, int[] target, int targetOffset, int count);

	void mixColors(int[] source, int sourceOffset, int sourceDelta, int color,
			int[] target, int targetOffset, int targetDelta, int count);

}
