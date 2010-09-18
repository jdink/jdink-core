package de.siteof.jdink.save;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.brain.JDinkChoiceBrain;
import de.siteof.jdink.brain.JDinkItemMenuBrain;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.format.save.JDinkSaveGameDescriptorLoader;
import de.siteof.jdink.format.save.JDinkSaveGameLoader;
import de.siteof.jdink.format.save.JDinkSaveGameSerializer;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkMapState;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSaveGame;
import de.siteof.jdink.model.JDinkSaveGameDescriptor;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.script.JDinkVariable;
import de.siteof.jdink.status.JDinkStatusManager;

public class JDinkSaveGameManager {

	private static final Log log = LogFactory.getLog(JDinkSaveGameManager.class);

	private static final JDinkSaveGameManager saveGameManager =
		new JDinkSaveGameManager();

	public static JDinkSaveGameManager getInstance(JDinkContext context) {
		return saveGameManager;
	}

	private String getSaveGameResourceName(
			JDinkContext context, int slotNumber) throws IOException {
		return "SAVE" + slotNumber + ".DAT";
	}

	private Resource getSaveGameResource(
			JDinkContext context, int slotNumber) throws IOException {
		return context.getFileManager().getResource(
				getSaveGameResourceName(context, slotNumber), false);
	}

	private Resource getOutputSaveGameResource(
			JDinkContext context, int slotNumber) throws IOException {
		return context.getFileManager().getOutputResource(
				getSaveGameResourceName(context, slotNumber));
	}

	public JDinkSaveGame getSaveGame(
			JDinkContext context, int slotNumber) throws IOException {
		JDinkSaveGame saveGame = null;
		Resource resource = getSaveGameResource(context, slotNumber);
		if (resource != null) {
			JDinkSaveGameLoader loader = new JDinkSaveGameLoader();
			loader.load(resource);
			saveGame = loader.getSaveGame();
		}
		return saveGame;
	}

	public void storeSaveGame(JDinkContext context, int slotNumber, JDinkSaveGame saveGame) throws Exception {
		Resource resource = getOutputSaveGameResource(context, slotNumber);
		if (resource != null) {
			JDinkSaveGameSerializer serializer = new JDinkSaveGameSerializer();
			OutputStream out = null;
			try {
				out = resource.getOutputStream();
				serializer.serialize(out, saveGame);
			} catch (Exception e) {
				log.error("[storeSaveGame] failed to serialize save game", e);
				throw e;
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
	}

	public void loadSaveGame(JDinkContext context, JDinkSaveGame saveGame) throws Exception {
		JDinkChoiceBrain.getInstance().hideChoiceMenu(context);
		JDinkItemMenuBrain.getInstance().hideItemMenu(context);

		JDinkController controller = context.getController();
		log.info("Loading save game: " + saveGame.getGameInfo());
		controller.clearState(context);

		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();

		JDinkPlayer player = context.getCurrentPlayer();
		if (player == null) {
			throw new Exception("no current player");
		}

		JDinkSprite playerSprite = controller.getSprite(player.getSpriteNumber(), true);
		playerSprite.setX(saveGame.getX());
		playerSprite.setY(saveGame.getY());
		spriteHelper.setSpriteSequenceAndFrame(playerSprite, saveGame.getPseq(), saveGame.getPframe());
		playerSprite.setAnimationSequenceNumber(saveGame.getSeq());
		playerSprite.setAnimationFrameNumber(saveGame.getFrame());
		playerSprite.setBaseWalk(saveGame.getBaseWalk());
		playerSprite.setBaseAttack(saveGame.getBaseHit());
		playerSprite.setBaseIdle(saveGame.getBaseIdle());
		playerSprite.setDepthHint(saveGame.getQue());
		playerSprite.setDirectionIndex(saveGame.getDir());
		playerSprite.setStrength(saveGame.getStrength());
		playerSprite.setDefense(saveGame.getDefense());
		playerSprite.setSize(saveGame.getSize());
		playerSprite.setTiming(33);
		spriteHelper.setSpriteBrain(playerSprite, 1);

		playerSprite.setNoControl(false);

		player.getItemStore().clear();
		player.getMagicItemStore().clear();

		JDinkScope globalScope = context.getGlobalScope();

		Map<Integer, Map<String, JDinkVariable>> variableScopeMap = saveGame.getVariableScopeMap();
		if (variableScopeMap != null) {
			Map<String, JDinkVariable> variableMap = variableScopeMap.get(Integer.valueOf(0));
			if (variableMap != null) {
				for (Map.Entry<String, JDinkVariable> entry: variableMap.entrySet()) {
					globalScope.addVariable(entry.getKey(), entry.getValue());
				}
			}
		}

		Map<Integer, JDinkItem> items = saveGame.getItems();
		if (items != null) {
			for (JDinkItem item: items.values()) {
				JDinkItem fullItem = getFullItem(context, item);
				player.setItem(fullItem.getItemNumber(), fullItem);
			}
		}

		Map<Integer, JDinkItem> magicItems = saveGame.getMagicItems();
		if (magicItems != null) {
			for (JDinkItem item: magicItems.values()) {
				JDinkItem fullItem = getFullItem(context, item);
				player.setMagicItem(fullItem.getItemNumber(), fullItem);
			}
		}

		Map<Integer, JDinkMapState> mapStateMap = saveGame.getMapStateMap();
		if (mapStateMap != null) {
			for (Map.Entry<Integer, JDinkMapState> entry: mapStateMap.entrySet()) {
				context.setMapState(entry.getKey().intValue(), entry.getValue());
			}
		}

		JDinkStatusManager statusManager = JDinkStatusManager.getInstance(context);
		statusManager.drawStatus(context);

		controller.setGameMode(2);
	}

	public JDinkSaveGame createSaveGame(JDinkContext context) throws Exception {
		JDinkSaveGame saveGame = new JDinkSaveGame();
		JDinkController controller = context.getController();
		int level = context.getGlobalVariables().level.getInt(context);
		int mapNumber = context.getGlobalVariables().playerMap.getInt(context);
		saveGame.setVersion(108);
		saveGame.setGameInfo("Level " + level);
		log.info("Storing save game: " + saveGame.getGameInfo());

		JDinkPlayer player = context.getCurrentPlayer();
		if (player == null) {
			throw new Exception("no current player");
		}

		JDinkSprite playerSprite = controller.getSprite(player.getSpriteNumber(), true);
		saveGame.setX(playerSprite.getX());
		saveGame.setY(playerSprite.getY());
		saveGame.setPseq(playerSprite.getSequenceNumber());
		saveGame.setPframe(playerSprite.getFrameNumber());
		saveGame.setSeq(playerSprite.getAnimationSequenceNumber());
		saveGame.setFrame(playerSprite.getAnimationFrameNumber());
		saveGame.setBaseWalk(playerSprite.getBaseWalk());
		saveGame.setBaseHit(playerSprite.getBaseAttack());
		saveGame.setBaseIdle(playerSprite.getBaseIdle());
		saveGame.setQue(playerSprite.getDepthHint());
		saveGame.setDir(playerSprite.getDirectionIndex());
		saveGame.setStrength(playerSprite.getStrength());
		saveGame.setDefense(playerSprite.getDefense());
		saveGame.setSize(playerSprite.getSize());

		saveGame.setItems(player.getItemStore().getItems());
		saveGame.setMagicItems(player.getMagicItemStore().getItems());

		JDinkScope globalScope = context.getGlobalScope();
		Collection<String> variableNames = globalScope.getLocalVariableNames();

		Map<Integer, Map<String, JDinkVariable>> variableScopeMap = new HashMap<Integer, Map<String, JDinkVariable>>(1);
		Map<String, JDinkVariable> variableMap = new HashMap<String, JDinkVariable>(variableNames.size());
		for (String variableName: variableNames) {
			variableMap.put(variableName, globalScope.getVariable(variableName));
		}
		variableScopeMap.put(Integer.valueOf(0), variableMap);
		saveGame.setVariableScopeMap(variableScopeMap);

		saveGame.setLastMap(mapNumber);

		return saveGame;
	}

	private JDinkItem getFullItem(JDinkContext context, JDinkItem item) {
		JDinkItem result = new JDinkItem(item.getItemNumber());
		result.setSequenceNumber(item.getSequenceNumber());
		result.setFrameNumber(item.getFrameNumber());
		result.setSequence(context.getSequence(item.getSequenceNumber(), false));
		result.setScope(new JDinkScope());
		JDinkScriptInstance scriptInstance = item.getScriptInstance();
		JDinkScriptFile scriptFile = (scriptInstance != null ? scriptInstance.getScriptFile() : null);
		if (scriptFile != null) {
			JDinkScriptFile fullScriptFile = context.getScript(scriptFile.getFileName(), true);
			if (fullScriptFile != null) {
				result.setScriptInstance(new JDinkScriptInstance(
						fullScriptFile, result.getScope()));
			} else {
				log.warn("[getFullItem] script not found: " + scriptFile.getFileName());
			}
		}
		return result;
	}

	public JDinkSaveGameDescriptor getSaveGameDescriptor(
			JDinkContext context, int slotNumber) throws IOException {
		JDinkSaveGameDescriptor saveGame = null;
		Resource resource = getSaveGameResource(context, slotNumber);
		if (resource != null) {
			JDinkSaveGameDescriptorLoader loader = new JDinkSaveGameDescriptorLoader();
			loader.load(resource);
			saveGame = loader.getSaveGameDescriptor();
		}
		return saveGame;
	}

	public boolean saveGameExists(
			JDinkContext context, int slotNumber) throws IOException {
		Resource resource = getSaveGameResource(context, slotNumber);
		return (resource != null);
	}

}
