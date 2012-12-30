package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_kill_wait(int spriteNumber)</p>
 * <p>Clears the wait period for the next animation frame.</p>
 */
public class JDinkSpKillWaitFunction extends AbstractJDinkNoParameterSpriteFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected Object invoke(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		sprite.setNextAnimationTime(0);
		return Integer.valueOf(0);
	}

}
