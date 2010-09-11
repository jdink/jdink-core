package de.siteof.jdink.interaction;

import java.util.Map;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

public class JDinkInteractionManager {
	
	private final Map<JDinkInteractionType, JDinkInteractionHandler> handlers;
	
	public JDinkInteractionManager(Map<JDinkInteractionType, JDinkInteractionHandler> handlers) {
		this.handlers = handlers;
	}
	
	public JDinkInteractionHandler getInteractionHandler(JDinkInteractionType interactionType) {
		return handlers.get(interactionType);
	}
	
	public boolean interact(JDinkInteractionType interactionType, JDinkContext context, JDinkSprite sprite) {
		boolean result;
		JDinkInteractionHandler handler = getInteractionHandler(interactionType);
		if (handler != null) {
			result = handler.interact(context, sprite);
		} else {
			result = false;
		}
		return result;
	}

}
