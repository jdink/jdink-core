package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int sp_target(int spriteNumber, int target)</p>
 */
public class JDinkSpTargetFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getTargetSpriteNumber();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setTargetSpriteNumber(value);
	}

}
