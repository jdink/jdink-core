package de.siteof.jdink.model;

/**
 * <p>
 * Persistent frame attributes
 * </p>
 */
public class JDinkSequenceFrameAttributes {

	private boolean special;
	private int delay;
	private int alternativeFrameNumber;

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public int getAlternativeFrameNumber() {
		return alternativeFrameNumber;
	}

	public void setAlternativeFrameNumber(int alternativeFrameNumber) {
		this.alternativeFrameNumber = alternativeFrameNumber;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
