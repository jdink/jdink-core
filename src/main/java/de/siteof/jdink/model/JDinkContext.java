package de.siteof.jdink.model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.cache.IObjectCache;
import de.siteof.jdink.brain.JDinkBrain;
import de.siteof.jdink.collision.JDinkCollisionTester;
import de.siteof.jdink.collision.JDinkCollisionTesterImpl;
import de.siteof.jdink.collision.JDinkConfiguration;
import de.siteof.jdink.collision.JDinkHardnessMap;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.format.dinkc.JDinkCLoader;
import de.siteof.jdink.format.dinkd.JDinkDLoader;
import de.siteof.jdink.format.hardness.JDinkTileHardnessEntry;
import de.siteof.jdink.format.hardness.JDinkTileHardnessLoader;
import de.siteof.jdink.format.map.JDinkMapEntry;
import de.siteof.jdink.format.map.JDinkMapLoader;
import de.siteof.jdink.format.map.JDinkMapTile;
import de.siteof.jdink.format.mapinfo.JDinkMapInfo;
import de.siteof.jdink.format.mapinfo.JDinkMapInfoLoader;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.interaction.JDinkInteractionManager;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.loader.JDinkFileManager;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.util.ArrayListMap;
import de.siteof.jdink.util.CaseInsensitveHashMap;
import de.siteof.jdink.util.IntegerHashMap;
import de.siteof.jdink.util.IntegerMap;
import de.siteof.jdink.util.ObjectCacheFactory;
import de.siteof.jdink.view.ColorConstants;
import de.siteof.jdink.view.JDinkImage;
import de.siteof.jdink.view.JDinkImageFactory;
import de.siteof.jdink.view.JDinkView;
import de.siteof.task.ITaskManager;

/**
 * <p>Contains the context of the game. Mostly static information about the game.</p>
 */
public class JDinkContext {

	private static final Log log = LogFactory.getLog(JDinkContext.class);

	private static final JDinkScriptFile NULL_SCRIPT_FILE = new JDinkScriptFile();

	private String gameId;
	private JDinkConfiguration configuration;
	private final Map<String, JDinkFunction> functionMap =
		new CaseInsensitveHashMap<JDinkFunction>();
	private JDinkFileManager fileManager;
	private final IntegerMap<JDinkSequence> sequenceMap =
		new IntegerHashMap<JDinkSequence>();
//	private final IntegerMap<JDinkSequence> sequenceMap =
//		new ArrayListMap<JDinkSequence>(new JDinkSequence[0]);
	private final Map<JDinkSequenceFrameKey, JDinkSequenceFrameAttributes> frameAttributesMap =
		new HashMap<JDinkSequenceFrameKey, JDinkSequenceFrameAttributes>();
	private JDinkScope globalScope = new JDinkScope();
	private JDinkController controller;
	private ITaskManager taskManager;
	private JDinkView view;
	private JDinkImageFactory imageLoader;
	private final Map<String, JDinkScriptFile> scriptFileMap =
		new CaseInsensitveHashMap<JDinkScriptFile>();
	private final IntegerMap<JDinkBrain> brainMap = new ArrayListMap<JDinkBrain>(new JDinkBrain[0]);
	private final Map<Integer, JDinkSound> soundMap = new HashMap<Integer, JDinkSound>();
	private final IObjectCache<String, JDinkImage> imageCacheByFileName =
		ObjectCacheFactory.getInstance().createCache("imageCacheByFileName");
	private final IObjectCache<Resource, JDinkImage> imageCacheByResource =
		ObjectCacheFactory.getInstance().createCache("imageCacheByResource");
	private final IntegerMap<JDinkTileSet> tileSetMap = new ArrayListMap<JDinkTileSet>(new JDinkTileSet[0]);
	private JDinkTileHardnessEntry[] tileHardnessEntries;
	private int[] hardnessIndex;
	private final JDinkHardnessMap hardnessMap = new JDinkHardnessMap(20, 0, 601, 401);
	private int[] fontColors;
	private int textBorderColor;
	private JDinkCollisionTester collisionTester = new JDinkCollisionTesterImpl();
	private JDinkInteractionManager interactionManager;
	private final JDinkSpriteHelper spriteHelper;
	private JDinkPlayer currentPlayer;
	private final Map<Integer, JDinkMapState> mapStateMap = new HashMap<Integer, JDinkMapState>();

	private final Map<Object, Object> metaData = new HashMap<Object, Object>();

	private final JDinkGlobalVariables globalVariables = new JDinkGlobalVariables();

	private final Random random = new Random();

	private transient Map<Integer, JDinkMapEntry> mapEntryMap = new HashMap<Integer, JDinkMapEntry>();


	public JDinkContext() {
		spriteHelper = new JDinkSpriteHelper(this);
	}

	public void clearGame() {
		this.clearMapState();
		this.sequenceMap.clear();
		this.frameAttributesMap.clear();
		this.globalScope.clear();
		this.globalVariables.detach();
	}

	public long getTime() {
		// TODO may need to use a constant time throughout the frame
		return System.currentTimeMillis();
	}

	public JDinkTileSet getTileSet(int tileSetNumber, boolean create) {
		if (tileSetNumber == 0) {
			return null;
		}
		JDinkTileSet tileSet = tileSetMap.get(tileSetNumber);
		if ((tileSet == null) && (create)) {
			tileSet = new JDinkTileSet();
			tileSet.setImage(getImage("tiles/ts" + (tileSetNumber / 10) + (tileSetNumber % 10) + ".bmp"));
			tileSetMap.put(tileSetNumber, tileSet);
		}
		return tileSet;
	}

	public JDinkMapInfo getMapInfo() {
		String resourceName	= "dink.dat";
		try {
			JDinkMapInfoLoader mapInfoLoader = new JDinkMapInfoLoader();
			mapInfoLoader.load(this.getFileManager().getResource(resourceName));
			return mapInfoLoader.getMapInfo();
		} catch (IOException e) {
			log.error("error reading map (" + resourceName + ")" + " - " + e, e);
			return null;
		}
	}

	public boolean hasMapEntry(int mapNumber) {
		boolean result = false;
		JDinkMapInfo mapInfo = getMapInfo();
		if (mapInfo != null) {
			if ((mapNumber >= 0) && (mapNumber < mapInfo.getLoc().length)) {
				int mapEntryIndex = mapInfo.getLoc()[mapNumber];
				result = (mapEntryIndex > 0);
			}
		}
		return result;
	}

	public JDinkMapEntry getMapEntry(int mapNumber) {
		JDinkMapEntry mapEntry = null;
		String resourceName	= "map.dat";
		try {
			Integer key = Integer.valueOf(mapNumber);
			if (mapEntryMap != null) {
				mapEntry = mapEntryMap.get(key);
			}
			if (mapEntry == null) {
				JDinkMapInfo mapInfo = getMapInfo();
				if (mapInfo != null) {
					int mapEntryIndex = mapInfo.getLoc()[mapNumber];
					JDinkMapLoader mapLoader = new JDinkMapLoader();
					mapLoader.load(this.getFileManager().getResource(resourceName));
					mapEntry = mapLoader.getMapEntry(mapEntryIndex);
					if (log.isDebugEnabled()) {
						log.debug("map entry: " + mapEntry);
					}
					if (mapEntryMap == null) {
						mapEntryMap = new HashMap<Integer, JDinkMapEntry>();
					}
					mapEntryMap.put(key, mapEntry);
				}
			}
		} catch (IOException e) {
			log.error("error reading map (" + resourceName + ")" + " - " + e, e);
		}
		return mapEntry;
	}

	public byte[][] getMapTileHardness(JDinkMapTile mapTile) {
		byte[][] result = null;
		if (tileHardnessEntries == null) {
			String resourceName	= "hard.dat";
			try {
				JDinkTileHardnessLoader hardnessLoader = new JDinkTileHardnessLoader();
				hardnessLoader.load(this.getFileManager().getResource(resourceName));
				tileHardnessEntries = hardnessLoader.getHardnessEntries();
				hardnessIndex = hardnessLoader.getHardnessIndex();
			} catch (IOException e) {
				log.error("error reading map (" + resourceName + ")" + " - " + e, e);
			}
		}
		if (tileHardnessEntries != null) {
			int hardnessNumber = mapTile.getAlthard();
			if (hardnessNumber <= 0) {
				hardnessNumber = hardnessIndex[mapTile.getNum()];
			}
			if (hardnessNumber < tileHardnessEntries.length) {
				result = tileHardnessEntries[hardnessNumber].getHardness();
			} else {
				log.warn("invalid hardness number: " +
						"hardnessNumber=[" + hardnessNumber + "], " +
						"tileHardnessEntries.length=[" + tileHardnessEntries.length + "], " +
						"althard=[" + mapTile.getAlthard() + "], " +
						"num=[" + mapTile.getNum() + "]");
			}
		}
		return result;
	}

	public void clearMapState() {
		this.mapStateMap.clear();
	}

	public void setMapState(int mapNumber, JDinkMapState mapState) {
		this.mapStateMap.put(Integer.valueOf(mapNumber), mapState);
	}

	public JDinkMapState getMapState(int mapNumber) {
		return getMapState(mapNumber, false);
	}

	public JDinkMapState getMapState(int mapNumber, boolean create) {
		Integer key = Integer.valueOf(mapNumber);
		JDinkMapState mapState = this.mapStateMap.get(key);
		if ((mapState == null) && (create)) {
			mapState = new JDinkMapState();
			this.mapStateMap.put(key, mapState);
		}
		return mapState;
	}

	public JDinkMapState getMapState(boolean create) {
		int mapNumber = controller.getCurrentMapNumber();
		return this.getMapState(mapNumber, true);
	}

	public int getAndSetEditorSpriteState(int editorSpriteNumber, int state) {
		int mapNumber = controller.getCurrentMapNumber();
		JDinkMapState mapState = this.getMapState(mapNumber, true);
		int result = mapState.getEditorSpriteState(editorSpriteNumber);
		mapState.setEditorSpriteState(editorSpriteNumber, state);
		return result;
	}

	public JDinkImage getImage(Resource resource) {
		if (resource == null) {
			throw new IllegalArgumentException("resource == null");
		}
		JDinkImage image;
		synchronized (imageCacheByResource) {
			image	= (JDinkImage) imageCacheByResource.get(resource);
		}
		if (image == null) {
			try {
				image = this.getImageLoader().getImage(resource);
				if (image == null) {
					if (log.isDebugEnabled()) {
						log.debug("image could not be loaded:" + resource.getName());
					}
					return null;
				}
				synchronized (imageCacheByResource) {
					imageCacheByResource.put(resource, image);
				}
				if (log.isDebugEnabled()) {
					log.debug("image loaded:" + resource.getName());
				}
			} catch (IOException e) {
				log.error("error reading image (" + resource.getName() + ")" + " - " + e, e);
			}
		}
		return image;
	}

	public JDinkImage getImage(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName == null");
		}
		JDinkImage image;
		synchronized (imageCacheByFileName) {
			image = (JDinkImage) imageCacheByFileName.get(fileName);
		}
		if (image == null) {
			try {
				Resource resource = this.getFileManager().getResource(fileName);
				if (resource != null) {
					image = getImage(resource);
					if (image != null) {
						synchronized (imageCacheByFileName) {
							imageCacheByFileName.put(fileName, image);
						}
					}
				} else {
					log.error("resource not found: " + fileName);
				}
			} catch (IOException e) {
				log.error("error reading image (" + fileName + ")" + " - " + e, e);
			}
		}
		return image;
	}

	public JDinkImage getImage(JDinkSequence sequence, JDinkSequenceFrame frame) {
		if (frame == null) {
			return null;
		}
		JDinkImage image = frame.getImage();
		if (image == null) {
			String fileName = null;
			try {
				fileName = frame.getFileName();
				if ((fileName != null) && (fileName.length() > 0)) {
					image = getImage(fileName);
					if (image == null) {
						if (log.isDebugEnabled()) {
							log.debug("image could not be loaded:" + fileName);
						}
						return null;
					}
					//sequence.setBackgroundColor(null);
					int width = image.getWidth();
					int height = image.getHeight();
					if (sequence.getBackgroundColor() != ColorConstants.NONE) {
						image = this.getImageLoader().getMaskedImage(image, sequence.getBackgroundColor());
					}
					JDinkRectangle bounds = frame.getBounds();
					if (bounds == null) {
						int offsetX = sequence.getOffsetX();
						int offsetY = sequence.getOffsetY();
						if ((offsetX == 0) && (offsetY == 0)) {
							offsetX = (width - (width / 2) + (width / 6));
							offsetY = (height - (height / 4) - (height / 30));
						}
						bounds = new JDinkRectangle(-offsetX, -offsetY, width, height);
					} else {
						bounds = bounds.getResizedTo(width, height);
					}
					frame.setBounds(bounds);
					frame.setImage(image);
					if (log.isDebugEnabled()) {
						log.debug("image loaded:" + fileName);
					}
				} else {
					log.debug("no file name set");
				}
			} catch (Exception e) {
				log.error("error frame image (" + fileName + ")" + " - " + e, e);
			}
		}
		return image;
	}

//	public void addScript(String name, JDinkScriptFile scriptFile) {
//		scriptFileMap.put(name.toLowerCase(), scriptFile);
//	}

	public JDinkScriptFile getScript(String name, boolean create) {
		name = name.toLowerCase();
		JDinkScriptFile scriptFile;
		synchronized (scriptFileMap) {
			scriptFile = scriptFileMap.get(name);
		}
		if ((scriptFile == null) && (create)) {
			try {
				JDinkCLoader dinkCLoader = new JDinkCLoader();
				dinkCLoader.setContext(this);
				String fileName = "story/" + name + ".c";
				Resource resource = this.getFileManager().getResource(fileName);
				if (resource != null) {
					dinkCLoader = new JDinkCLoader();
				} else {
					fileName = "story/" + name + ".d.c";
					resource = this.getFileManager().getResource(fileName);
					if (resource != null) {
						dinkCLoader = new JDinkCLoader();
					} else {
						fileName = "story/" + name + ".d";
						resource = this.getFileManager().getResource(fileName);
						if (resource != null) {
							dinkCLoader = new JDinkDLoader();
						}
					}
				}
				if (dinkCLoader != null) {
					dinkCLoader.getScriptFile().setFileName(fileName);
					dinkCLoader.load(resource);
					scriptFile = dinkCLoader.getScriptFile();
				} else {
					log.error("script not found: " + name);
				}
			} catch (IOException e) {
				log.error("unable to retrieve script \"" + name + "\" - " + e, e);
			}
			if (scriptFile != null) {
				synchronized (scriptFileMap) {
					scriptFileMap.put(name, scriptFile);
				}
			} else {
				synchronized (scriptFileMap) {
					scriptFileMap.put(name, NULL_SCRIPT_FILE);
				}
			}
		}
		if (scriptFile == NULL_SCRIPT_FILE) {
			scriptFile = null;
		}
		return scriptFile;
	}

	public void addFunction(String name, JDinkFunction function) {
		if (functionMap.get(name) != null) {
			throw new IllegalArgumentException("function " + name + " already registered");
		}
		functionMap.put(name, function);
	}

	public JDinkFunction getFunction(String name) {
		return (JDinkFunction) functionMap.get(name);
	}

	public JDinkFileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager(JDinkFileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void addBrain(int brainNumber, JDinkBrain brain) {
		brainMap.put(brainNumber, brain);
	}

	public JDinkBrain getBrain(int brainNumber) {
		return brainMap.get(brainNumber);
	}

	public void addSound(int soundNumber, JDinkSound sound) {
		soundMap.put(new Integer(soundNumber), sound);
	}

	public JDinkSound getSound(int soundNumber) {
		return (JDinkSound) soundMap.get(Integer.valueOf(soundNumber));
	}

	public Collection<Integer> getSequenceNumbers() {
		List<Integer> result = new ArrayList<Integer>(sequenceMap.keySet());
		Collections.sort(result);
		return result;
	}

	public JDinkSequence getSequence(int sequenceNumber) {
		return getSequence(sequenceNumber, false);
	}

	public JDinkSequence getSequence(int sequenceNumber, boolean create) {
		JDinkSequence sequence = sequenceMap.get(sequenceNumber);
		if ((sequence == null) && (create)) {
			sequence = new JDinkSequence();
			sequenceMap.put(sequenceNumber, sequence);
		}
		return sequence;
	}

	public Map<Integer, JDinkSequenceFrameAttributes> getFrameAttributeMapBySequenceNumber(int sequenceNumber) {
		Map<Integer, JDinkSequenceFrameAttributes> result =
			new HashMap<Integer, JDinkSequenceFrameAttributes>();
		for (Map.Entry<JDinkSequenceFrameKey, JDinkSequenceFrameAttributes> entry: this.frameAttributesMap.entrySet()) {
			if (entry.getKey().getSequenceNumber() == sequenceNumber) {
				result.put(Integer.valueOf(entry.getKey().getFrameNumber()), entry.getValue());
			}
		}
		return result;
	}

	public JDinkSequenceFrameAttributes getFrameAttributes(int sequenceNumber, int frameNumber, boolean create) {
		JDinkSequenceFrameKey key = JDinkSequenceFrameKey.getInstance(sequenceNumber, frameNumber);
		JDinkSequenceFrameAttributes result = this.frameAttributesMap.get(key);
		if ((result == null) && (create)) {
			result = new JDinkSequenceFrameAttributes();
			this.frameAttributesMap.put(key, result);

			// now also set the attributes on the frame if they haven't been set yet
			JDinkSequence sequence = this.getSequence(sequenceNumber, false);
			if ((sequence != null) && (sequence.isLoaded())) {
				JDinkSequenceFrame frame = sequence.getFrame(frameNumber, false);
				if (frame != null) {
					frame.setAttributes(result);
				}
			}
		}
		return result;
	}

	public JDinkPlayer getPlayer(JDinkSprite sprite) {
		JDinkPlayer result;
		if ((this.currentPlayer != null) && (sprite != null) &&
				(this.currentPlayer.getSpriteNumber() == sprite.getSpriteNumber())) {
			result = this.currentPlayer;
		} else {
			result = null;
		}
		return result;
	}

	public void setMetaData(Object key, Object value) {
		if (value != null) {
			this.metaData.put(key, value);
		} else {
			this.metaData.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getMetaData(Object key, Class<T> c) {
		return (T) this.metaData.get(key);
	}

	public JDinkSprite getSprite(int spriteNumber, boolean create) {
		return this.getController().getSprite(spriteNumber, create);
	}


	// plain getters/setters

	public JDinkScope getGlobalScope() {
		return globalScope;
	}

	public void setGlobalScope(JDinkScope globalScope) {
		this.globalScope = globalScope;
	}

	public JDinkController getController() {
		return controller;
	}

	public void setController(JDinkController controller) {
		this.controller = controller;
	}

	/**
	 * @return the configuration
	 */
	public JDinkConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(JDinkConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return the view
	 */
	public JDinkView getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(JDinkView view) {
		this.view = view;
	}

	/**
	 * @return the imageLoader
	 */
	public JDinkImageFactory getImageLoader() {
		return imageLoader;
	}

	/**
	 * @param imageLoader the imageLoader to set
	 */
	public void setImageLoader(JDinkImageFactory imageLoader) {
		this.imageLoader = imageLoader;
	}

	/**
	 * @return the fontColors
	 */
	public int[] getFontColors() {
		return fontColors;
	}

	/**
	 * @param fontColors the fontColors to set
	 */
	public void setFontColors(int[] fontColors) {
		this.fontColors = fontColors;
	}

	/**
	 * @return the hardnessMap
	 */
	public JDinkHardnessMap getHardnessMap() {
		return hardnessMap;
	}

	/**
	 * @return the collisionTest
	 */
	public JDinkCollisionTester getCollisionTester() {
		return collisionTester;
	}

	/**
	 * @param collisionTest the collisionTest to set
	 */
	public void setCollisionTester(JDinkCollisionTester collisionTest) {
		this.collisionTester = collisionTest;
	}

	public JDinkInteractionManager getInteractionManager() {
		return interactionManager;
	}

	public void setInteractionManager(JDinkInteractionManager interactionManager) {
		this.interactionManager = interactionManager;
	}

	public JDinkSpriteHelper getSpriteHelper() {
		return spriteHelper;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getTextBorderColor() {
		return textBorderColor;
	}

	public void setTextBorderColor(int textBorderColor) {
		this.textBorderColor = textBorderColor;
	}

	public Random getRandom() {
		return random;
	}

	public JDinkPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(JDinkPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public JDinkGlobalVariables getGlobalVariables() {
		return globalVariables;
	}

	public ITaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(ITaskManager taskManager) {
		this.taskManager = taskManager;
	}
}
