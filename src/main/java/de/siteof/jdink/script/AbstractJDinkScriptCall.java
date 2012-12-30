package de.siteof.jdink.script;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Abstract class for implementations of {@link JDinkScriptCall}.<p>
 */
public abstract class AbstractJDinkScriptCall implements JDinkScriptCall {

	private static final long serialVersionUID = 1L;
	
	protected static final Object VOID = null;

	private int lineNo;
	
	public int getLineNo() {
		return lineNo;
	}
	
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	
	protected Object asValue(JDinkExecutionContext executionContext, Object value) throws Throwable {
		if (value instanceof JDinkScriptCall) {
			return ((JDinkScriptCall) value).invoke(executionContext);
		}
		return value;
	}
	
	protected String getExceptionMessage(String text) {
		return "(" + this.getLineNo() + ") " + text;
	}
	
	protected void runtimeException(String text) throws Throwable {
		throw new RuntimeException(getExceptionMessage(text));
	}

}
