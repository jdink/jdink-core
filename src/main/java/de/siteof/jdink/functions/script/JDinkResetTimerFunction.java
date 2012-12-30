package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void reset_timer()</p>
 */
public class JDinkResetTimerFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkResetTimerFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("reset timer");
		log.warn("reset_timer not implemented");
		return null;
	}

}
