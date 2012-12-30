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
public class JDinkFillScreenFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkFillScreenFunction.class);

	/* (non-Javadoc)
	 * @see de.siteof.jdink.functions.JDinkFunction#invoke(de.siteof.jdink.functions.JDinkExecutionContext)
	 */
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer value = toInteger(executionContext.getArgument(0), null);
		assertNotNull(value, "fill screen: value missing");
		executionContext.getContext().getController().setBackgroundColorIndex(value.intValue());
		executionContext.getContext().getController().setChanged(true);
		if (log.isDebugEnabled()) {
			log.debug("fill screen: value=" + value);
		}
		log.warn("fill_screen not implemented");
		return null;
	}

}
