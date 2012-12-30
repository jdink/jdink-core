package de.siteof.jdink.geom;


/**
 * <p>
 * Similar to Rectangle but doesn't depend on AWT.
 * </p>
 */
public final class JDinkRectangle implements JDinkShape {

	public static final JDinkRectangle EMPTY = new JDinkRectangle(0, 0, 0, 0); 

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public JDinkRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static JDinkRectangle getInstance(int x, int y, int width, int height) {
		return EMPTY.getTransformedTo(x, y, width, height);
	}
	
	public static JDinkRectangle between(JDinkPoint p1, JDinkPoint p2) {
		return between(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public static JDinkRectangle between(int x1, int y1, int x2, int y2) {
		return new JDinkRectangle(
				Math.min(x1, x2),
				Math.min(y1, y2),
				Math.abs(x2 - x1) + 1,
				Math.abs(y2 - y1) + 1);
	}

	@Override
	public String toString() {
		return "JDinkRectangle [x=" + x + ", y=" + y + ", width=" + width
				+ ", height=" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
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
		JDinkRectangle other = (JDinkRectangle) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public boolean contains(int x, int y) {
		// copied from Rectangle#inside(int, int)
		int w = this.width;
		int h = this.height;
		if ((w | h) < 0) {
			// At least one of the dimensions is negative...
			return false;
		}
		// Note: if either dimension is zero, tests below must return false...
		int thisX = this.x;
		int thisY = this.y;
		if (x < thisX || y < thisY) {
			return false;
		}
		w += thisX;
		h += thisY;
		// overflow || intersect
		return ((w < thisX || w > x) && (h < thisY || h > y));
	}

	public boolean intersects(JDinkRectangle r) {
		int tw = this.width;
		int th = this.height;
		int rw = r.width;
		int rh = r.height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
		    return false;
		}
		int tx = this.x;
		int ty = this.y;
		int rx = r.x;
		int ry = r.y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		//      overflow || intersect
		return ((rw < rx || rw > tx) &&
			(rh < ry || rh > ty) &&
			(tw < tx || tw > rx) &&
			(th < ty || th > ry));
	}

	@Override
	public boolean intersects(JDinkShape shape) {
		boolean result;
		if (shape instanceof JDinkRectangle) {
			result = intersects((JDinkRectangle) shape);
		} else {
			// not handling intersection of other kind of shapes yet
			// use the bounds instead
			result = intersects(shape.getBounds());
		}
		return result;
	}

    /**
     * Computes the intersection of this <code>Rectangle</code> with the 
     * specified <code>Rectangle</code>. Returns a new <code>Rectangle</code> 
     * that represents the intersection of the two rectangles.
     * If the two rectangles do not intersect, the result will be
     * an empty rectangle.
     *
     * @param     r   the specified <code>Rectangle</code>
     * @return    the largest <code>Rectangle</code> contained in both the 
     *            specified <code>Rectangle</code> and in 
     *		  this <code>Rectangle</code>; or if the rectangles
     *            do not intersect, an empty rectangle.
     */
    public JDinkRectangle intersection(JDinkRectangle r) {
		int tx1 = this.x;
		int ty1 = this.y;
		int rx1 = r.x;
		int ry1 = r.y;
		long tx2 = tx1; tx2 += this.width;
		long ty2 = ty1; ty2 += this.height;
		long rx2 = rx1; rx2 += r.width;
		long ry2 = ry1; ry2 += r.height;
		if (tx1 < rx1) tx1 = rx1;
		if (ty1 < ry1) ty1 = ry1;
		if (tx2 > rx2) tx2 = rx2;
		if (ty2 > ry2) ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;
		// tx2,ty2 will never overflow (they will never be
		// larger than the smallest of the two source w,h)
		// they might underflow, though...
		if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
		if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
		return new JDinkRectangle(tx1, ty1, (int) tx2, (int) ty2);
    }

//	public void setLocation(int x, int y) {
//		setX(x);
//		setY(y);
//	}
 
	@Override
	public JDinkRectangle getTranslated(int dx, int dy) {
		return getLocatedTo(this.x + dx, this.y + dy);
	}

	@Override
	public JDinkRectangle getLocatedTo(int x, int y) {
		JDinkRectangle result;
		if ((this.x != x) || (this.y != y)) {
			result = new JDinkRectangle(x, y, this.width, this.height);
		} else {
			// the location is the same, we may return this
			result = this;
		}
		return result;
	}
	
	public JDinkRectangle getResizedTo(int width, int height) {
		JDinkRectangle result;
		if ((this.width != width) || (this.height != height)) {
			result = new JDinkRectangle(this.x, this.y, width, height);
		} else {
			// the size is the same, we may return this
			result = this;
		}
		return result;
	}
	
	public JDinkRectangle getTransformedTo(int x, int y, int width, int height) {
		JDinkRectangle result;
		if ((this.x != x) || (this.y != y) || (this.width != width) || (this.height != height)) {
			result = new JDinkRectangle(x, y, width, height);
		} else {
			// the location and size are the same, we may return this
			result = this;
		}
		return result;
	}

	@Override
	public JDinkRectangle getBounds() {
		return this;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

//	/**
//	 * @param x
//	 *            the x to set
//	 */
//	public void setX(int x) {
//		this.x = x;
//	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

//	/**
//	 * @param y
//	 *            the y to set
//	 */
//	public void setY(int y) {
//		this.y = y;
//	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

//	/**
//	 * @param width
//	 *            the width to set
//	 */
//	public void setWidth(int width) {
//		this.width = width;
//	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

//	/**
//	 * @param height
//	 *            the height to set
//	 */
//	public void setHeight(int height) {
//		this.height = height;
//	}

}
