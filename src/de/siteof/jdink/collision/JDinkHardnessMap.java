package de.siteof.jdink.collision;


public class JDinkHardnessMap {

	private final byte[][] hardness;
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public JDinkHardnessMap(int x, int y, int width, int height) {
		this.hardness = new byte[height][width];
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void copyHardnessFrom(
			byte[][] source, int sourceX, int sourceY,
			int destX, int destY,
			int width, int height) {
		int currentDestY = destY - this.y;
		for (int currentSourceY = 0; currentSourceY < height; currentSourceY++) {
			int currentDestX = destX - this.x;
			for (int currentSourceX = 0; currentSourceX < width; currentSourceX++) {
				this.hardness[currentDestY][currentDestX] = source[currentSourceY][currentSourceX];
				currentDestX++;
			}
			currentDestY++;
		}
	}

	public byte getHardnessAt(int x, int y, byte defaultHardness) {
		byte result;
		int sourceX = (x - this.x);
		int sourceY = (y - this.y);
		if ((sourceX >= 0) && (sourceX < this.width) && (sourceY >= 0) && (sourceY < this.height)) {
			result = this.hardness[sourceY][sourceX];
		} else {
			result = defaultHardness;
		}
		return result;
	}

	/**
	 * @return the hardness
	 */
	public byte[][] getHardness() {
		return hardness;
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
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}



}
