package de.siteof.jdink.events;

import de.siteof.jdink.model.JDinkContext;

public class JDinkFrameEventListenerList extends AbstractJDinkEventListenerList<JDinkFrameEventListener, JDinkFrameEvent> {

	@Override
	protected void callListener(JDinkFrameEventListener listener,
			JDinkContext context, JDinkFrameEvent event) {
		switch(event.getId()) {
		case JDinkFrameEvent.BEGIN_FRAME:
			listener.onBeginFrame(context, event);
			break;
		case JDinkFrameEvent.END_FRAME:
			listener.onEndFrame(context, event);
			break;
		}
	}

}
