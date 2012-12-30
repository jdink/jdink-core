package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void move_stop(int spriteNumber, int directionIndex, int destination, boolean noHard)</p>
 */
public class JDinkMoveStopFunction extends JDinkMoveFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkMoveStopFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer direction = toInteger(executionContext.getArgument(1), null);
		Integer destination = toInteger(executionContext.getArgument(2), null);
		Boolean nohard = toBoolean(executionContext.getArgument(3), null);
		assertNotNull(spriteNumber, "move stop: spriteNumber missing");
		assertNotNull(direction, "move stop: direction missing");
		assertNotNull(destination, "move stop: destination missing");
		assertNotNull(nohard, "move stop: nohard missing");
		log.info("move stop: spriteNumber=" + spriteNumber + " direction=" + direction + " destination=" + destination +
				 " nohard=" + nohard);
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);

		if (sprite != null) {
			this.moveSprite(executionContext, sprite, direction, destination, nohard, true);
		}

//		this.sleep(executionContext);

		//log.warn("move_stop not implemented");
		log.info("move_stop end" +
				(sprite != null ? " (sprite.x=" + sprite.getX() + ", sprite.y=" + sprite.getY() + ")" : ""));
		return null;
	}

}
