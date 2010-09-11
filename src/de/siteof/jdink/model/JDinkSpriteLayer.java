package de.siteof.jdink.model;

import de.siteof.jdink.geom.JDinkShape;


public class JDinkSpriteLayer {
	
	private final int layerNumber;
	private boolean opaque;
	private JDinkShape bounds;
	
	public JDinkSpriteLayer(int layerNumber) {
		this.layerNumber = layerNumber;
	}

	public boolean isOpaque() {
		return opaque;
	}

	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}

	public int getLayerNumber() {
		return layerNumber;
	}

	public JDinkShape getBounds() {
		return bounds;
	}

	public void setBounds(JDinkShape bounds) {
		this.bounds = bounds;
	}

}
