package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int say(String text, int spriteNumber)</p>
 */
public class JDinkSayFunction extends AbstractJDinkTextFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSayFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String text = executionContext.getArgumentAsString(0, "");
		Integer spriteNumber = toInteger(executionContext.getArgument(1), null);
		assertNotNull(text, "say: text missing");
		assertNotNull(spriteNumber, "say: spriteNumber missing");
		log.info("say: spriteNumber=" + spriteNumber + " text=" + text);
		return processSay(executionContext, text, null, null, spriteNumber, false);
//		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
//		if (sprite != null) {
//			sprite.setText(text);
//			executionContext.getContext().getController().setChanged(true);
//		}
//		log.warn("say not implemented");
//		return spriteNumber;
	}

}
