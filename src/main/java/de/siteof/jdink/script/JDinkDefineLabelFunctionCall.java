package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>This is not a real function call. Instead it defines a label.</p>
 */
public class JDinkDefineLabelFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String name;

	private static final Log log	= LogFactory.getLog(JDinkDefineLabelFunctionCall.class);

	public JDinkDefineLabelFunctionCall(String name) {
		super("!LABEL");
		this.name = name;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("define label " + name);
		}
		// do nothing (a goto statement will for the labels instead)
		return VOID;
	}

	public String getName() {
		return name;
	}
}
