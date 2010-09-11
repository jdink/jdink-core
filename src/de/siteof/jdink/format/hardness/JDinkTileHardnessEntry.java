package de.siteof.jdink.format.hardness;

public class JDinkTileHardnessEntry {

	private byte[][] hardness;
	private boolean used;
	private int hold;

	/**
	 * @return the hardness
	 */
	public byte[][] getHardness() {
		return hardness;
	}
	/**
	 * @param hardness the hardness to set
	 */
	public void setHardness(byte[][] hardness) {
		this.hardness = hardness;
	}
	/**
	 * @return the used
	 */
	public boolean isUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}
	/**
	 * @return the hold
	 */
	public int getHold() {
		return hold;
	}
	/**
	 * @param hold the hold to set
	 */
	public void setHold(int hold) {
		this.hold = hold;
	}

}
