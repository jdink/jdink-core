package de.siteof.jdink.collision;

import de.siteof.jdink.model.JDinkSprite;

public class JDinkSpriteCollisionImpl implements JDinkSpriteCollision {

	private final JDinkSprite sprite;

	public JDinkSpriteCollisionImpl(JDinkSprite sprite) {
		this.sprite = sprite;
	}

	public JDinkSprite getSprite() {
		return sprite;
	}

}
