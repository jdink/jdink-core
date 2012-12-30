package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void breakpoint(</p>
 */
public class JDinkBreakpointFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkBreakpointFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		if (true) {
			log.debug("breakpoint");
		}
		log.warn("breakpoint not implemented");
		return null;
	}

}
