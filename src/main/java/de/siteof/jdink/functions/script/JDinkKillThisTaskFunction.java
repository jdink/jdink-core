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
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkKillThisTaskFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkKillThisTaskFunction.class);

	/* (non-Javadoc)
	 * @see de.siteof.jdink.functions.JDinkFunction#invoke(de.siteof.jdink.functions.JDinkExecutionContext)
	 */
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("kill this task");
//		log.warn("kill_this_task not implemented");
		executionContext.setState(JDinkExecutionContext.STATE_EXIT);
//		this.returnFunctionCall(executionContext);
		// TODO free memory and objects associated with this script
		return null;
	}

}
