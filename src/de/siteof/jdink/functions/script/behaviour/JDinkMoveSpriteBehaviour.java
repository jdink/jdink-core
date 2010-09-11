package de.siteof.jdink.functions.script.behaviour;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkSprite;

public class JDinkMoveSpriteBehaviour implements JDinkFunction {

	private static final long serialVersionUID = 1L;

	private final int directionX;
	private final int directionY;
	private final Integer destinationX;
	private final Integer destinationY;
	private final boolean updateDirectionIndex;
	private final int directionIndex;

	public JDinkMoveSpriteBehaviour(
			int directionX,
			int directionY,
			Integer destinationX,
			Integer destinationY,
			boolean updateDirectionIndex,
			int directionIndex) {
		this.directionX = directionX;
		this.directionY = directionY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.updateDirectionIndex = updateDirectionIndex;
		this.directionIndex = directionIndex;
	}

	public Object invoke(JDinkExecutionContext executionContext)
			throws Throwable {
		Boolean result = Boolean.TRUE;
		JDinkSprite sprite = (JDinkSprite) executionContext.getArgument(0);
		int x = sprite.getX();
		int y = sprite.getY();
		x += directionX;
		y += directionY;
		if (destinationX != null) {
			if ((directionX > 0) && (x >= destinationX.intValue())) {
				x = destinationX.intValue();
				result = Boolean.FALSE;
			}
			if ((directionX < 0) && (x <= destinationX.intValue())) {
				x = destinationX.intValue();
				result = Boolean.FALSE;
			}
		}
		if (destinationY != null) {
			if ((directionY > 0) && (y >= destinationY.intValue())) {
				y = destinationY.intValue();
				result = Boolean.FALSE;
			}
			if ((directionY < 0) && (y <= destinationY.intValue())) {
				y = destinationY.intValue();
				result = Boolean.FALSE;
			}
		}
		if (result.booleanValue()) {
			// None standard behaviour: automatically change the direction index and animation sequence
			if ((sprite.getAnimationSequenceNumber() == 0) && (sprite.getBaseWalk() > 0)) {
				int directionIndex = sprite.getDirectionIndex();
				if ((updateDirectionIndex) || (directionIndex == 0)) {
					directionIndex = this.directionIndex;
					if (directionIndex == 0) {
						directionIndex = this.getDirectionIndex(x - sprite.getX(), y - sprite.getY());
					}
				}
				if (directionIndex != 0) {
					int animationSequenceNumber = sprite.getBaseWalk() + directionIndex;
					if (executionContext.getContext().getSequence(animationSequenceNumber, false) != null) {
						sprite.setAnimationSequenceNumber(sprite.getBaseWalk() + directionIndex);
					}
				}
			}
		}
		sprite.setX(x);
		sprite.setY(y);
		executionContext.getContext().getController().setChanged(true);
		return result;
	}

	private int getDirectionIndex(int deltaX, int deltaY) {
		int directionIndex;
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);
		boolean diagonal = ((deltaX != 0) && (deltaY != 0));
		if ((deltaX == 0) && (deltaY == 0)) {
			directionIndex = 0;
		} else if (diagonal) {
			if (deltaY > 0) {
				if (deltaX > 0) {
					directionIndex = JDinkDirectionIndexConstants.DOWN_RIGHT;
				} else {
					directionIndex = JDinkDirectionIndexConstants.DOWN_LEFT;
				}
			} else {
				if (deltaX > 0) {
					directionIndex = JDinkDirectionIndexConstants.UP_RIGHT;
				} else {
					directionIndex = JDinkDirectionIndexConstants.UP_LEFT;
				}
			}
		} else if (absDeltaX > absDeltaY) {
			if (deltaX > 0) {
				directionIndex = JDinkDirectionIndexConstants.RIGHT;
			} else {
				directionIndex = JDinkDirectionIndexConstants.LEFT;
			}
		} else {
			if (deltaY > 0) {
				directionIndex = JDinkDirectionIndexConstants.DOWN;
			} else {
				directionIndex = JDinkDirectionIndexConstants.UP;
			}
		}
		return directionIndex;
	}

}
