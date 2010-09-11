package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void debug(String msg)</p>
 */
public class JDinkDebugFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkDebugFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String text = executionContext.getArgumentAsString(0, null);
		assertNotNull(text, "debug: text missing");
		log.info("dink debug: " + text);
		//log.warn("debug not implemented");
		return null;
	}

}
