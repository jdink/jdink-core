package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Similar to {@link JDinkScriptModificatorFunctionCall} but
 * also setting the value of the variable (first argument must be a variable get operand)</p>
 */
public class JDinkScriptSetModificatorFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	public final static int OP_INCREMENT_AND_GET = 3;
	public final static int OP_GET_AND_INCREMENT = 4;
	public final static int OP_DECREMENT_AND_GET = 5;
	public final static int OP_GET_AND_DECREMENT = 6;

	private final int op;

	private static final Log log	= LogFactory.getLog(JDinkScriptSetModificatorFunctionCall.class);


	public JDinkScriptSetModificatorFunctionCall(int op) {
		super("!SMOD" + op);
		this.op = op;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object operand1  = getArgument(0);
		if (this.getArgumentCount() != 1) {
			throw new RuntimeException("modificator expected 1 operand, found " + this.getArgumentCount());
		}
		String name;
		if (operand1 instanceof JDinkScriptVariableGetFunctionCall) {
			name = ((JDinkScriptVariableGetFunctionCall) operand1).getVariableName();
		} else {
			runtimeException("set-operator: operand1 should be a variable (is:" + operand1 + ")");
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug("set modificator " + op + " value=" + operand1);
		}
		if (operand1 == null) {
			operand1 = Integer.valueOf(0);
		}
		if (!(operand1 instanceof Integer)) {
			throw new RuntimeException("integer operand expected (" + operand1 + ")");
		}
		int variableValue = ((Integer) operand1).intValue();
		int result;
		switch(op) {
			case OP_INCREMENT_AND_GET:
				result = ++variableValue;
				break;
			case OP_GET_AND_INCREMENT:
				result = variableValue++;
				break;
			case OP_DECREMENT_AND_GET:
				result = --variableValue;
				break;
			case OP_GET_AND_DECREMENT:
				result = variableValue--;
				break;
			default:
				throw new RuntimeException("invalid modificator: " + op);
		}
		if (log.isDebugEnabled()) {
			log.debug("set variable " + name + "=" + result);
		}
		executionContext.getScope().setVariableValue(name, Integer.valueOf(result));
		return Integer.valueOf(result);
	}
}
