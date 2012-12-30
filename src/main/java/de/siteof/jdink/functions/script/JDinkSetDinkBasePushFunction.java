package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkPlayer;

/**
 * <p>Function: void set_dink_base_push(int basePush)</p>
 * TODO untested
 */
public class JDinkSetDinkBasePushFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSetDinkBasePushFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer basePush = toInteger(executionContext.getArgument(0), null);
		assertNotNull(basePush, "set_dink_base_push basePush missing");
		if (log.isDebugEnabled()) {
			log.debug("set_dink_base_push: basePush=" + basePush);
		}
		JDinkPlayer player = executionContext.getContext().getCurrentPlayer();
		if (player != null) {
			player.setBasePush(basePush.intValue());
		} else {
			log.warn("[invoke] player not set");
		}
		return null;
	}

}
