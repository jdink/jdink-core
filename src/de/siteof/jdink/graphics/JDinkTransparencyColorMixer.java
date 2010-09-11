package de.siteof.jdink.graphics;

public class JDinkTransparencyColorMixer implements JDinkColorMixer {

	@Override
	public void mixColors(int[] source, int sourceOffset, int color,
			int[] target, int targetOffset, int count) {
		mixColors(source, sourceOffset, 1, color, target, targetOffset, 1, count);
	}

	@Override
	public void mixColors(int[] source, int sourceOffset, int sourceDelta, int color,
			int[] target, int targetOffset, int targetDelta, int count) {
		int targetEndOffset = targetOffset + (count * targetDelta);
		int colorTransparency = (color >> 24) & 0xFF;
		if (colorTransparency == 0) {
			// nothing to copy
		} else if (colorTransparency == 255) {
			// copy
			while (targetOffset < targetEndOffset) {
				target[targetOffset] = color;
				targetOffset += targetDelta;
			}
		} else {
			while (targetOffset < targetEndOffset) {
				int sourceColor1 = source[sourceOffset];
				sourceOffset += sourceDelta;
				int sourceTransparency1 = (sourceColor1 >> 24) & 0xFF;
				if (sourceTransparency1 == 0) {
					target[targetOffset++] = color;
				} else {
					int targetTransparency = sourceTransparency1;
					if (colorTransparency > targetTransparency) {
						targetTransparency = colorTransparency;
					}
					int m1 = sourceTransparency1;
					int m2 = colorTransparency;
					int d = sourceTransparency1 + colorTransparency;
					target[targetOffset] =
						(targetTransparency << 24) +
						(Math.min(255, ((((sourceColor1 >> 16) & 0xFF) * m1) +
								(((color >> 16) & 0xFF) * m2)) / d) << 16) +
						(Math.min(255, ((((sourceColor1 >> 8) & 0xFF) * m1) +
								(((color >> 8) & 0xFF) * m2)) / d) << 8) +
						(Math.min(255, (((sourceColor1 & 0xFF) * m1) +
								((color & 0xFF) * m2)) / d));
					targetOffset += targetDelta;
				}
			}
		}
	}

}
