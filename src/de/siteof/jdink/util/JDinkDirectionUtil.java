package de.siteof.jdink.util;

import de.siteof.jdink.model.JDinkDirectionIndexConstants;

public final class JDinkDirectionUtil {

	private JDinkDirectionUtil() {
		// prevent instantiation
	}

	public static int getInvertedDirection(int directionIndex) {
		switch (directionIndex) {
		case JDinkDirectionIndexConstants.DOWN_LEFT:
			directionIndex = JDinkDirectionIndexConstants.UP_RIGHT;
			break;
		case JDinkDirectionIndexConstants.DOWN_RIGHT:
			directionIndex = JDinkDirectionIndexConstants.UP_LEFT;
			break;
		case JDinkDirectionIndexConstants.UP_RIGHT:
			directionIndex = JDinkDirectionIndexConstants.DOWN_LEFT;
			break;
		case JDinkDirectionIndexConstants.UP_LEFT:
			directionIndex = JDinkDirectionIndexConstants.DOWN_RIGHT;
			break;
		case JDinkDirectionIndexConstants.LEFT:
			directionIndex = JDinkDirectionIndexConstants.RIGHT;
			break;
		case JDinkDirectionIndexConstants.RIGHT:
			directionIndex = JDinkDirectionIndexConstants.LEFT;
			break;
		case JDinkDirectionIndexConstants.UP:
			directionIndex = JDinkDirectionIndexConstants.DOWN;
			break;
		case JDinkDirectionIndexConstants.DOWN:
			directionIndex = JDinkDirectionIndexConstants.LEFT;
			break;
		}
		return directionIndex;
	}

}
