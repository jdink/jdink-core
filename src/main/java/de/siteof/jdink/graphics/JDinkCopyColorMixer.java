package de.siteof.jdink.graphics;

public class JDinkCopyColorMixer implements JDinkColorMixer {


	@Override
	public void mixColors(int[] source, int sourceOffset, int color,
			int[] target, int targetOffset, int count) {
		mixColors(source, sourceOffset, 1, color, target, targetOffset, 1, count);
	}

	@Override
	public void mixColors(int[] source, int sourceOffset, int sourceDelta, int color,
			int[] target, int targetOffset, int targetDelta, int count) {
		int targetEndOffset = targetOffset + count;
		while (targetOffset < targetEndOffset) {
			target[targetOffset] = color;
			targetOffset += targetDelta;
		}
	}

}
