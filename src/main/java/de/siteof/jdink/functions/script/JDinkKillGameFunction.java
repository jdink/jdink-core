package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void kill_game()</p>
 * <p>Halts the game.</p>
 */
public class JDinkKillGameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkKillGameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("kill game");
		log.warn("kill_game not implemented");
		System.exit(0);
		return null;
	}

}
