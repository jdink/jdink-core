package de.siteof.jdink.interaction;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

public interface JDinkInteractionHandler {
	
	boolean interact(JDinkContext context, JDinkSprite sprite);

}
