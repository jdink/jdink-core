package de.siteof.jdink.events;

import java.util.EventListener;

import de.siteof.jdink.model.JDinkContext;

public interface JDinkKeyEventListener extends EventListener {

	void onKeyEvent(JDinkContext context, JDinkKeyEvent event);

}
