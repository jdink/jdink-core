package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>This is not a real function call. Instead it defines a label.</p>
 */
public class JDinkGotoLabelFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String name;

	private static final Log log	= LogFactory.getLog(JDinkGotoLabelFunctionCall.class);

	public JDinkGotoLabelFunctionCall(String name) {
		super("!GOTO LABEL");
		this.name = name;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("goto label " + name);
		}
		// do nothing (a goto statement will for the labels instead)
		executionContext.setResult(name);
		executionContext.setState(JDinkExecutionContext.STATE_GOTO);
		return VOID;
	}

	public String getName() {
		return name;
	}
}
