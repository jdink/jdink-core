package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Implementation of {@link JDinkScriptFunctionCall} for simple modificators (e.g. negate)</p>
 */
public class JDinkScriptModificatorFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	public final static int OP_NEG = 1;
	public final static int OP_NOT = 2;
	public final static int OP_INCREMENT_AND_GET = 3;
	public final static int OP_GET_AND_INCREMENT = 4;
	public final static int OP_DECREMENT_AND_GET = 5;
	public final static int OP_GET_AND_DECREMENT = 6;

	private final int op;

	private static final Log log	= LogFactory.getLog(JDinkScriptModificatorFunctionCall.class);


	public JDinkScriptModificatorFunctionCall(int op) {
		super("!MOD" + op);
		this.op = op;
	}
	
//	private int getInt

	public Object invoke(JDinkExecutionContext parentExecutionContext) throws Throwable {
		Object operand  = getArgument(0);
		if (this.getArgumentCount() != 1) {
			throw new RuntimeException("modificator expected 1 operand, found " + this.getArgumentCount());
		}
		if (operand instanceof JDinkScriptFunctionCall) {
			operand = ((JDinkScriptFunctionCall) operand).invoke(parentExecutionContext);
		}
		if (log.isDebugEnabled()) {
			log.debug("modificator " + op + " value=" + operand);
		}
		switch(op) {
			case OP_NEG:
			case OP_INCREMENT_AND_GET:
			case OP_GET_AND_INCREMENT:
			case OP_DECREMENT_AND_GET:
			case OP_GET_AND_DECREMENT:
				if (operand == null) {
					operand = new Integer(0);
				}
				if (operand instanceof Integer) {
					return new Integer(-((Integer) operand).intValue());
				} else {
					throw new RuntimeException("integer operand expected (" + operand + ")");
				}
			case OP_NOT:
				if (operand == null) {
					operand = Boolean.TRUE;
				}
				if (operand instanceof Boolean) {
					return Boolean.valueOf(!((Boolean) operand).booleanValue());
				} else {
					throw new RuntimeException("boolean operand expected (" + operand + ")");
				}
			default:
				throw new RuntimeException("invalid modificator: " + op);
		}
	}
}
