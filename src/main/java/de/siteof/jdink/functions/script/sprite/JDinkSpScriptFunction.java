package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;

/**
 * <p>Function: void sp_sprite(int spriteNumber, String script)</p>
 * <p>Sets the script of sprite</p>
 */
public class JDinkSpScriptFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	public final static String CURRENT_SPRITE_VARNAME = "&current_sprite";

	private static final Log log = LogFactory.getLog(JDinkSpScriptFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer spriteNumber = toInteger(executionContext.getArgument(0), null);
		String scriptName = executionContext.getArgumentAsString(1, "");
		assertNotNull(spriteNumber, "sp script: spriteNumber missing");
		assertNotNull(scriptName, "sp script: scriptName missing");
		if (log.isDebugEnabled()) {
			log.debug("sp script: spriteNumber=" + spriteNumber + " scriptName=" + scriptName);
		}
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber.intValue(), true);
		JDinkScope spriteScope = requestSpriteScope(sprite, executionContext.getContext());
		if (sprite != null) {
			JDinkScriptFile scriptFile = executionContext.getContext().getScript(scriptName, true);
			if (scriptFile != null) {
				JDinkScriptInstance scriptInstance = new JDinkScriptInstance(
						scriptFile, new JDinkScope(spriteScope));
				scriptInstance.getScope().setVariableValue(CURRENT_SPRITE_VARNAME, spriteNumber);
				scriptInstance.initialize(executionContext.getContext());
				sprite.setScriptInstance(scriptInstance);
			} else {
				sprite.setScriptInstance(null);
			}
		}
		return null;
	}

}
