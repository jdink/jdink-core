package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.functions.script.behaviour.JDinkMoveSpriteBehaviour;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: void move(int spriteNumber, int directionIndex, int destination, boolean noHard)</p>
 */
public class JDinkMoveFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkMoveFunction.class);


	protected void moveSprite(
			JDinkExecutionContext executionContext,
			JDinkSprite sprite,
			Integer direction,
			Integer destination,
			Boolean nohard,
			boolean stop) throws Exception {
		if (sprite != null) {
			//sprite.setX(value.intValue());
			//executionContext.getContext().getController().setChanged(true);
			int directionX = 0;
			int directionY = 0;
			Integer destinationX = null;
			Integer destinationY = null;
			switch (direction.intValue()) {
			case JDinkDirectionIndexConstants.DOWN_LEFT:
				directionX = -1;
				directionY = 1;
				break;
			case JDinkDirectionIndexConstants.DOWN:
				directionX = 0;
				directionY = 1;
				break;
			case JDinkDirectionIndexConstants.DOWN_RIGHT:
				directionX = 1;
				directionY = 1;
				break;
			case JDinkDirectionIndexConstants.LEFT:
				directionX = -1;
				directionY = 0;
				break;
			case JDinkDirectionIndexConstants.RIGHT:
				directionX = 1;
				directionY = 0;
				break;
			case JDinkDirectionIndexConstants.UP_LEFT:
				directionX = -1;
				directionY = -1;
				break;
			case JDinkDirectionIndexConstants.UP:
				directionX = 0;
				directionY = -1;
				break;
			case JDinkDirectionIndexConstants.UP_RIGHT:
				directionX = 1;
				directionY = -1;
				break;
			}
			JDinkFunction behaviourFunction = null;
			if ((directionX != 0) || (directionY != 0)) {
				if (directionX == 0) {
					destinationY = destination;
				} else {
					destinationX = destination;
				}
				boolean alreadyReachedPosition = false;
				if ((true) && (!sprite.containsBehaviour(JDinkMoveSpriteBehaviour.class))) {
					if ((destinationX != null) &&
							(((directionX > 0) && (sprite.getX() >= destinationX.intValue())) ||
									((directionX < 0) && (sprite.getX() <= destinationX.intValue())))) {
						alreadyReachedPosition = true;
					}
					if ((destinationY != null) &&
							(((directionY > 0) && (sprite.getY() >= destinationY.intValue())) ||
									((directionY < 0) && (sprite.getY() <= destinationY.intValue())))) {
						alreadyReachedPosition = true;
					}
				}
				if (!alreadyReachedPosition) {
					int speed = Math.max(1, sprite.getSpeed()) * 5;
					behaviourFunction = new JDinkMoveSpriteBehaviour(
							directionX * speed, directionY * speed, destinationX, destinationY,
							true, direction.intValue());
				}
			}
			if (behaviourFunction != null) {
				sprite.addBehaviour(behaviourFunction);
				if (stop) {
					JDinkController controller = executionContext.getContext().getController();
					long delay = controller.getDelay(50);
					while (sprite.containsBehaviour(behaviourFunction)) {
						controller.sleep(delay);
					}
				}
			}
			/*
			while (behaviourFunction != null) {
				long delay = executionContext.getContext().getController().getDelay(50);
				try {
					JDinkExecutionContext childExecutionContext = new JDinkExecutionContext();
					childExecutionContext.setContext(executionContext.getContext());
					childExecutionContext.setArguments(new Object[] { sprite });
					Boolean behaviourResult = (Boolean) behaviourFunction.invoke(childExecutionContext);
					if (!behaviourResult.booleanValue()) {
						behaviourFunction = null;
					}
//					log.info("move_stop, sprite.x=" + sprite.getX() + ", sprite.y=" + sprite.getY() +
//							", destinationX=" + destinationX + ", destinationY=" + destinationY);
					executionContext.getContext().getView().updateView();
					this.sleep(executionContext, delay);
				} catch (Throwable e) {
					log.error("failed to execute behaviour", e);
					behaviourFunction = null;
				}
			}
			*/
		}
	}

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer direction = toInteger(executionContext.getArgument(1), null);
		Integer destination = toInteger(executionContext.getArgument(2), null);
		Boolean nohard = toBoolean(executionContext.getArgument(3), null);
		assertNotNull(spriteNumber, "move: spriteNumber missing");
		assertNotNull(direction, "move: direction missing");
		assertNotNull(destination, "move: destination missing");
		assertNotNull(nohard, "move: nohard missing");
		if (log.isDebugEnabled()) {
			log.debug("move: spriteNumber=" + spriteNumber + " direction=" + direction + " destination=" + destination +
					 " nohard=" + nohard);
		}
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
		if (sprite != null) {
			this.moveSprite(executionContext, sprite, direction, destination, nohard, false);
			//sprite.setX(value.intValue());
			//executionContext.getContext().getController().setChanged(true);
		}
//		log.warn("move not implemented");
		log.info("move end");
		return VOID;
	}

}
