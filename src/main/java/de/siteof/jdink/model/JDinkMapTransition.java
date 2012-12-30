package de.siteof.jdink.model;

public class JDinkMapTransition {

	private final int fromMapNumber;
	private final int toMapNumber;
	private final int deltaX;
	private final int deltaY;
	
	public JDinkMapTransition(int fromMapNumber, int toMapNumber, int deltaX, int deltaY) {
		this.fromMapNumber = fromMapNumber;
		this.toMapNumber = toMapNumber;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	public boolean isImmediateLeft() {
		return (deltaX == -1) && (deltaY == 0);
	}
	
	public boolean isImmediateTop() {
		return (deltaX == 0) && (deltaY == -1);
	}
	
	public boolean isImmediateRight() {
		return (deltaX == 1) && (deltaY == 0);
	}
	
	public boolean isImmediateBottom() {
		return (deltaX == 0) && (deltaY == 1);
	}

	public int getFromMapNumber() {
		return fromMapNumber;
	}

	public int getToMapNumber() {
		return toMapNumber;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}
}
