package de.siteof.jdink.script;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;

/**
 * <p>JDinkScriptFunction represents a function/method definition of a script (e.g. "main").</p>
 */
public class JDinkScriptFunction implements JDinkFunction {

	private static final long serialVersionUID = 1L;

	private String resultType;
	private String name;
	private JDinkScriptBlock scriptBlock;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		if (scriptBlock != null) {
			scriptBlock.invoke(executionContext);
		} else {
			throw new RuntimeException("no script block defined for function " + this.getName());
		}

		Object result = null;

		switch (executionContext.getState()) {
		case JDinkExecutionContext.STATE_RETURN:
			result = executionContext.getResult();
			break;
		case JDinkExecutionContext.STATE_EXIT:
			break;
		case JDinkExecutionContext.STATE_GOTO:
			throw new RuntimeException("label not found " + executionContext.getResult() +
					" (function: " + this.getName() + ")");
		}
		return result;
	}

	public JDinkScriptBlock getScriptBlock() {
		return scriptBlock;
	}

	public void setScriptBlock(JDinkScriptBlock scriptBlock) {
		this.scriptBlock = scriptBlock;
	}
}
