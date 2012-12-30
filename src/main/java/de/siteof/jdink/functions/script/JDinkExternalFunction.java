package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptFunction;
import de.siteof.jdink.script.JDinkScriptInstance;

/**
 * <p>Function: void external(String scriptName, String methodName)</p>
 */
public class JDinkExternalFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkExternalFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		String scriptName = executionContext.getArgumentAsString(0, "");
		String methodName = executionContext.getArgumentAsString(1, "");
		assertNotNull(scriptName, "external: scriptName missing");
		assertNotNull(methodName, "external: methodName missing");
		if (log.isDebugEnabled()) {
			log.debug("external: scriptName=" + scriptName + " methodName=" + methodName);
		}
		JDinkContext context = executionContext.getContext();
		JDinkScriptFile scriptFile = context.getScript(scriptName, true);
		if (scriptFile == null) {
			throw new Exception("script not found, scriptName=[" + scriptName + "]");
		}
		JDinkScriptFunction function = scriptFile.getFunctionByName(methodName);
		if (function == null) {
			throw new Exception("script function not found, scriptName=[" + scriptName +
					"], methodName=[" + methodName + "]");
		}
		JDinkScriptInstance scriptInstance = new JDinkScriptInstance(
				scriptFile, new JDinkScope(context.getGlobalScope()));
		return scriptInstance.callFunction(context, function, new Object[0]);
	}

}
