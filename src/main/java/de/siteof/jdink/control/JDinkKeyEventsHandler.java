package de.siteof.jdink.control;

import java.util.EventObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.events.JDinkFrameEvent;
import de.siteof.jdink.events.JDinkFrameEventListener;
import de.siteof.jdink.events.JDinkKeyConstants;
import de.siteof.jdink.events.JDinkKeyEvent;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.script.util.JDinkScriptUtil;

public class JDinkKeyEventsHandler implements JDinkFrameEventListener {

	private static final Log log = LogFactory.getLog(JDinkKeyEventsHandler.class);


	@Override
	public void onBeginFrame(JDinkContext context, JDinkFrameEvent event) {
	}

	@Override
	public void onEndFrame(JDinkContext context, JDinkFrameEvent event) {
		EventObject[] events = context.getController().getEvents();
		for (EventObject eventObject: events) {
			if (eventObject instanceof JDinkKeyEvent) {
				JDinkKeyEvent keyEvent = (JDinkKeyEvent) eventObject;
				String scriptName = null;
				if (keyEvent.isKeyReleased()) {
					switch (keyEvent.getKeyCode()) {
					case JDinkKeyConstants.VK_ESCAPE:
						scriptName = "ESCAPE";
						break;
					}
				}
				if (scriptName != null) {
					try {
						JDinkScriptUtil.callStatelessScript(context, scriptName);
					} catch (Throwable e) {
						log.error("[onEndFrame] failed to execute script, scriptName=[" + scriptName + "]", e);
					}
				}
			}
		}
	}

}
