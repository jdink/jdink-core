/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkScriptVariableGetFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String variableName;

	private static final Log log	= LogFactory.getLog(JDinkScriptVariableGetFunctionCall.class);


	public JDinkScriptVariableGetFunctionCall(String variableName) {
		super("!GET");
		this.variableName = variableName;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("!get " + variableName);
		}
		Object value = executionContext.getScope().getVariableValue(variableName);
		if (value == null) {
			value = new Integer(0);
		}
		return value;
	}

	public String getVariableName() {
		return variableName;
	}
}
