package de.siteof.jdink.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.brain.JDinkBrain;
import de.siteof.jdink.brain.JDinkPlayerBrain;
import de.siteof.jdink.collision.JDinkCollisionTester;
import de.siteof.jdink.collision.JDinkCollisionTesterImpl;
import de.siteof.jdink.collision.JDinkHardnessMap;
import de.siteof.jdink.collision.JDinkScreenlockCollisionTester;
import de.siteof.jdink.events.JDinkFrameEvent;
import de.siteof.jdink.events.JDinkFrameEventListener;
import de.siteof.jdink.events.JDinkFrameEventListenerList;
import de.siteof.jdink.events.JDinkKeyEventListener;
import de.siteof.jdink.events.JDinkKeyEventListenerList;
import de.siteof.jdink.format.map.JDinkMapEntry;
import de.siteof.jdink.format.map.JDinkMapSpritePlacement;
import de.siteof.jdink.format.map.JDinkMapTile;
import de.siteof.jdink.functions.script.sprite.JDinkSpScriptFunction;
import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.interaction.JDinkInteractionType;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkMapState;
import de.siteof.jdink.model.JDinkMapTransition;
import de.siteof.jdink.model.JDinkMovement;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteLayer;
import de.siteof.jdink.model.JDinkSpriteStore;
import de.siteof.jdink.model.JDinkTile;
import de.siteof.jdink.model.view.JDinkDisplayInformation;
import de.siteof.jdink.model.view.JDinkSpriteDisplayInformation;
import de.siteof.jdink.model.view.JDinkSpriteLayerView;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.status.JDinkStatusManager;
import de.siteof.jdink.util.JDinkSpriteUtil;
import de.siteof.jdink.util.LoggingReentrantReadWriteLock;
import de.siteof.jdink.util.NumberUtil;
import de.siteof.jdink.util.debug.JDinkObjectOutputUtil;
import de.siteof.util.filter.IObjectFilter;

/**
 * <p>Main Dink controller</p>
 */
public class JDinkController {

	private static final Log log = LogFactory.getLog(JDinkController.class);

	public static final int VIEW_CHANGE = 1;
	public static final int CONTROL_CHANGE = 2;
	public static final int ALL_CHANGE = 3;

	private static final IObjectFilter<JDinkSprite> ACTIVE_SPRITE_FILTER =
		new IObjectFilter<JDinkSprite>() {
			@Override
			public boolean matches(JDinkSprite sprite) {
				return sprite.isActive();
			}
	};

	private static final IObjectFilter<JDinkSprite> VISIBLE_SPRITE_FILTER =
		new IObjectFilter<JDinkSprite>() {
			@Override
			public boolean matches(JDinkSprite sprite) {
				return (sprite.isActive()) && (sprite.isVisible());
			}
	};

	private boolean paused;
	private final AtomicBoolean changed = new AtomicBoolean();
	private final JDinkSpriteStore spriteStore;
	private final List<JDinkSpriteLayer> spriteLayers;
	private JDinkSpriteLayer currentSpriteLayer;
	private JDinkPoint mousePosition;

	private int backgroundColorIndex;
	private final Comparator<JDinkSprite> spriteDepthComparator;
	private JDinkSprite[] sortedVisibleSprites;

	private JDinkSprite hoverSprite;

	private final Collection<EventObject> eventObjectList = new ArrayList<EventObject>();
	private EventObject[] events = new EventObject[0];

	private int gameMode;

//	private List<JDinkItem> items = new ArrayList<JDinkItem>();
	private boolean pushActive;

	private int currentMapNumber;
	private JDinkTile[] tiles = new JDinkTile[0];
	private boolean screenLocked;

	private JDinkScriptInstance screenScriptInstance;

	private final AtomicReference<JDinkDisplayInformation> previousDisplayInformation =
		new AtomicReference<JDinkDisplayInformation>();

	private final Map<Integer, Boolean> keyPressedMap = new HashMap<Integer, Boolean>();

//	private final double delayFactor = 0.25d;
	private final double delayFactor = 1.0d;

	private final LoggingReentrantReadWriteLock rwl = new LoggingReentrantReadWriteLock();

	private final JDinkRectangle playerMapBounds = new JDinkRectangle(20, 0, 620 - 20, 400);

	private final AtomicLong lastFrameTime = new AtomicLong(0);

	private final AtomicInteger baseTiming = new AtomicInteger(50);

	private final AtomicBoolean waitingForView = new AtomicBoolean();

	private final JDinkKeyEventListenerList keyEventListenerList =
		new JDinkKeyEventListenerList();

	private final JDinkFrameEventListenerList frameEventListenerList =
		new JDinkFrameEventListenerList();

	public JDinkController() {
		spriteDepthComparator = new Comparator<JDinkSprite>() {
			public int compare(JDinkSprite sprite1, JDinkSprite sprite2) {
				int result = NumberUtil.compareInts(sprite1.getLevel(), sprite2.getLevel());
				if (result == 0) {
					result = NumberUtil.compareInts(sprite1.getLayerNumber(), sprite2.getLayerNumber());
				}
				if (result == 0) {
					result = NumberUtil.compareInts(sprite1.getDepthValue(),
							sprite2.getDepthValue());
				}
				return result;
			}
		};

		spriteStore = new JDinkSpriteStore();

		spriteLayers = new ArrayList<JDinkSpriteLayer>();

		// add a lower level sprite layer
		JDinkSpriteLayer spriteLayer = addNewSpriteLayer();
		spriteLayer.setOpaque(true);

		// add the main sprite layer
		spriteLayer = addNewSpriteLayer();
		spriteLayer.setBounds(playerMapBounds);
	}

	public int getBaseTiming() {
		return baseTiming.get();
	}

	public JDinkSpriteLayer addNewSpriteLayer() {
		int layerNumber = spriteLayers.size();
		if (log.isDebugEnabled()) {
			log.debug("[addNewSpriteLayer] adding sprite layer, layerNumber=" + layerNumber);
		}
		JDinkSpriteLayer spriteLayer = new JDinkSpriteLayer(layerNumber);
		spriteLayers.add(spriteLayer);
		currentSpriteLayer = spriteLayer;
		return spriteLayer;
	}

	public JDinkSpriteLayer getSpriteLayer(int layerNumber) {
		JDinkSpriteLayer result = null;
		for (JDinkSpriteLayer spriteLayer: spriteLayers) {
			if (spriteLayer.getLayerNumber() == layerNumber) {
				result = spriteLayer;
				break;
			}
		}
		return result;
	}

	public JDinkSpriteLayer getCurrentSpriteLayer() {
		return currentSpriteLayer;
	}

	public void setCurrentSpriteLayer(JDinkSpriteLayer spriteLayer) {
		currentSpriteLayer = spriteLayer;
	}

	public void removeSpriteLayer(JDinkSpriteLayer spriteLayer) {
		if (spriteLayer != null) {
			if (log.isDebugEnabled()) {
				log.debug("[removeSpriteLayer] removing sprite layer, layerNumber=" + spriteLayer.getLayerNumber());
			}
			spriteLayers.remove(spriteLayer);
			JDinkSprite[] sprites = spriteStore.getAllocatedSprites();
			for (int i = 0; i < sprites.length; i++) {
				if ((sprites[i] != null) && (sprites[i].getLayerNumber() == spriteLayer.getLayerNumber())) {
					this.releaseSprite(sprites[i].getSpriteNumber());
				}
			}
			if ((currentSpriteLayer != null) && (currentSpriteLayer.getLayerNumber() == spriteLayer.getLayerNumber())) {
				if (!spriteLayers.isEmpty()) {
					currentSpriteLayer = spriteLayers.get(spriteLayers.size() - 1);
				} else {
					currentSpriteLayer = null;
				}
			}
		}
	}

	public long getDelay(long delay) {
		return (long) (this.delayFactor * delay);
	}

	public void setKeyPressed(int keyCode, boolean pressed) {
		keyPressedMap.put(new Integer(keyCode), (pressed ? Boolean.TRUE
				: Boolean.FALSE));
	}

	public boolean isKeyPressed(int keyCode) {
		Boolean pressed = (Boolean) keyPressedMap.get(new Integer(keyCode));
		if (pressed != null) {
			return ((Boolean) pressed).booleanValue();
		} else {
			return false;
		}
	}

//	public JDinkItem allocateItem() {
//		return itemStore.allocateItem();
//	}
//
//	public JDinkItem getItem(int itemNumber) {
//		return itemStore.getItem(itemNumber);
//	}
//
//	public JDinkItem allocateMagicItem() {
//		return magicItemStore.allocateItem();
//	}
//
//	public JDinkItem getMagicItem(int itemNumber) {
//		return magicItemStore.getItem(itemNumber);
//	}

	public void addEvent(EventObject event) {
		synchronized (eventObjectList) {
			eventObjectList.add(event);
		}
	}

	public EventObject[] getEvents() {
		return events;
	}

	public void clearCurrentEvents() {
		this.events = new EventObject[0];
		synchronized (eventObjectList) {
			eventObjectList.clear();
		}
	}

	public void addKeyEventListener(JDinkKeyEventListener listener) {
		this.keyEventListenerList.addListener(listener);
	}

	public void removeKeyEventListener(JDinkKeyEventListener listener) {
		this.keyEventListenerList.removeListener(listener);
	}

	public void addFrameEventListener(JDinkFrameEventListener listener) {
		this.frameEventListenerList.addListener(listener);
	}

	public void removeFrameEventListener(JDinkFrameEventListener listener) {
		this.frameEventListenerList.removeListener(listener);
	}

	public JDinkSprite getSpriteByEditorSpriteNumber(final int editorSpriteNumber) {
		return spriteStore.findSprite(new IObjectFilter<JDinkSprite>() {
			@Override
			public boolean matches(JDinkSprite sprite) {
				return sprite.getEditorSpriteNumber() == editorSpriteNumber;
			}});
	}

	public JDinkSprite getSprite(int spriteNumber, boolean create) {
		JDinkSprite sprite = spriteStore.getSprite(spriteNumber);
		if ((sprite == null) && (create)) {
			sprite = allocateSpriteAt(spriteNumber);
		}
		return sprite;
	}

	protected void processMovements(JDinkContext context, JDinkSprite sprite,
			List<JDinkMovement> movements, JDinkCollisionTester collisionTester) {
		if (!movements.isEmpty()) {
			// JDinkHardnessMap hardnessMap = context.getHardnessMap();
			for (JDinkMovement movement : movements) {
				// TODO process movement
				if (log.isDebugEnabled()) {
					log.debug("movement=" + movement);
				}
			}
		}
	}

	private int getRelativeMapNumber(int mapNumber, int deltaX, int deltaY) {
		return mapNumber + deltaX + (32 * deltaY);
	}

	public JDinkMapTransition getMapTransition(JDinkContext context, JDinkSprite playerSprite) {
		JDinkMapTransition result = null;
		int x = playerSprite.getX();
		int y = playerSprite.getY();
		if (!playerMapBounds.contains(x, y)) {
		    if (playerSprite.getX() < playerMapBounds.getX()) {
		    	// screen to the left
		    	int mapNumber = getRelativeMapNumber(currentMapNumber, -1, 0);
		        if ((!isScreenLocked()) && (context.hasMapEntry(mapNumber))) {
		        	result = new JDinkMapTransition(
		        			currentMapNumber, mapNumber, -1, 0);
		        } else {
		            playerSprite.setX(playerMapBounds.getX());
		        }
		    }
		    if (playerSprite.getY() < playerMapBounds.getY()) {
		    	// screen to the top
		    	int mapNumber = getRelativeMapNumber(currentMapNumber, 0, -1);
		        if ((!isScreenLocked()) && (context.hasMapEntry(mapNumber))) {
		        	result = new JDinkMapTransition(
		        			currentMapNumber, mapNumber, 0, -1);
		        } else {
		            playerSprite.setX(playerMapBounds.getY());
		        }
		    }
		    if (playerSprite.getX() >= playerMapBounds.getX() + playerMapBounds.getWidth()) {
		    	// screen to the right
		    	int mapNumber = getRelativeMapNumber(currentMapNumber, 1, 0);
		        if ((!isScreenLocked()) && (context.hasMapEntry(mapNumber))) {
		        	result = new JDinkMapTransition(
		        			currentMapNumber, mapNumber, 1, 0);
		        } else {
		            playerSprite.setX(playerMapBounds.getX() + playerMapBounds.getWidth() - 1);
		        }
		    }
		    if (playerSprite.getY() >= playerMapBounds.getY() + playerMapBounds.getHeight()) {
		    	// screen to the bottom
		    	int mapNumber = getRelativeMapNumber(currentMapNumber, 0, 1);
		        if ((!isScreenLocked()) && (context.hasMapEntry(mapNumber))) {
		        	result = new JDinkMapTransition(
		        			currentMapNumber, mapNumber, 0, 1);
		        } else {
		            playerSprite.setX(playerMapBounds.getY() + playerMapBounds.getHeight() - 1);
		        }
		    }
		}
		return result;
	}

	public void setMapTransition(JDinkContext context, JDinkSprite playerSprite, JDinkMapTransition transition) {
		context.getGlobalVariables().playerMap.setInt(context, transition.getToMapNumber());
		if (transition.isImmediateLeft()) {
			playerSprite.setX(playerMapBounds.getX() + playerMapBounds.getWidth() - 1);
		}
		if (transition.isImmediateTop()) {
			playerSprite.setY(playerMapBounds.getY() + playerMapBounds.getHeight() - 1);
		}
		if (transition.isImmediateRight()) {
			playerSprite.setX(playerMapBounds.getX());
		}
		if (transition.isImmediateBottom()) {
			playerSprite.setY(playerMapBounds.getY());
		}
	}

	public void warpTo(JDinkContext context, JDinkSprite sprite, int mapNumber, int x, int y) {
		sprite.setX(x);
		sprite.setY(y);
		context.getGlobalVariables().playerMap.setInt(context, mapNumber);
	}

	private void processAnimation(JDinkContext context, JDinkSprite sprite) {
		// TODO handle player
		boolean changed = false;
		JDinkSequence sequence = context.getSequence(sprite.getAnimationSequenceNumber(), false);
		JDinkMapSpritePlacement spritePlacement = sprite.getSpritePlacement();
		if ((sprite.getAnimationSequenceNumber() > 0) &&
				(sequence != null) && (sequence.getFrameCount() > 0) &&
				((spritePlacement == null) || (spritePlacement.getWarpMap() == 0)) &&
				(sprite.getNextAnimationTime() < context.getTime())) {
			float animationTimingFactor = 2.0f;
			if (sprite.getAnimationSequenceNumber() != sprite.getSequenceNumber()) {
				sprite.setSequenceNumber(sprite.getAnimationSequenceNumber());
				sprite.setSequence(context.getSequence(sprite.getSequenceNumber(), false));
				changed = true;
			}
			boolean newFrame = false;
			int animationFrameNumber = sprite.getAnimationFrameNumber();
			if (animationFrameNumber <= 0) {
				// new animation
				if (sprite.isReverse()) {
					animationFrameNumber = sequence.getLastFrameNumber();
				} else {
					animationFrameNumber = sequence.getFirstFrameNumber();
				}
				sprite.setAnimationFrameNumber(animationFrameNumber);
				sprite.setFrameNumber(animationFrameNumber);
				newFrame = true;
				changed = true;
			} else {
				long nextAnimationTime = sprite.getNextAnimationTime();
				if (nextAnimationTime == 0) {
					// not started by the above block
					if ((sprite.getSpriteNumber() == 1) && (sprite.getAnimationSequenceNumber() > 50)) {
						log.info("[processAnimation] start of animation - s=" + sprite.getAnimationSequenceNumber() +
								", f=" + sprite.getAnimationFrameNumber() +
								", r=" + sprite.isReverse() +
								", ar=" + sprite.isAutoReverse());
					}
					sprite.setFrameNumber(animationFrameNumber);
					if (sprite.getAnimationFrameNumber() == sequence.getFirstFrameNumber()) {
						sprite.setReverse(false);
					}
					sprite.setAutoReverse(false);
					newFrame = true;
					changed = true;
				} else if (nextAnimationTime < context.getTime()) {
					boolean end;
					if (sprite.isReverse()) {
						end = (animationFrameNumber == sequence.getFirstFrameNumber());
						animationFrameNumber = sequence.getPreviousFrameNumber(animationFrameNumber);
					} else {
						end = (animationFrameNumber >= sequence.getLastFrameNumber());
						animationFrameNumber = sequence.getNextFrameNumber(animationFrameNumber);
					}
					if (end) {
//						if (sequence.isAnimation()) {
//						if ((sequence.isAnimation()) && (!sprite.isReverse()) && (sprite.isAutoReverse()) && (false)) {
//							// for some reasons reverse also means no animation
//							if (sprite.isAutoReverse()) {
//								if (sprite.isReverse()) {
//									animationFrameNumber = sequence.getNextFrameNumber(
//											sequence.getFirstFrameNumber());
//								} else {
//									animationFrameNumber = sequence.getPreviousFrameNumber(
//											sequence.getLastFrameNumber());
//								}
//								sprite.setReverse(!sprite.isReverse());
//							} else {
//								if (sprite.isReverse()) {
//									animationFrameNumber = sequence.getFrameCount();
//								} else {
//									animationFrameNumber = 1;
//								}
//							}
//							sprite.setAnimationFrameNumber(animationFrameNumber);
//							sprite.setFrameNumber(animationFrameNumber);
//						} else {
							if ((sprite.getSpriteNumber() == 1) && (sprite.getAnimationSequenceNumber() > 50)) {
								log.info("[processAnimation] end of animation - s=" + sprite.getAnimationSequenceNumber() +
										", f=" + sprite.getAnimationFrameNumber() +
										", r=" + sprite.isReverse());
							}
							// the animation is not played back on reverse because
							// we want to end animations like the hit one
							sprite.setAnimationFrameNumber(0);
							sprite.setOriginalAnimationSequenceNumber(sprite.getAnimationSequenceNumber());
							sprite.setAnimationSequenceNumber(0);
							sprite.setReverse(false);
							sprite.setNoControl(false);

							JDinkBrain brain = sprite.getBrain();
							if (brain instanceof JDinkPlayerBrain) {
								((JDinkPlayerBrain) brain).onAnimationSequenceEnd(context, sprite);
							}
//						}

//						if ((sequence.isAnimation()) && (!sprite.isReverse())) {
//							// for some reasons reverse also means no animation
//							if (sprite.isReverse()) {
//								animationFrameNumber = 2;
//							} else {
//								animationFrameNumber = sequence.getFrameCount() - 2;
//							}
//							sprite.setAnimationFrameNumber(animationFrameNumber);
//							sprite.setFrameNumber(animationFrameNumber);
//							sprite.setReverse(!sprite.isReverse());
//						} else {
//							// revert the animation number to what it currently is
//							sprite.setAnimationFrameNumber(0);
//							sprite.setOriginalAnimationSequenceNumber(sprite.getAnimationSequenceNumber());
//							sprite.setAnimationSequenceNumber(0);
//							sprite.setReverse(!sprite.isReverse());
//							// TODO nocontrol?
//						}

						//						sprite.setInitialSequenceNumber(0);
					} else {
						sprite.setAnimationFrameNumber(animationFrameNumber);
						sprite.setFrameNumber(animationFrameNumber);
					}
					newFrame = true;
				}
			}
			if ((sprite.getAnimationSequenceNumber() > 0) && (newFrame)) {
				JDinkSequenceFrameAttributes attributes =
					context.getFrameAttributes(sprite.getAnimationSequenceNumber(),
							sprite.getAnimationFrameNumber(), false);
				int delay = (int) (JDinkSpriteUtil.getEffectiveFrameDelay(context, sprite) * animationTimingFactor);
				sprite.setNextAnimationTime(context.getTime() + delay);
				changed = true;
				if (sprite.getFrame() == null) {
					log.warn("[processAnimation] frame not found");
				}

				if ((attributes != null) && (attributes.isSpecial())) {
					if (!context.getInteractionManager().interact(
						JDinkInteractionType.HIT, context, sprite)) {
					}
				}
			}
		}
		if (changed) {
			this.setChanged(changed);
		}
	}

	private void doEndFrame(JDinkContext context) {
		// Date now = new Date();
		List<JDinkSprite> sprites = context.getController().getActiveSprites();
		JDinkCollisionTester collisionTester = new JDinkCollisionTesterImpl(
				context.getHardnessMap(), sprites);
		if (this.isScreenLocked()) {
			collisionTester = new JDinkScreenlockCollisionTester(collisionTester);
		}
		context.setCollisionTester(collisionTester);
		List<JDinkMovement> movements = new ArrayList<JDinkMovement>();
		int currentSpriteLayer = this.getCurrentSpriteLayerNumber();
		for (JDinkSprite sprite: sprites) {
			// if ((sprite.getText() != null) && (sprite.isTextExpiredBy(now)))
			// {
			// sprite.setText(null);
			// sprite.setTextExpireDate(null);
			// }
			if (sprite.getLayerNumber() == currentSpriteLayer) {
				if (sprite.getBrain() != null) {
					sprite.getBrain().update(context, sprite);
				}
			}
			processAnimation(context, sprite);
			sprite.pollMovements(movements);
			processMovements(context, sprite, movements, collisionTester);
		}

		JDinkStatusManager statusManager = JDinkStatusManager.getInstance(context);
		if (statusManager != null) {
			if (statusManager.update(context)) {
				this.setChanged(true);
			}
		}
		frameEventListenerList.fireEvent(context, new JDinkFrameEvent(this, JDinkFrameEvent.END_FRAME));
	}

	protected void fillHardness(JDinkContext context, JDinkRectangle clipRectangle) {
		JDinkHardnessMap hardnessMap = context.getHardnessMap();
		JDinkTile[] tiles = this.getTiles();
		JDinkRectangle hardnessMapRectangle = new JDinkRectangle(hardnessMap.getX(),
				hardnessMap.getY(), hardnessMap.getWidth(), hardnessMap
						.getHeight());
		if (clipRectangle == null) {
			clipRectangle = hardnessMapRectangle;
		} else {
			clipRectangle = clipRectangle.intersection(hardnessMapRectangle);
		}
		JDinkRectangle tileRectangle = new JDinkRectangle(0, 0, 50, 50);
		for (int tileIndex = 0; tileIndex < tiles.length; tileIndex++) {
			JDinkTile tile = tiles[tileIndex];
			tileRectangle = tileRectangle.getLocatedTo(tile.getX(), tile.getY());
			if ((clipRectangle == null)
					|| (tileRectangle.intersects(clipRectangle))) {
				JDinkRectangle intersection = tileRectangle;
				if (clipRectangle != null) {
					intersection = tileRectangle.intersection(clipRectangle);
				}
				hardnessMap.copyHardnessFrom(tile.getHardness(),
						intersection.getX()	- tileRectangle.getX(),
						intersection.getY() - tileRectangle.getY(),
						intersection.getX(),
						intersection.getY(),
						intersection.getWidth(),
						intersection.getHeight());
			}
		}
	}

	private void setVision(JDinkContext context, int vision) {
		context.getGlobalVariables().vision.setInt(context, vision);
	}

	public int getVision(JDinkContext context) {
		int vision = context.getGlobalVariables().vision.getInt(context);
		return vision;
	}

	private void serializeMapEntry(int mapNumber, JDinkMapEntry mapEntry) {
		JDinkObjectOutputUtil.writeObject("map-" + mapNumber, mapEntry);
	}

	private void detachVariables(JDinkContext context) {
		context.getGlobalVariables().detach();
	}

	public void clearState(JDinkContext context) {
		List<JDinkSprite> sprites = this.getAllocatedNormalSprites();
		// remove all of the
		for (JDinkSprite sprite: sprites) {
			if (sprite.getSpriteNumber() != 1) {
				this.releaseSprite(sprite);
			}
		}
		JDinkScope globalScope = context.getGlobalScope();
		globalScope.clear();
		detachVariables(context);
		context.clearMapState();
		this.currentMapNumber = -1;
	}

	private void loadMap(JDinkContext context, int mapNumber) {
		currentMapNumber = mapNumber;
		if (log.isDebugEnabled()) {
			log.debug("new map number: " + currentMapNumber);
		}
		try {
			JDinkMapEntry mapEntry = context.getMapEntry(currentMapNumber);
			if (mapEntry == null) {
				log.error("error loading map entry: " + currentMapNumber);
				return;
			}
			if (log.isDebugEnabled()) {
				serializeMapEntry(currentMapNumber, mapEntry);
			}
			JDinkTile[] tiles = new JDinkTile[97];
			int playl = playerMapBounds.getX();
			for (int i = 0; i < tiles.length; i++) {
				JDinkTile tile = new JDinkTile();
				JDinkMapTile mapTile = mapEntry.getTiles()[i];
				byte[][] hardness = context.getMapTileHardness(mapTile);
				int tileSetNumber = mapTile.getNum() / 128;
				int pa = mapTile.getNum() - (tileSetNumber * 128);
				tile.setSourceRectangle(new JDinkRectangle(
								(pa * 50 - (pa / 12) * 600),
								(pa / 12) * 50, 50, 50));
				tile.setX((i * 50 - ((i / 12) * 600)) + playl);
				tile.setY((i / 12) * 50);
				tile
						.setTileSet(context.getTileSet(tileSetNumber + 1,
								true));
				tile.setHardness(hardness);
				tiles[i] = tile;
			}

			//spriteStore.releaseSpritesFrom(2);
			for (JDinkSprite sprite: spriteStore.getAllocatedSprites()) {
				if ((sprite.getSpriteNumber() != 1) && (sprite.getLayerNumber() > 0)) {
					spriteStore.releaseSprite(sprite.getSpriteNumber());
				}
			}

			this.setTiles(tiles);

			fillHardness(context, null);

			// reset vision (each map script may change the vision)
			setVision(context, 0);

			JDinkMapState mapState = context.getMapState(mapNumber);

			List<JDinkScriptInstance> scriptInstances = new LinkedList<JDinkScriptInstance>();

			this.loadSprites(context, scriptInstances, mapEntry.getSpritePlacements(), mapState, 0);

//			this.waitForView(context);

			setScreenScriptInstance(null);
			if ((mapEntry.getScriptName() != null)
					&& (mapEntry.getScriptName().length() > 0)) {
				JDinkScriptFile scriptFile = context.getScript(mapEntry
						.getScriptName(), true);
				if (scriptFile != null) {
					JDinkScriptInstance scriptInstance = new JDinkScriptInstance();
					scriptInstance.setScriptFile(scriptFile);
					JDinkScope scope = new JDinkScope(context
							.getGlobalScope());
					scriptInstance.setScope(scope);
					try {
						scriptInstance.initialize(context);
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					setScreenScriptInstance(scriptInstance);
				}
			}

			int vision = this.getVision(context);

			if (vision != 0) {
				this.loadSprites(context, scriptInstances, mapEntry.getSpritePlacements(), mapState, vision);
			}

			this.waitForView(context);

			for (JDinkScriptInstance scriptInstance: scriptInstances) {
				try {
					if (log.isInfoEnabled()) {
						log.info("initialising (main): " +
								scriptInstance.getScriptFile().getFileName());
					}
					scriptInstance.initialize(context);
				} catch (Throwable e) {
					log.error("failed to initialize script due to " + e, e);
				}
			}

			this.setChanged(true);
			// context.getImage()
		} catch (Exception e) {
			log.error("begin frame failed - " + e, e);
		}
	}

	private void loadSprites(
			JDinkContext context,
			List<JDinkScriptInstance> scriptInstances,
			JDinkMapSpritePlacement[] spritePlacements,
			JDinkMapState mapState,
			int vision) {
		long now = System.currentTimeMillis();

		if (mapState == null) {
			mapState = JDinkMapState.EMPTY_MAP_STATE;
		}

		for (int i = 0; i < spritePlacements.length; i++) {
			JDinkMapSpritePlacement spritePlacement = spritePlacements[i];
			int editorSpriteNumber = i;
			int editorSpriteState = mapState.getEditorSpriteState(editorSpriteNumber);
			int editorSequenceNumber = mapState.getEditorSequenceNumber(editorSpriteNumber);
			int editorFrameNumber = mapState.getEditorFrameNumber(editorSpriteNumber);
			int level = 0;
			if (spritePlacement.getType() == JDinkMapSpritePlacement.INVISIBLE_1_TYPE) {
				level = -1;
			}
			@SuppressWarnings("unused")
			boolean hardnessEnabled = true;
			switch (editorSpriteState) {
			case JDinkMapState.DEFAULT_STATE:
				break;
			case JDinkMapState.KILL_PERMANENTLY_STATE:
				continue;
			case JDinkMapState.FUTURE_DRAW_NORMAL_WITHOUT_HARDNESS_STATE:
				hardnessEnabled = false;
				break;
			case JDinkMapState.FUTURE_DRAW_BACKGROUND_WITHOUT_HARDNESS_STATE:
				level = -1;
				hardnessEnabled = false;
				break;
			case JDinkMapState.FUTURE_DRAW_NORMAL_WITH_HARDNESS_STATE:
				break;
			case JDinkMapState.FUTURE_DRAW_BACKGROUND_WITH_HARDNESS_STATE:
				level = -1;
				break;
			case JDinkMapState.RETURN_IN_5_MINUTES_STATE:
				if (mapState.getLastTime() + (5 * 60 * 100) > now) {
					continue;
				}
				break;
			case JDinkMapState.RETURN_IN_3_MINUTES_STATE:
				if (mapState.getLastTime() + (3 * 60 * 100) > now) {
					continue;
				}
				break;
			case JDinkMapState.RETURN_IN_1_MINUTE_STATE:
				if (mapState.getLastTime() + (1 * 60 * 100) > now) {
					continue;
				}
				break;
			}
			if (spritePlacement != null) {
				boolean onThisVision = ((spritePlacement.getVision() == 0) ||
						(spritePlacement.getVision() == vision));
				if ((spritePlacement.isActive()) && (onThisVision)) {
					int spriteNumber = this.allocateSprite();
					JDinkSprite sprite = this.getSprite(spriteNumber,
							false);
					if (sprite != null) {
						sprite.setX(spritePlacement.getX());
						sprite.setY(spritePlacement.getY());
						sprite.setSize(spritePlacement.getSize());
						sprite.setBrainNumber(spritePlacement
								.getBrainNumber());
						sprite.setBrain(context.getBrain(sprite
								.getBrainNumber()));
						sprite.setAnimationSequenceNumber(spritePlacement
								.getParamSeq());
						sprite.setSequenceNumber(spritePlacement
								.getSequenceNumber());
						sprite.setFrameNumber(spritePlacement
								.getFrameNumber());
						if (editorSequenceNumber > 0) {
							sprite.setAnimationSequenceNumber(editorSequenceNumber);
							sprite.setSequenceNumber(editorSequenceNumber);
						}
						if (editorFrameNumber > 0) {
							sprite.setAnimationFrameNumber(editorFrameNumber);
							sprite.setFrameNumber(editorFrameNumber);
						}
						sprite.setSequence(context.getSequence(sprite
								.getSequenceNumber(), false));
						sprite.setBaseWalk(spritePlacement
								.getBaseWalk());
						sprite.setBaseIdle(spritePlacement
								.getBaseIdle());
						sprite.setBaseAttack(spritePlacement
								.getBaseAttack());
						sprite.setBaseHit(spritePlacement
								.getBaseHit());
						sprite.setSpeed(spritePlacement.getSpeed());
						sprite.setDepthHint(spritePlacement.getQue());
						sprite.setCollisionType(spritePlacement.getHard());
						sprite.setEditorSpriteNumber(editorSpriteNumber);
						sprite.setSpritePlacement(spritePlacement);
						sprite.setFrameDelay(spritePlacement.getTimer());
						sprite.setHitPoints(spritePlacement.getHitPoints());
						sprite.setLevel(level);
						if ((spritePlacement.getAltX1() != spritePlacement.getAltX2()) &&
								(spritePlacement.getAltY1() != spritePlacement.getAltY2())) {
							sprite.setClipShape(JDinkRectangle.between(
									spritePlacement.getAltX1(), spritePlacement.getAltY1(),
									spritePlacement.getAltX2(), spritePlacement.getAltY2()));
						}
						if (spritePlacement.getType() == JDinkMapSpritePlacement.INVISIBLE_2_TYPE) {
							// invisible object
							sprite.setVisible(false);
						} else if ((spritePlacement.getScriptName() != null)
								&& (spritePlacement.getScriptName()
										.length() > 0)) {
							if (spritePlacement.getType() != JDinkMapSpritePlacement.PLAYER_CREATURE_TYPE) {
								log.warn("unexpected sprite type: "
										+ spritePlacement.getType());
							}
							JDinkScope spriteScope = sprite
									.requestScope(context);
							JDinkScriptFile scriptFile = context
									.getScript(spritePlacement
											.getScriptName(), true);
							if (scriptFile != null) {
								JDinkScriptInstance scriptInstance = new JDinkScriptInstance();
								scriptInstance
										.setScriptFile(scriptFile);
								JDinkScope scope = new JDinkScope(
										spriteScope);
								scriptInstance.setScope(scope);
								scope
										.setVariableValue(
												JDinkSpScriptFunction.CURRENT_SPRITE_VARNAME,
												new Integer(
														spriteNumber));
								if (log.isInfoEnabled()) {
									log.info("queueuing script for initialisation (main): " +
											scriptFile.getFileName());
								}
//								scriptInstances.add(scriptInstance);
								// TODO this may need to be called after initialize
								sprite.setScriptInstance(scriptInstance);
								scriptInstances.add(scriptInstance);
							}
						}
					}
				}
			}
		}
	}

	private void doBeginFrame(JDinkContext context) {
		synchronized (eventObjectList) {
			events = (EventObject[]) eventObjectList
					.toArray(new EventObject[eventObjectList.size()]);
			eventObjectList.clear();
		}
		frameEventListenerList.fireEvent(context, new JDinkFrameEvent(this, JDinkFrameEvent.BEGIN_FRAME));
		if (this.getGameMode() == 2) {
			int playerMap = context.getGlobalVariables().playerMap.getInt(context);
			if ((playerMap > 0) && (playerMap != currentMapNumber)) {
				loadMap(context, playerMap);
			}
		}
	}

	public void invokeLater(Runnable runnable) {
		invokeAndWait(runnable);
	}

	private void lock(Lock lock) {
		rwl.lock(lock);
	}

	public void invokeAndWait(Runnable runnable) {
		lock(rwl.writeLock());
		try {
			runnable.run();
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public void invokeReadOnlyLater(Runnable runnable) {
		invokeReadOnlyAndWait(runnable);
	}

	public void invokeReadOnlyAndWait(Runnable runnable) {
		lock(rwl.readLock());
		try {
			runnable.run();
		} finally {
			rwl.readLock().unlock();
		}
	}

	public boolean isRunning() {
//		return this.runningCount.get() > 0;
		return rwl.isWriteLocked();
	}

	public boolean nextFrame(JDinkContext context) {
		boolean changed;
		lock(rwl.writeLock());
		try {
			if (!waitingForView.get()) {
				long now = System.currentTimeMillis();
				long previousFrameTime = lastFrameTime.getAndSet(now);
				if (previousFrameTime > 0) {
					long delta = now - previousFrameTime;
					this.baseTiming.set((int) Math.max(4, Math.max(12, Math.min(63, delta)) / 3 / 5));
				} else {
					this.baseTiming.set(50);
				}
				if (!isPaused()) {
					doBeginFrame(context);
					doEndFrame(context);
					if (this.isChanged()) {
						sortedVisibleSprites = null;
					}
				}
				changed = this.isChanged();
			} else {
				changed = false;
			}
		} finally {
			rwl.writeLock().unlock();
		}
		return changed;
	}

	public JDinkDisplayInformation getDisplayInformation(JDinkContext context) {
		JDinkDisplayInformation result = previousDisplayInformation.get();
		boolean lockAquired = false;
		try {
			if (result != null) {
				try {
					lockAquired = rwl.readLock().tryLock(100, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					log.info("[getDisplayInformation] tryLock interrupted");
				}
			} else {
				// we do need to wait
				lock(rwl.readLock());
				lockAquired = true;
			}
			if (lockAquired) {
				result = new JDinkDisplayInformation(
						this.getBackgroundColorIndex(),
						this.getTiles(),
						playerMapBounds,
						getSpriteDisplayInformationList(),
						getSpriteLayerViewInformationList());
				previousDisplayInformation.set(result);
			}
		} finally {
			if (lockAquired) {
				rwl.readLock().unlock();
			}
		}
		return result;
	}

	private List<JDinkSpriteDisplayInformation> getSpriteDisplayInformationList() {
		JDinkSprite[] sprites = getSortedVisibleSprites();
		Map<Integer, JDinkSpriteDisplayInformation> spriteDisplayInformationMap = new HashMap<Integer, JDinkSpriteDisplayInformation>(
				sprites.length);
		List<JDinkSpriteDisplayInformation> spriteDisplayInformationList =
				new ArrayList<JDinkSpriteDisplayInformation>(sprites.length);
		for (JDinkSprite sprite : sprites) {
			spriteDisplayInformationList.add(JDinkSpriteDisplayInformation.forSprite(sprite,
					spriteDisplayInformationMap));
		}
		return spriteDisplayInformationList;
	}

	private Map<Integer, JDinkSpriteLayerView> getSpriteLayerViewInformationList() {
		List<JDinkSpriteLayer> spriteLayers = this.spriteLayers;
		Map<Integer, JDinkSpriteLayerView> spriteLayerMap =
			new HashMap<Integer, JDinkSpriteLayerView>(spriteLayers.size());
		for (JDinkSpriteLayer spriteLayer: spriteLayers) {
			spriteLayerMap.put(Integer.valueOf(spriteLayer.getLayerNumber()),
					new JDinkSpriteLayerView(spriteLayer));
		}
		return spriteLayerMap;
	}

	public void sleep(long length) {
		Object sleep = new Object();
		synchronized (sleep) {
			this.waitFor(sleep, length);
		}
		/*
		if (this.isChanged()) {
			sortedVisibleSprites = null;
		}
		this.events = new EventObject[0];
		boolean lockHeld = false;
		try {
			try {
				this.runningCount.decrementAndGet();
				rwl.writeLock().unlock();
				lockHeld = true;
			} catch (IllegalMonitorStateException e) {
				log.warn("write lock not held", e);
			}
			log.debug("script sleep");
			try {
				Thread.sleep(length);
			} catch (InterruptedException e) {
				log.warn("sleep interrupted - " + e, e);
			}
		} finally {
			this.runningCount.incrementAndGet();
			if (lockHeld) {
				rwl.writeLock().lock();
			}
		}
		*/
	}

	public void waitFor(Object o, long length) {
		if (this.isChanged()) {
			sortedVisibleSprites = null;
		}
		this.events = new EventObject[0];
		boolean lockHeld = false;
		try {
			try {
				rwl.writeLock().unlock();
				lockHeld = true;
			} catch (IllegalMonitorStateException e) {
				log.warn("write lock not held", e);
			}
			log.debug("script waitFor");
			try {
				if (length >= 0) {
					o.wait(length);
				} else {
					o.wait();
				}
			} catch (InterruptedException e) {
				log.warn("wait interrupted - " + e, e);
			}
		} finally {
			if (lockHeld) {
				lock(rwl.writeLock());
			}
		}
	}

	public void waitForView(JDinkContext context) {
		waitForView(context, 5000);
	}

	public void waitForView(JDinkContext context, long length) {
		previousDisplayInformation.set(null);
		this.setChanged(true);
		boolean lockHeld = false;
		try {
			waitingForView.getAndSet(true);
			try {
				rwl.writeLock().unlock();
				lockHeld = true;
			} catch (IllegalMonitorStateException e) {
				log.warn("write lock not held", e);
			}
			log.debug("waitForView");
			context.getView().waitForView(length);
		} finally {
			if (lockHeld) {
				lock(rwl.writeLock());
			}
			waitingForView.set(false);
		}
	}

	private JDinkSprite[] getVisibleSprites() {
		return spriteStore.getAllocatedSprites(VISIBLE_SPRITE_FILTER);
	}

	public JDinkSprite[] getSortedVisibleSprites() {
		if (sortedVisibleSprites != null) {
			return sortedVisibleSprites;
		}
		JDinkSprite[] visibleSprites = this.getVisibleSprites();
		if (visibleSprites.length >= 2) {
			Arrays.sort(visibleSprites, spriteDepthComparator);
		}
		sortedVisibleSprites = visibleSprites;
		return sortedVisibleSprites;
	}

	public List<JDinkSprite> getActiveSprites() {
		return spriteStore.getAllocatedSpritesList(ACTIVE_SPRITE_FILTER);
	}

	public List<JDinkSprite> getActiveNormalSprites() {
		JDinkSpriteLayer spriteLayer = this.getCurrentSpriteLayer();
		final int spriteLayerNumber = (spriteLayer != null ? spriteLayer.getLayerNumber() : 0);
		return spriteStore.getAllocatedSpritesList(new IObjectFilter<JDinkSprite>() {
			@Override
			public boolean matches(JDinkSprite sprite) {
				return (sprite.isActive()) && (sprite.getLayerNumber() == spriteLayerNumber);
			}});
	}

	public List<JDinkSprite> getAllocatedNormalSprites() {
		JDinkSpriteLayer spriteLayer = this.getCurrentSpriteLayer();
		final int spriteLayerNumber = (spriteLayer != null ? spriteLayer.getLayerNumber() : 0);
		return spriteStore.getAllocatedSpritesList(new IObjectFilter<JDinkSprite>() {
			@Override
			public boolean matches(JDinkSprite sprite) {
				return (sprite.getLayerNumber() == spriteLayerNumber);
			}});
	}

	public int getCurrentSpriteLayerNumber() {
		return (currentSpriteLayer != null ? currentSpriteLayer.getLayerNumber() : 0);
	}

	public JDinkSprite allocateSpriteAt(int spriteNumber) {
		JDinkSprite sprite = new JDinkSprite(
				this.getCurrentSpriteLayerNumber(), spriteNumber);
		spriteStore.setSprite(spriteNumber, sprite);
		this.setChanged(true);
		return sprite;
	}

	public int allocateSprite() {
		int spriteNumber = spriteStore.getNextFreeSpriteNumber();
		if (spriteNumber >= 1000) {
			log.warn("[allocateSprite] allocating sprite above sprite number 1000");
		}
		allocateSpriteAt(spriteNumber);
		return spriteNumber;
	}

	public void releaseSprite(JDinkSprite sprite) {
		releaseSprite(sprite.getSpriteNumber());
	}

	public void releaseSprite(int spriteNumber) {
		if (spriteStore.releaseSprite(spriteNumber)) {
			this.setChanged(true);
		}
	}

	public void notifyChanged(JDinkSprite sprite, int changeType) {
		this.setChanged(true);
	}

	public int getBackgroundColorIndex() {
		return backgroundColorIndex;
	}

	public void setBackgroundColorIndex(int backgroundColorIndex) {
		this.backgroundColorIndex = backgroundColorIndex;
	}

	public boolean isChanged() {
		return changed.get();
	}

	public void setChanged(boolean changed) {
		this.changed.set(changed);
	}

	public JDinkPoint getMousePosition() {
		return mousePosition;
	}

	public void setMousePosition(JDinkPoint mousePosition) {
		this.mousePosition = mousePosition;
	}

	public JDinkSprite getHoverSprite() {
		return hoverSprite;
	}

	public void setHoverSprite(JDinkSprite hoverSprite) {
		this.hoverSprite = hoverSprite;
	}

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public boolean isPushActive() {
		return pushActive;
	}

	public void setPushActive(boolean pushActive) {
		this.pushActive = pushActive;
	}

	public JDinkTile[] getTiles() {
		return tiles;
	}

	public void setTiles(JDinkTile[] tiles) {
		this.tiles = tiles;
	}

	public JDinkScriptInstance getScreenScriptInstance() {
		return screenScriptInstance;
	}

	public void setScreenScriptInstance(JDinkScriptInstance screenScriptInstance) {
		this.screenScriptInstance = screenScriptInstance;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isScreenLocked() {
		return screenLocked;
	}

	public void setScreenLocked(boolean screenLocked) {
		this.screenLocked = screenLocked;
	}

	public int getCurrentMapNumber() {
		return currentMapNumber;
	}

	public JDinkRectangle getPlayerMapBounds() {
		return playerMapBounds;
	}
}
