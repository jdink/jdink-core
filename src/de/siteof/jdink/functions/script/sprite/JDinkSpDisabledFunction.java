package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_disabled(int spriteNumber, int disabled)</p>
 * <p>An item that is disabled will not take part of the game. It is however still allocated.</p>
 */
public class JDinkSpDisabledFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return (sprite.isDisabled() ? 1 : 0);
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setDisabled(value != 0);
	}

}
