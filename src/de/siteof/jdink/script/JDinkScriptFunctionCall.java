package de.siteof.jdink.script;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;

/**
 * <p>Implementation of {@link JDinkScriptCall} calling a function.</p>
 */
public class JDinkScriptFunctionCall extends AbstractJDinkScriptCall {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkScriptFunctionCall.class);

	private final String functionName;
	private final List<Object> arguments = new ArrayList<Object>();


	// caches the function
	private transient JDinkFunction function;

	public JDinkScriptFunctionCall(String functionName) {
		this.functionName = functionName;
	}


	public String getFunctionName() {
		return functionName;
	}

//	public void setFunctionName(String functionName) {
//		this.functionName = functionName;
//	}

	public void addArgument(Object value) {
		arguments.add(value);
	}

	public Object popArgument() {
		return arguments.remove(arguments.size() - 1);
	}

	public Object[] getArguments() {
		return arguments.toArray(new Object[0]);
	}

	public Object getArgument(int index) {
		return (index < arguments.size() ? arguments.get(index) : null);
	}

	public int getArgumentCount() {
		return arguments.size();
	}

	@Override
	public Object invoke(JDinkExecutionContext parentExecutionContext) throws Throwable {
		JDinkExecutionContext executionContext = new JDinkExecutionContext();
		Object[] arguments = this.getArguments();
		executionContext.setContext(parentExecutionContext.getContext());
		executionContext.setFunctionName(this.getFunctionName());
		executionContext.setScope(new JDinkScope(parentExecutionContext.getScope()));
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = asValue(parentExecutionContext, arguments[i]);
		}
		executionContext.setArguments(arguments);
		JDinkScriptInstance thisValue = (JDinkScriptInstance) parentExecutionContext.getScope().getInternalVariableValue("this");
		if (thisValue == null) {
			throw new RuntimeException("this == null?!");
		}
		JDinkFunction function = this.function;
		if (function == null) {
			function = thisValue.getScriptFile().getFunctionByName(this.getFunctionName());
			if (function == null) {
				function = parentExecutionContext.getContext().getFunction(this.getFunctionName());
			}
			this.function = function;
		}
		try {
			if (function == null) {
				throw new RuntimeException("(" + this.getLineNo() + ") unknown function:" + this.getFunctionName());
			}
			Object value = function.invoke(executionContext);
//			parentExecutionContext.setResult(parentExecutionContext.getResult());
//			parentExecutionContext.setState(parentExecutionContext.getState());
			parentExecutionContext.setResult(executionContext.getResult());
			parentExecutionContext.setState(executionContext.getState());
			return value;
		} catch (Throwable e) {
			log.error("error invoking (" + this.getLineNo() + ") " + this.getFunctionName() + ":" + e, e);
			throw e;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": function=" + this.getFunctionName() + " line=" + this.getLineNo();
	}
}
