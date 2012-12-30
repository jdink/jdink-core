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
public class JDinkScriptSetOperatorFunctionCall extends JDinkScriptOperatorFunctionCall {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkScriptSetOperatorFunctionCall.class);


	public JDinkScriptSetOperatorFunctionCall(int op) {
		super(op);
		// TODO amend function name
//		this.setFunctionName("!SET " + this.getFunctionName());
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object operand1  = getArgument(0);
		Object operand2  = getArgument(1);
		if (this.getArgumentCount() != 2) {
			runtimeException("operator expected 2 operands, found " + this.getArgumentCount() + " (" + operand1 + ", " + operand2 + ")");
		}
		String name;
		if (operand1 instanceof JDinkScriptVariableGetFunctionCall) {
			name = ((JDinkScriptVariableGetFunctionCall) operand1).getVariableName();
		} else {
			runtimeException("set-operator: operand1 should be a variable (is:" + operand1 + ")");
			return null;
		}
		Object result = super.invoke(executionContext);
		if (log.isDebugEnabled()) {
			log.debug("set variable " + name + "=" + result);
		}
		executionContext.getScope().setVariableValue(name, result);
		return result;
	}
}
