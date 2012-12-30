package de.siteof.jdink.model;

public final class JDinkDirection {

	private final int directionIndex;

	public JDinkDirection(int directionIndex) {
		this.directionIndex = directionIndex;
	}

	public static final boolean isValid(int directionIndex) {
		return (directionIndex >= 1) && (directionIndex <= 9) && (directionIndex != 5);
	}

	public static final boolean isDiagonal(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.UP_LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.UP_RIGHT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN_LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN_RIGHT);
	}

	public static final boolean isStraight(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.UP) ||
				(directionIndex == JDinkDirectionIndexConstants.LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.RIGHT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN);
	}

	public static final boolean isUp(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.UP_LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.UP) ||
				(directionIndex == JDinkDirectionIndexConstants.UP_RIGHT);
	}

	public static final boolean isDown(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.DOWN_LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN_RIGHT);
	}

	public static final boolean isLeft(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.UP_LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.LEFT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN_LEFT);
	}

	public static final boolean isRight(int directionIndex) {
		return (directionIndex == JDinkDirectionIndexConstants.UP_RIGHT) ||
				(directionIndex == JDinkDirectionIndexConstants.RIGHT) ||
				(directionIndex == JDinkDirectionIndexConstants.DOWN_RIGHT);
	}

	@Override
	public String toString() {
		return "JDinkDirection [directionIndex=" + directionIndex + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + directionIndex;
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
		JDinkDirection other = (JDinkDirection) obj;
		if (directionIndex != other.directionIndex)
			return false;
		return true;
	}

	public final boolean isValid() {
		return isValid(directionIndex);
	}

	public final boolean isDiagonal() {
		return isDiagonal(directionIndex);
	}

	public final boolean isStraight() {
		return isStraight(directionIndex);
	}

	public int getDirectionIndex() {
		return directionIndex;
	}

}
