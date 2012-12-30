package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int say_stop_xy(String text, int x, int y)</p>
 */
public class JDinkSayStopXYFunction extends AbstractJDinkTextFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSayStopXYFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String text = executionContext.getArgumentAsString(0, "");
		Integer x = toInteger(executionContext.getArgument(1), null);
		Integer y = toInteger(executionContext.getArgument(2), null);
		assertNotNull(text, "say stop xy: text missing");
		assertNotNull(x, "say stop xy: x missing");
		assertNotNull(y, "say stop xy: y missing");
		log.info("say stop xy: x=" + x + " y=" + y + " text=" + text);
		return processSay(executionContext, text, x, y, null, true);
//		int spriteNumber = executionContext.getContext().getController().allocateSprite();
//		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber, false);
//		if (sprite != null) {
//			sprite.setX(x.intValue());
//			sprite.setY(y.intValue());
//			sprite.setText(text);
//			this.updateFrame(executionContext);
////			executionContext.getContext().getController().setChanged(true);
//			this.sleep(executionContext);
//			sprite.setText(null);
//		}
//		//log.warn("say_stop_xy not implemented");
//		return new Integer(spriteNumber);
	}

}
