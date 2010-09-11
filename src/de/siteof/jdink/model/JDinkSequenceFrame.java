package de.siteof.jdink.model;

import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.view.JDinkImage;

/**
 * <p>A single frame of a sequence</p>
 */
public class JDinkSequenceFrame {

	private String fileName;
	private JDinkImage image;
//	private int delay;
	private JDinkRectangle bounds;
	private JDinkShape collisionShape;
	private JDinkSequenceFrameAttributes attributes;

	public boolean isInside(int x, int y) {
		if (bounds != null) {
			return bounds.contains(x, y);
		} else {
			return false;
		}
	}

//	public int getDelay() {
//		return delay;
//	}
//
//	public void setDelay(int delay) {
//		this.delay = delay;
//	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JDinkImage getImage() {
		return image;
	}

	public void setImage(JDinkImage image) {
		this.image = image;
	}

	public JDinkRectangle getBounds() {
		return bounds;
	}

	public void setBounds(JDinkRectangle bounds) {
		this.bounds = bounds;
	}

	public JDinkShape getCollisionShape() {
		return collisionShape;
	}

	public void setCollisionShape(JDinkShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public JDinkSequenceFrameAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(JDinkSequenceFrameAttributes attributes) {
		this.attributes = attributes;
	}
}
