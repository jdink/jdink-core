/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void push_active
 */
public class JDinkPushActiveFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkPushActiveFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer value = toInteger(executionContext.getArgument(0), null);
		assertNotNull(value, "push active: value missing");
		if (log.isDebugEnabled()) {
			log.debug("push active: value=" + value);
		}
		executionContext.getContext().getController().setPushActive(value.intValue() != 0);
		executionContext.getContext().getController().setChanged(true);
		return null;
	}

}
