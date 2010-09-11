package de.siteof.jdink.events;

import java.util.EventListener;

import de.siteof.jdink.model.JDinkContext;

public interface JDinkFrameEventListener extends EventListener {

	void onBeginFrame(JDinkContext context, JDinkFrameEvent event);

	void onEndFrame(JDinkContext context, JDinkFrameEvent event);

}
