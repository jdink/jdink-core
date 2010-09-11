package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int freeze(int spriteNumber)</p>
 */
public class JDinkFreezeFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkFreezeFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(spriteNumber, "freeze: spriteNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("freeze: spriteNumber=" + spriteNumber);
		}
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), false);
		if (sprite != null) {
			if (!sprite.isFrozen()) {
				sprite.setFrozen(true);
				executionContext.getContext().getController().setChanged(true);
			}
			// TODO confirm that we should return the value
			return Integer.valueOf(sprite.isFrozen() ? 1 : 0);
		}
		return null;
	}

}
