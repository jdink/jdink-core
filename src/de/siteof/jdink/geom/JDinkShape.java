package de.siteof.jdink.geom;

public interface JDinkShape {

	boolean contains(int x, int y);
	
	boolean intersects(JDinkShape shape);

	JDinkRectangle getBounds();
	
	JDinkShape getTranslated(int dx, int dy);
	
	JDinkShape getLocatedTo(int x, int y);

}
