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
 * TODO not used
 */
@Deprecated
public class JDinkScriptVariableDeclarationFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String type;
	private final String variableName;

	private static final Log log	= LogFactory.getLog(JDinkScriptVariableDeclarationFunctionCall.class);


	public JDinkScriptVariableDeclarationFunctionCall(String type, String variableName) {
		super("!DECLARE");
		this.type = type;
		this.variableName = variableName;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("declare " + variableName + ":" + type);
		}
		JDinkVariable variable = new JDinkVariable();
		variable.setType(JDinkIntegerType.getInstance());
		executionContext.getScope().addVariable(variableName, variable);
		return null;
	}
}
