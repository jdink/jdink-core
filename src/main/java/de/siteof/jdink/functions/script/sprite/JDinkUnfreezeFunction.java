package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int unfreeze(int spriteNumber)</p>
 */
public class JDinkUnfreezeFunction extends AbstractJDinkNoParameterSpriteFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected Object invoke(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		if (sprite.isFrozen()) {
			sprite.setFrozen(false);
			executionContext.getContext().getController().setChanged(true);
		}
		// TODO confirm that we should return the value
		return Integer.valueOf(sprite.isFrozen() ? 1 : 0);
	}

}
