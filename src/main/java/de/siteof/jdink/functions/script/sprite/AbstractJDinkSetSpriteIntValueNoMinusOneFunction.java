package de.siteof.jdink.functions.script.sprite;


/**
 * <p>Function: int sp_?(int spriteNumber, int value)</p>
 * <p>Convenience class, for all functions that don't consider "-1" to be a valid value</p>
 * <p>(This is the behaviour of change_sprite, as opposed to change_sprite_noreturn)</p>
 */
public abstract class AbstractJDinkSetSpriteIntValueNoMinusOneFunction extends AbstractJDinkSetSpriteIntValueFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isValid(int value) {
		return super.isValid(value) && (value != -1);
	}

}
