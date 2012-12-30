package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: sp_sound</p>
 * <p>Sets the sound of a sprite</p>
 */
public class JDinkSpSoundFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSpSoundFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext)
			throws Throwable {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer value = toInteger(executionContext.getArgument(1), null);
		assertNotNull(spriteNumber, "sp sound: spriteNumber missing");
		assertNotNull(value, "sp sound: value missing");
		if (log.isDebugEnabled()) {
			log.debug("sp sound: spriteNumber=" + spriteNumber + " value=" + value);
		}
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
		if (sprite != null) {
			log.info("sp_sound not implemented");
		}
		return null;
	}

}
