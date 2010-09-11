package de.siteof.jdink.functions.script;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;

/**
 * <p>Implementation of {@link JDinkFunction} used in place of the actual implementation.</p>
 */
public class JDinkUnimplementedFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkUnimplementedFunction.class);
	
	private final String functionName;
	
	public JDinkUnimplementedFunction(String functionName) {
		this.functionName = functionName;
	}

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object[] arguments = executionContext.getArguments();
		if (log.isWarnEnabled()) {
			log.warn("unimplemented function: functionName=" + functionName + " arguments=" + Arrays.asList(arguments));
		}
		return null;
	}

}
