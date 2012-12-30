package de.siteof.jdink.events;

import de.siteof.jdink.model.JDinkContext;

public class JDinkKeyEventListenerList extends AbstractJDinkEventListenerList<JDinkKeyEventListener, JDinkKeyEvent> {

	@Override
	protected void callListener(JDinkKeyEventListener listener,
			JDinkContext context, JDinkKeyEvent event) {
		listener.onKeyEvent(context, event);
	}

}
