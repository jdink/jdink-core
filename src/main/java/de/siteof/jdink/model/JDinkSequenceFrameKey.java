package de.siteof.jdink.model;

/**
 * <p>Key to identify a frame</p>
 */
public class JDinkSequenceFrameKey {

	private final int sequenceNumber;
	private final int frameNumber;

	public JDinkSequenceFrameKey(int sequenceNumber, int frameNumber) {
		this.sequenceNumber = sequenceNumber;
		this.frameNumber = frameNumber;
	}

	public static JDinkSequenceFrameKey getInstance(int sequenceNumber, int frameNumber) {
		return new JDinkSequenceFrameKey(sequenceNumber, frameNumber);
	}

	@Override
	public String toString() {
		return "JDinkSequenceFrameKey [frameNumber=" + frameNumber
				+ ", sequenceNumber=" + sequenceNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frameNumber;
		result = prime * result + sequenceNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JDinkSequenceFrameKey other = (JDinkSequenceFrameKey) obj;
		if (frameNumber != other.frameNumber)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public int getFrameNumber() {
		return frameNumber;
	}

}
