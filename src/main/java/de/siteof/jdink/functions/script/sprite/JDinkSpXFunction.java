package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Implementation of {@link JDinkFunction}.</p>
 *
 * <p>Signature: void sp_x(int spriteNumber, int x)</p>
 */
public class JDinkSpXFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getX();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setX(value);
	}

}
