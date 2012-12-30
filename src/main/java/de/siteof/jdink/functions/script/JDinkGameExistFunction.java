package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.save.JDinkSaveGameManager;

/**
 * <p>Function: int game_exist(int game)</p>
 */
public class JDinkGameExistFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkGameExistFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer value = toInteger(executionContext.getArgument(0), null);
		assertNotNull(value, "game exist: value missing");
		if (log.isDebugEnabled()) {
			log.debug("game exist: value=" + value);
		}
		int slotNumber = value.intValue();
		JDinkContext context = executionContext.getContext();
		JDinkSaveGameManager saveGameManager = JDinkSaveGameManager.getInstance(context);
		boolean exists = saveGameManager.saveGameExists(
				context, slotNumber);
		return new Integer(exists ? 1 : 0); // pretend that game 1 exists
	}

}
