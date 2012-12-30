package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_frame(int spriteNumber, int frameNumber)</p>
 * <p>Sets the frame number of a sprite</p>
 */
public class JDinkSpFrameFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getAnimationFrameNumber();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setAnimationFrameNumber(value);
	}

}
