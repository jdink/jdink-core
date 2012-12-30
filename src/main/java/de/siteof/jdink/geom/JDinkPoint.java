package de.siteof.jdink.geom;

public final class JDinkPoint {
	
	public static final JDinkPoint EMPTY = new JDinkPoint(0, 0); 

	private final int x;
	private final int y;
	
	public JDinkPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static JDinkPoint getInstance(int x, int y) {
		return EMPTY.getLocatedTo(x, y);
	}

	@Override
	public String toString() {
		return "JDinkPoint [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		JDinkPoint other = (JDinkPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public JDinkPoint getLocatedTo(int x, int y) {
		JDinkPoint result;
		if ((this.x != x) || (this.y != y)) {
			result = new JDinkPoint(x, y);
		} else {
			// the location is the same, we may return this
			result = this;
		}
		return result;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
