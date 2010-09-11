package de.siteof.jdink.model.view;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkSpriteLayer;

public class JDinkSpriteLayerView {
	
	private final int layerNumber;
	private final boolean opaque;
	private final JDinkShape bounds;

	public JDinkSpriteLayerView(JDinkSpriteLayer spriteLayer) {
		this.layerNumber = spriteLayer.getLayerNumber();
		this.opaque = spriteLayer.isOpaque();
		this.bounds = spriteLayer.getBounds();
	}

	public int getLayerNumber() {
		return layerNumber;
	}

	public boolean isOpaque() {
		return opaque;
	}

	public JDinkShape getBounds() {
		return bounds;
	}

}
