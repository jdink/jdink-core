package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_strength(int spriteNumber, int strength)</p>
 */
public class JDinkSpStrengthFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getStrength();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setStrength(value);
	}

}
