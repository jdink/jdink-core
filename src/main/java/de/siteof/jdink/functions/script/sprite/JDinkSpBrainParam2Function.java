package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_brain_parm2(int spriteNumber, int value)</p>
 */
public class JDinkSpBrainParam2Function extends
		AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getBrainParameter(1);
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setBrainParameter(1, value);
	}

}
