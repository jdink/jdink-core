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
public class JDinkScriptInstanceVariableDeclarationFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private final String type;
	private final String name;

	private static final Log log	= LogFactory.getLog(JDinkScriptInstanceVariableDeclarationFunctionCall.class);

	public JDinkScriptInstanceVariableDeclarationFunctionCall(String type, String name) {
		super("!DECLARE");
		this.type = type;
		this.name = name;
	}

	public Object invoke(JDinkExecutionContext parentExecutionContext) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("declare " + name + ":" + type);
		}
		JDinkScriptInstance thisValue = (JDinkScriptInstance) parentExecutionContext.getScope().getInternalVariableValue("this");
		if (thisValue == null) {
			throw new RuntimeException("this == null?!");
		}
		JDinkVariable variable = new JDinkVariable();
		variable.setType(JDinkIntegerType.getInstance());
		thisValue.getScope().addVariable(name, variable);
		return null;
	}
}
