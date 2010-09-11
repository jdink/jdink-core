package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Sets a variable (assignment).</p>
 */
public class JDinkScriptVariableSetFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String variableName;

	private static final Log log	= LogFactory.getLog(JDinkScriptVariableSetFunctionCall.class);


	public JDinkScriptVariableSetFunctionCall(String variableName) {
		super("!SET");
		this.variableName = variableName;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object value = getArgument(0);
		value = asValue(executionContext, value);
		if (log.isDebugEnabled()) {
			log.debug("set " + variableName + "=" + value);
		}
		executionContext.getScope().setVariableValue(variableName, value);
		return null;
	}

	public String getVariableName() {
		return variableName;
	}
}
