package de.siteof.jdink.brain;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkObjectType;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkType;
import de.siteof.jdink.script.JDinkVariable;

/**
 * <p>Brain Number: 8</p>
 * <p>Handles text.</p>
 */
public class JDinkTextBrain extends AbstractJDinkBrain {

	private final static String EXPIRE_DATE_VAR_NAME = "brain.text.expireDate";

	private final static JDinkType DATE_TYPE = JDinkObjectType.getObjectTypeInstance(Date.class);

	private static final Log log = LogFactory.getLog(JDinkTextBrain.class);

	public static boolean stopAll(JDinkContext context) {
		boolean result = false;
		for (JDinkSprite sprite: context.getController().getActiveSprites()) {
			if (sprite.getBrain() instanceof JDinkTextBrain) {
				if (((JDinkTextBrain) sprite.getBrain()).stop(context, sprite)) {
					result = true;
				}
			}
		}
		return result;
	}

	public boolean stop(JDinkContext context, JDinkSprite sprite) {
		boolean result;
		JDinkScope spriteScope = requestSpriteScope(context, sprite);
		JDinkVariable expiryDateVariable = spriteScope.getInternalVariable(EXPIRE_DATE_VAR_NAME);
		if (expiryDateVariable != null) {
			this.internalStop(context, sprite, spriteScope, expiryDateVariable);
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		log.debug("JDinkTextBrain.update");
		automove(context, sprite, false);
		JDinkScope spriteScope = requestSpriteScope(context, sprite);
		JDinkVariable expiryDateVariable = spriteScope.getInternalVariable(EXPIRE_DATE_VAR_NAME);
		String text = sprite.getText();
		if ((expiryDateVariable == null) && (text != null)) {
			long delay = 77 * Math.max(35, text.length());
//			log.info("delay=" + delay + ", text=[" + text + "]");
			delay = context.getController().getDelay(delay);
			Date expiryDate = new Date(context.getTime() + delay);
			expiryDateVariable = new JDinkVariable();
			expiryDateVariable.setType(DATE_TYPE);
			expiryDateVariable.setValue(expiryDate);
			spriteScope.addInternalVariable(EXPIRE_DATE_VAR_NAME, expiryDateVariable);
		} else if (expiryDateVariable != null) {
			Date expiryDate = (Date) expiryDateVariable.getValue();
			if ((expiryDate != null) && (expiryDate.before(new Date()))) {
				this.internalStop(context, sprite, spriteScope, expiryDateVariable);
			}
		}
		// TODO follow original sprite
	}

	private void internalStop(JDinkContext context, JDinkSprite sprite,
			JDinkScope spriteScope, JDinkVariable expiryDateVariable) {
		spriteScope.removeInternalVariable(EXPIRE_DATE_VAR_NAME, expiryDateVariable);
		context.getController().releaseSprite(sprite.getSpriteNumber());
		sprite.setText(null);
	}

}
