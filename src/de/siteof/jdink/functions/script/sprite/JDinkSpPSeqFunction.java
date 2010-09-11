package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void sp_pseq(int spriteNumber, int sequenceNumber)<p>
 * <p>Sets the sequence of a sprite</p>
 */
public class JDinkSpPSeqFunction extends AbstractJDinkSetSpriteIntValueNoMinusOneFunction {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return sprite.getSequenceNumber();
	}

	@Override
	protected void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value) {
		sprite.setSequenceNumber(value);
		sprite.setSequence(executionContext.getContext().getSequence(
				sprite.getSequenceNumber(), false));
	}

}
