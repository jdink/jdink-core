package de.siteof.jdink.functions;

import java.io.Serializable;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptCall;

/**
 * <p>The execution context is used to call any method. It holds the temporary state of the execution.
 * Successive calls will use a new context. A new execution context may also be called
 * to call a function within a function.</p>
 */
public class JDinkExecutionContext implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int STATE_NONE = 0;
	public final static int STATE_RETURN = 1;
	public final static int STATE_GOTO = 2;
	public final static int STATE_EXIT = 3;

	public final static Object[] EMPTY_ARRAY = new Object[0];

	private JDinkContext context;
	private JDinkScope scope;
	private String functionName;
	private Object[] arguments;
	private Object result;
	private int state;

	public JDinkExecutionContext(
			JDinkContext context, JDinkScope scope, String functionName, Object[] arguments) {
		this.context = context;
		this.scope = scope;
		this.functionName = functionName;
		this.arguments = arguments;
	}

	public JDinkExecutionContext(
			JDinkContext context, JDinkScope scope, String functionName) {
		this(context, scope, functionName, EMPTY_ARRAY);
	}

	public JDinkExecutionContext() {
		// do nothing
	}

	public Object asValue(Object value) throws Throwable {
		if (value instanceof JDinkScriptCall) {
			return ((JDinkScriptCall) value).invoke(this);
		}
		return value;
	}

	public String getAsString(Object o, String defaultValue) {
		return (o != null ? o.toString() : defaultValue);
	}

	public String getArgumentAsString(int index, String defaultValue) {
		return (index < arguments.length ? getAsString(arguments[index], defaultValue) : defaultValue);
	}

	public Object getArgument(int index, Object defaultValue) {
		return ((index < arguments.length) && (arguments[index] != null) ? arguments[index] : defaultValue);
	}

	public Object getArgument(int index) {
		return getArgument(index, null);
	}

	// plain getters/setters

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public JDinkContext getContext() {
		return context;
	}

	public void setContext(JDinkContext context) {
		this.context = context;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public JDinkScope getScope() {
		return scope;
	}

	public void setScope(JDinkScope scope) {
		this.scope = scope;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
