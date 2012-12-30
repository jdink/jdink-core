package de.siteof.jdink.model;

import java.io.Serializable;

/**
 * <p>
 * Persistent frame attributes
 * </p>
 */
public class JDinkSequenceFrameAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean special;
	private int delay;
	private int sourceSequenceNumber;
	private int sourceFrameNumber;

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getSourceSequenceNumber() {
		return sourceSequenceNumber;
	}

	public void setSourceSequenceNumber(int sourceSequenceNumber) {
		this.sourceSequenceNumber = sourceSequenceNumber;
	}

	public int getSourceFrameNumber() {
		return sourceFrameNumber;
	}

	public void setSourceFrameNumber(int sourceFrameNumber) {
		this.sourceFrameNumber = sourceFrameNumber;
	}

}
