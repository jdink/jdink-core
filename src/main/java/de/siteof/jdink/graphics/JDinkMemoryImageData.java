package de.siteof.jdink.graphics;

public class JDinkMemoryImageData {

	private final int[] data;
	private final int offsetX;
	private final int offsetY;
	private final int width;
	private final int height;
	private JDinkColorMixer colorMixer = new JDinkCopyColorMixer();

	public JDinkMemoryImageData(int[] data, int offsetX, int offsetY, int width, int height) {
		this.data = data;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
	}

	public void drawRectangle(int x, int y, int width, int height,
			int borderWidth, int color) {
		int thisWidth = this.width;
		int thisHeight = this.height;
		JDinkColorMixer colorMixer = this.colorMixer;
		x -= this.offsetX;
		y -= this.offsetY;
		// calculate intersection with image data
		if (x < 0) {
			width += x;
			x = 0;
		}
		if (y < 0) {
			height += y;
			y = 0;
		}
		if (x + width > thisWidth) {
			width = thisWidth - x;
		}
		if (y + height > thisHeight) {
			height = thisHeight - y;
		}
		if ((width > 0) && (height > 0)) {
			if ((width <= 2 * borderWidth) || (height <= 2 * borderWidth)) {
				fillRectangle(x, y, borderWidth, height, color);
			} else {
				// fill intersection
				int offset = (y * thisWidth) + x;
				final int[] data = this.data;
				for (int currentY = 0; currentY < borderWidth; currentY++) {
					colorMixer.mixColors(data, offset, color, data, offset, width);
					offset += thisWidth;
				}
				int verticalBorderHeight = height - (2 * borderWidth);
				for (int currentX = 0; currentX < borderWidth; currentX++) {
					int tempOffset = offset + currentX;
					colorMixer.mixColors(data, tempOffset, thisWidth, color,
							data, tempOffset, thisWidth, verticalBorderHeight);
					tempOffset = offset +  width - 1 - currentX;
					colorMixer.mixColors(data, tempOffset, thisWidth, color,
							data, tempOffset, thisWidth, verticalBorderHeight);
				}
				offset += verticalBorderHeight * thisWidth;
				for (int currentY = 0; currentY < borderWidth; currentY++) {
					colorMixer.mixColors(data, offset, color, data, offset, width);
					offset += thisWidth;
				}
			}
		}
	}

	public void fillRectangle(int x, int y, int width, int height, int color) {
		int thisWidth = this.width;
		int thisHeight = this.height;
		JDinkColorMixer colorMixer = this.colorMixer;
		x -= this.offsetX;
		y -= this.offsetY;
		// calculate intersection with image data
		if (x < 0) {
			width += x;
			x = 0;
		}
		if (y < 0) {
			height += y;
			y = 0;
		}
		if (x + width > thisWidth) {
			width = thisWidth - x;
		}
		if (y + height > thisHeight) {
			height = thisHeight - y;
		}
		if ((width > 0) && (height > 0)) {
			// fill intersection
			int offset = (y * thisWidth) + x;
			final int[] data = this.data;
			for (int currentY = 0; currentY < height; currentY++) {
				colorMixer.mixColors(data, offset, color, data, offset, width);
				offset += thisWidth;
			}
		}
	}


	/**
	 * @return the data
	 */
	public int[] getData() {
		return data;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the colorMixer
	 */
	public JDinkColorMixer getColorMixer() {
		return colorMixer;
	}

	/**
	 * @param colorMixer the colorMixer to set
	 */
	public void setColorMixer(JDinkColorMixer colorMixer) {
		this.colorMixer = colorMixer;
	}

}
