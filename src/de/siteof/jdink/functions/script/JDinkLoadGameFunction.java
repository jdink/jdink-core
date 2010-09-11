package de.siteof.jdink.functions.script;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSaveGame;
import de.siteof.jdink.save.JDinkSaveGameManager;

/**
 * <p>Function: void load_game(int slotNumber)</p>
 */
public class JDinkLoadGameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

//	private static final Log log = LogFactory.getLog(JDinkLoadGameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer slotNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(slotNumber, "load_game: slotNumber missing");
		JDinkContext context = executionContext.getContext();
		JDinkSaveGameManager saveGameManager = JDinkSaveGameManager.getInstance(context);
		JDinkSaveGame saveGame = saveGameManager.getSaveGame(context, slotNumber.intValue());
		if (saveGameManager == null) {
			throw new Exception("Save game not loaded, slotNumber=" + slotNumber);
		}
		saveGameManager.loadSaveGame(context, saveGame);
		return null;
	}

}
