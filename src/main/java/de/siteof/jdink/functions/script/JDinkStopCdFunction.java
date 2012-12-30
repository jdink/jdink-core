package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void stopcd()</p>
 */
public class JDinkStopCdFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkStopCdFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("stop cd");
		log.warn("stop_cd not implemented");
		return null;
	}

}
