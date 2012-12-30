package de.siteof.jdink.choice;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSaveGameDescriptor;
import de.siteof.jdink.save.JDinkSaveGameManager;

public class JDinkSaveGameInfoChoicePlaceholder implements
		JDinkChoicePlaceholder {

	private static final Log log = LogFactory.getLog(JDinkSaveGameInfoChoicePlaceholder.class);

	@Override
	public String getText(JDinkExecutionContext executionContext, int index) {
		String text = null;
		int slotNumber = (1 + index);
		try {
			JDinkContext context = executionContext.getContext();
			JDinkSaveGameManager saveGameManager = JDinkSaveGameManager.getInstance(context);
			JDinkSaveGameDescriptor saveGame = saveGameManager.getSaveGameDescriptor(
					context, slotNumber);
			if (saveGame != null) {
				int minutes = saveGame.getMinutes();
				text = "Slot " + slotNumber + " - " +
						(minutes / 60) + ":" + new DecimalFormat("00").format(minutes % 60) +
						" - " + saveGame.getGameInfo();
			}
		} catch (IOException e) {
			log.error("[getText] failed to get text due to " + e, e);
			text = e.getMessage();
		}
		if (text == null) {
			text = "Slot " + slotNumber + " - Empty";
		}
		return text;
	}

}
