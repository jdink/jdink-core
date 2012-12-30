package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Implementation of {@link JDinkScriptFunctionCall} for operators that take two arguments</p>
 */
public class JDinkScriptOperatorFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	public final static int OP_EQUALS = 1;
	public final static int OP_NOT_EQUALS = 2;
	public final static int OP_LESS_THAN = 3;
	public final static int OP_GREATER_THAN = 4;
	public final static int OP_LESS_THAN_OR_EQUALS = 6;
	public final static int OP_GREATER_THAN_OR_EQUALS = 5;
	public final static int OP_ADD = 7;
	public final static int OP_SUBTRACT = 8;

	private final int op;

	private static final Log log	= LogFactory.getLog(JDinkScriptOperatorFunctionCall.class);


	public JDinkScriptOperatorFunctionCall(int op) {
		super("!OP" + op);
		this.op = op;
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object operand1  = getArgument(0);
		Object operand2  = getArgument(1);
		if (this.getArgumentCount() != 2) {
			throw new RuntimeException("operator expected 2 operands, found " + this.getArgumentCount());
		}
		if (log.isDebugEnabled()) {
			log.debug("operator " + op);
		}
		Object result;
		switch(op) {
			case OP_EQUALS:
			case OP_NOT_EQUALS:
				operand1 = asValue(executionContext, operand1);
				operand2 = asValue(executionContext, operand2);
				boolean equals = ((operand1 == operand2) || ((operand1 != null) && (operand2 != null) && (operand1.equals(operand2))));
				if (op == OP_NOT_EQUALS) {
					equals = !equals;
				}
				result = Boolean.valueOf(equals);
				break;
			case OP_LESS_THAN:
			case OP_GREATER_THAN:
			case OP_LESS_THAN_OR_EQUALS:
			case OP_GREATER_THAN_OR_EQUALS:
			case OP_ADD:
			case OP_SUBTRACT:
				operand1 = asValue(executionContext, operand1);
				operand2 = asValue(executionContext, operand2);
				if (operand1 == null) {
					operand1 = Integer.valueOf(0);
				}
				if (operand2 == null) {
					operand2 = Integer.valueOf(0);
				}
				if ((operand1 instanceof String) || (operand2 instanceof String)) {
					return operand1.toString() + operand2.toString();
				}
				if (!(operand1 instanceof Integer)) {
					runtimeException("operand 1 is not integer");
					return null;
				}
				if (!(operand2 instanceof Integer)) {
					runtimeException("operand 2 is not integer");
					return null;
				}
				int i1 = ((Integer) operand1).intValue();
				int i2 = ((Integer) operand2).intValue();
				switch(op) {
				case OP_LESS_THAN:
					result = Boolean.valueOf(i1 < i2);
					break;
				case OP_GREATER_THAN:
					result = Boolean.valueOf(i1 > i2);
					break;
				case OP_LESS_THAN_OR_EQUALS:
					result = Boolean.valueOf(i1 <= i2);
					break;
				case OP_GREATER_THAN_OR_EQUALS:
					result = Boolean.valueOf(i1 >= i2);
					break;
				case OP_ADD:
					result = Integer.valueOf(i1 + i2);
					break;
				case OP_SUBTRACT:
					result = Integer.valueOf(i1 - i2);
					break;
				default:
					// cannot happen
					result = null;
				}
				break;
			default:
				runtimeException("invalid op: " + op);
				result = null;
		}
		return result;
	}
}
