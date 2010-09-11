package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void wait(int delay)</p>
 */
public class JDinkWaitFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkWaitFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer delay = toInteger(executionContext.getArgument(0), null);
		assertNotNull(delay, "wait: delay missing");
		if (log.isDebugEnabled()) {
			log.debug("wait: " + delay);
		}
		this.sleep(executionContext, delay.intValue());
		log.debug("wait end");
		return VOID;
	}

}
