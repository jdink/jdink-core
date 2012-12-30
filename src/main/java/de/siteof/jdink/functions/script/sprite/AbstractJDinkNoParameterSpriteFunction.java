package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: ? sp_?(int spriteNumber)</p>
 */
public abstract class AbstractJDinkNoParameterSpriteFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(AbstractJDinkNoParameterSpriteFunction.class);
	
	protected abstract Object invoke(JDinkExecutionContext executionContext,
			JDinkSprite sprite);
	
	protected Object getInvalidSpriteReturnValue() {
		return Integer.valueOf(-1);
	}

	@Override
	public final Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(spriteNumber, "sp_?: spriteNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("sp_?: spriteNumber=" + spriteNumber);
		}
		Object result;
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
		if (sprite != null) {
			result = invoke(executionContext, sprite);
		} else {
			result = getInvalidSpriteReturnValue();
		}
		return result;
	}

}
