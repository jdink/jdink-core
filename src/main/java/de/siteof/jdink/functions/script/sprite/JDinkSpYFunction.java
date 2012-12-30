package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Implementation of {@link JDinkFunction}.</p>
 *
 * <p>Signature: void sp_y(int spriteNumber, int y)</p>
 */
public class JDinkSpYFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getY();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setY(value);
	}

}
