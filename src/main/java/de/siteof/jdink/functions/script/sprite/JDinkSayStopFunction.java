package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int say_stop(String text, int spriteNumber)</p>
 */
public class JDinkSayStopFunction extends AbstractJDinkTextFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSayStopFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String text = executionContext.getArgumentAsString(0, "");
		Integer spriteNumber = toInteger(executionContext.getArgument(1), null);
		assertNotNull(text, "say stop: text missing");
		assertNotNull(spriteNumber, "say stop: spriteNumber missing");
		log.info("say stop: spriteNumber=" + spriteNumber + " text=" + text);
		return processSay(executionContext, text, null, null, spriteNumber, true);
//		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
//		if (sprite != null) {
//			sprite.setText(text);
//			this.updateFrame(executionContext);
////			executionContext.getContext().getController().setChanged(true);
//			this.sleep(executionContext);
//			sprite.setText(null);
//		}
//		//log.warn("say_stop not implemented");
//		return spriteNumber;
	}

}
