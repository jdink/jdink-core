package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: screenlock(int screenLocked)</p>
 */
public class JDinkScreenLockFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkScreenLockFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer value = toInteger(executionContext.getArgument(0), null);
		assertNotNull(value, "screen lock: value missing");
		if (log.isDebugEnabled()) {
			log.debug("screen lock: value=" + value);
		}
		JDinkController controller = executionContext.getContext().getController();
		boolean screenLocked = value.intValue() == 1;
		if (controller.isScreenLocked() != screenLocked) {
			controller.setScreenLocked(screenLocked);
			executionContext.getContext().getController().setChanged(true);
		}
		return null;
	}

}
