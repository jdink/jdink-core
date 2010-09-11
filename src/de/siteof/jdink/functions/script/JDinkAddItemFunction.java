package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;

/**
 * <p>Function: void add_item(int spriteNumber, int sequenceNumber, int frameNumber)</p>
 */
public class JDinkAddItemFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkAddItemFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		String scriptName = executionContext.getArgumentAsString(0, "");
		Integer sequenceNumber = toInteger(executionContext.getArgument(1), null);
		Integer frameNumber = toInteger(executionContext.getArgument(2), null);
		assertNotNull(scriptName, "add item: scriptName missing");
		assertNotNull(sequenceNumber, "add item: sequenceNumber missing");
		assertNotNull(frameNumber, "add item: frameNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("add item: scriptName=" + scriptName + " sequenceNumber=" + sequenceNumber + " frameNumber=" + frameNumber);
		}
		JDinkContext context = executionContext.getContext();
		JDinkPlayer player = context.getCurrentPlayer();
		if (player == null) {
			throw new Exception("no current player");
		}
		JDinkItem item = player.allocateItem();
		if (item != null) {
			item.setSequenceNumber(sequenceNumber.intValue());
			item.setSequence(executionContext.getContext().getSequence(item.getSequenceNumber(), false));
			item.setFrameNumber(frameNumber.intValue());
			JDinkScope spriteScope = item.requestScope(executionContext.getContext());
			JDinkScriptFile scriptFile = executionContext.getContext().getScript(scriptName, true);
			if (scriptFile != null) {
				JDinkScriptInstance scriptInstance = new JDinkScriptInstance();
				scriptInstance.setScriptFile(scriptFile);
				JDinkScope scope = new JDinkScope(spriteScope);
				scriptInstance.setScope(scope);
				//scope.setVariableValue(CURRENT_SPRITE_VARNAME, spriteNumber);
				scriptInstance.initialize(executionContext.getContext());
				item.setScriptInstance(scriptInstance);
			} else {
				item.setScriptInstance(null);
			}
		}
		return null;
	}

}
