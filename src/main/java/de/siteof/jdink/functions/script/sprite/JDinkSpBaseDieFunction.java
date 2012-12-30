package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_base_die(int spriteNumber, int baseDie)</p>
 * <p>Function: void sp_base_death(int spriteNumber, int baseDie)</p>
 */
public class JDinkSpBaseDieFunction extends AbstractJDinkSetSpriteIntValueFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getBaseDie();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setBaseDie(value);
	}

}
