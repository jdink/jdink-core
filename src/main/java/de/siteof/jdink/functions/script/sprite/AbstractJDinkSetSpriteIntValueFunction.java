package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int sp_?(int spriteNumber, int value)</p>
 * <p>(By default, this is the behaviour of change_sprite, as opposed to change_sprite_noreturn)</p>
 */
public abstract class AbstractJDinkSetSpriteIntValueFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(AbstractJDinkSetSpriteIntValueFunction.class);

	protected abstract int getValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite);

	protected abstract void setValue(JDinkExecutionContext executionContext,
			JDinkSprite sprite, int value);

	protected boolean isValid(int value) {
		return true;
	}

	@Override
	public final Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer value = toInteger(executionContext.getArgument(1), null);
		assertNotNull(spriteNumber, "sp_?: spriteNumber missing");
		assertNotNull(value, "sp_?: value missing");
		if (log.isDebugEnabled()) {
			log.debug("sp_?: spriteNumber=" + spriteNumber + " value=" + value);
		}
		int result;
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
		if (sprite != null) {
			int intValue = value.intValue();
			if ((isValid(intValue)) && (intValue != getValue(executionContext, sprite))) {
				setValue(executionContext, sprite, intValue);
				executionContext.getContext().getController().setChanged(true);
			}
			result = getValue(executionContext, sprite);
		} else {
			result = -1;
		}
		return Integer.valueOf(result);
	}

}
