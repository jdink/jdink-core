package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Implements the return statement, with optional return value.</p>
 */
public class JDinkScriptReturnFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkScriptReturnFunctionCall.class);


	public JDinkScriptReturnFunctionCall() {
		super("!RETURN");
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object value  = getArgument(0);
		if (log.isDebugEnabled()) {
			log.debug("return " + value);
		}
		value = asValue(executionContext, value);
		executionContext.setResult(value);
		executionContext.setState(JDinkExecutionContext.STATE_RETURN);
		return value;
	}
}
