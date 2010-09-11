package de.siteof.jdink.functions.script;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSaveGame;
import de.siteof.jdink.save.JDinkSaveGameManager;

/**
 * <p>Function: void save_game(int slotNumber)</p>
 */
public class JDinkSaveGameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

//	private static final Log log = LogFactory.getLog(JDinkLoadGameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer slotNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(slotNumber, "save_game: slotNumber missing");
		JDinkContext context = executionContext.getContext();
		JDinkSaveGameManager saveGameManager = JDinkSaveGameManager.getInstance(context);
		JDinkSaveGame saveGame = saveGameManager.createSaveGame(context);
		if (saveGameManager == null) {
			throw new Exception("Save game not created");
		}
		saveGameManager.storeSaveGame(context, slotNumber.intValue(), saveGame);
		return null;
	}

}
