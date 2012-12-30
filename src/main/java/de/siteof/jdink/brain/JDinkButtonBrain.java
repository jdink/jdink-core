package de.siteof.jdink.brain;


import java.util.EventObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.events.JDinkMouseEvent;
import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkBooleanType;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.script.JDinkVariable;

/**
 * <p>Handles buttons. i.e. the state and events</p>
 */
public class JDinkButtonBrain extends AbstractJDinkBrain {

	private final static String HOVERSTATE_VARNAME = "brain.button.hoverState";

	private static final Log log = LogFactory.getLog(JDinkButtonBrain.class);


	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		JDinkPoint mousePosition = context.getController().getMousePosition();
		if (mousePosition == null) {
			return;
		}
		boolean newHoverState = false;
		JDinkSequence sequence = sprite.getSequence();
		JDinkSequenceFrame frame = (sequence != null ? sequence.getFrame(sprite.getFrameNumber(), false) : null);
		JDinkScope spriteScope = requestSpriteScope(context, sprite);
		JDinkVariable hoverStateVariable = spriteScope.getInternalVariable(HOVERSTATE_VARNAME);
		if (hoverStateVariable == null) {
			hoverStateVariable = new JDinkVariable();
			hoverStateVariable.setType(JDinkBooleanType.getBooleanTypeInstance());
			spriteScope.addInternalVariable(HOVERSTATE_VARNAME, hoverStateVariable);
		}
		boolean oldHoverState = toBoolean(hoverStateVariable.getValue(), Boolean.FALSE).booleanValue();
		if (frame != null) {
			if (frame.isInside(mousePosition.getX() - sprite.getX(), mousePosition.getY() - sprite.getY())) {
				newHoverState = true;
			}
		}
		if (oldHoverState != newHoverState) {
			if (log.isDebugEnabled()) {
				log.debug("newHoverState=" + newHoverState);
			}
			hoverStateVariable.setValue(Boolean.valueOf(newHoverState));
			//context.getController().setHoverSprite(newHoverSprite);
			if (oldHoverState) {
				if (sprite.getScriptInstance() != null) {
					try {
						sprite.getScriptInstance().callFunction(context, "buttonoff", null);
					} catch (Throwable e) {
						log.error("buttonoff failed - " + e, e);
					}
				}
			}
			if (newHoverState) {
				if (sprite.getScriptInstance() != null) {
					try {
						sprite.getScriptInstance().callFunction(context, "buttonon", null);
					} catch (Throwable e) {
						log.error("buttonon failed - " + e);
					}
				}
			}
		}
		boolean clicked = false;
		EventObject[] events = context.getController().getEvents();
		for (int i = 0; i < events.length; i++) {
			if (events[i] instanceof JDinkMouseEvent) {
				JDinkMouseEvent mouseEvent = (JDinkMouseEvent) events[i];
				if (mouseEvent.getId() == JDinkMouseEvent.MOUSE_CLICKED) {
					clicked = true;
				}
			}
		}
		if ((newHoverState) && (clicked)) {
			JDinkScriptInstance scriptInstance = sprite.getScriptInstance();
			if (scriptInstance != null) {
				try {
					log.info("[update] calling click function, script=[" +
							scriptInstance.getScriptFile() + "]");
					scriptInstance.callFunction(context, "click", null);
				} catch (Throwable e) {
					log.error("click failed - " + e, e);
				}
			} else {
				log.info("[update] no script attached");
			}
		}
	}

}
