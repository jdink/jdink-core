package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void set_mode(int mode)</p>
 */
public class JDinkSetModeFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSetModeFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer mode = toInteger(executionContext.getArgument(0), null);
		assertNotNull(mode, "set mode: mode missing");
		if (log.isDebugEnabled()) {
			log.debug("set mode: " + mode);
		}
		executionContext.getContext().getController().setGameMode(mode.intValue());
		return null;
	}

}
