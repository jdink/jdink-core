package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int get_version()</p>
 * <p>Returns the version of the game engine, e.g. 108.</p>
 */
public class JDinkGetVersionFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkGetVersionFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("get_version");
		return Integer.valueOf(108);
	}

}
