package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkVariable;

/**
 * <p>Function: void make_global_int(int name, int value)</p>
 */
public class JDinkMakeGlobalIntFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkMakeGlobalIntFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String variableName = executionContext.getArgumentAsString(0, null);
		Integer value = toInteger(executionContext.getArgument(1), null);
		assertNotNull(variableName, "make global int: variableName missing");
		assertNotNull(value, "make global int: value missing");
		if (log.isDebugEnabled()) {
			log.debug("make global int: variableName=" + variableName + " value=" + value);
		}
		JDinkVariable variable = new JDinkVariable();
		variable.setType(new JDinkIntegerType());
		executionContext.getContext().getGlobalScope().addVariable(variableName, variable);
		variable.setValue(value);
		return null;
	}

}
