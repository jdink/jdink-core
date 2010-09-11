package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: sp(int editorSprite)</p>
 * <p>Returns the sprite with the index as found in the editor (sprite placement)</p>
 */
public class JDinkSpFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSpFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer editorSpriteNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(editorSpriteNumber, "sp: editorSpriteNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("sp: editorSpriteNumber=" + editorSpriteNumber);
		}
		JDinkSprite sprite = executionContext.getContext().getController().getSpriteByEditorSpriteNumber(editorSpriteNumber.intValue());
		int result;
		if (sprite != null) {
			result = sprite.getSpriteNumber();
		} else {
			log.warn("sprite not found with editor sprite number: " + editorSpriteNumber);
			result = 0;
		}
		return Integer.valueOf(result);
	}

}
