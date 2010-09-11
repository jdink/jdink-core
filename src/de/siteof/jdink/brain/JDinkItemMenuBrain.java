package de.siteof.jdink.brain;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.events.JDinkKeyConstants;
import de.siteof.jdink.events.JDinkKeyEvent;
import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.model.JDinkSpriteLayer;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkVariable;

/**
 * <p>Handles the item menu.</p>
 * <p>This brain isn't assigned to any brain number.</p>
 */
public class JDinkItemMenuBrain extends AbstractJDinkBrain {

	private static final Log log = LogFactory.getLog(JDinkItemMenuBrain.class);

	private final static String ITEM_MENU_LAYER_KEY = "brain.item.menu.layer";

	public final static String CURRENT_ITEM_VAR_NAME = "brain.item.current";
//	public final static String CHOICES_RESULT_VAR_NAME = "brain.choice.result";

	private static JDinkItemMenuBrain instance = new JDinkItemMenuBrain();

	public static JDinkItemMenuBrain getInstance() {
		return instance;
	}

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		this.processKeyEvents(context, sprite);
	}

	private int getCurrentItem(JDinkContext context, JDinkSprite sprite) {
		JDinkScope spriteScope = requestSpriteScope(context, sprite);
		Object currentItemValue = spriteScope.getInternalVariableValue(
				CURRENT_ITEM_VAR_NAME);
		int result;
		if (currentItemValue instanceof Integer) {
			result = ((Integer) currentItemValue).intValue();
		} else {
			log.warn("integer variable expected");
			result = 0;
		}
		return result;
	}

	private void setCurrentItem(JDinkContext context, JDinkSprite sprite, int currentItem) {
		JDinkScope spriteScope = requestSpriteScope(context, sprite);
		spriteScope.setInternalVariableValue(
				CURRENT_ITEM_VAR_NAME, Integer.valueOf(currentItem));
	}


	private JDinkPoint getItemLocation(int index, boolean magic) {
		int columns;
		int x;
		int y = 83;
		int width = 18 + 65;
		int height = 20 + 55;
		if (magic) {
			columns = 2;
			x = 45;
		} else {
			columns = 4;
			x = 260;
		}
		int column = index % columns;
		int row = index / columns;
		x += column * width;
		y += row * height;
		return new JDinkPoint(x, y);
	}

	private JDinkSprite addItem(JDinkContext context,
			int index, boolean magic, JDinkItem item) {
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		JDinkPoint location = this.getItemLocation(index, magic);
		return spriteHelper.newSpriteAbsolute(
				location.getX(), location.getY(),
				item.getSequenceNumber(), item.getFrameNumber(), false);
	}

	public void hideItemMenu(JDinkContext context) {
		JDinkSpriteLayer spriteLayer = context.getMetaData(
				ITEM_MENU_LAYER_KEY, JDinkSpriteLayer.class);
		if (spriteLayer != null) {
			context.setMetaData(ITEM_MENU_LAYER_KEY, null);
			context.getController().removeSpriteLayer(spriteLayer);
		}
	}

	public void showItemMenu(JDinkContext context) {
		hideItemMenu(context);
		JDinkController controller = context.getController();
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		JDinkSpriteLayer spriteLayer = controller.addNewSpriteLayer();
		context.setMetaData(ITEM_MENU_LAYER_KEY, spriteLayer);

		int x = 20;
		int y = 0;

//		JDinkItemStore itemStore = controller.getItemStore();
//		JDinkItem[] items = controller.getItems();

		JDinkPlayer player = context.getCurrentPlayer();
		if (player == null) {
			throw new RuntimeException("[showItemMenu] no current player");
		}

		List<JDinkSprite> sprites = new ArrayList<JDinkSprite>();
		sprites.add(spriteHelper.newSpriteAbsolute(x, y, 423, 1, false));
		for (int itemIndex = 0; itemIndex < 16; itemIndex++) {
			JDinkItem item = player.getItem(1 + itemIndex);
			if (item != null) {
				sprites.add(addItem(context, itemIndex, false, item));
			}
		}
//		for (JDinkItem item: items) {
//			sprites.add(addItem(context, itemIndex++, false, item));
//		}

		int currentItem =
			Math.max(0, context.getGlobalVariables().currentWeapon.getInt(context) - 1);

		JDinkPoint location = this.getItemLocation(currentItem, false);
		JDinkSprite selectorSprite = spriteHelper.newSpriteAbsolute(
				location.getX(), location.getY(), 423, 2, false);
//		selectorSprite.setAnimationSequenceNumber(423);
//		selectorSprite.setAnimationFrameNumber(2);

		JDinkScope scope = selectorSprite.requestScope(context);
		scope.addInternalVariable(CURRENT_ITEM_VAR_NAME,
				new JDinkVariable(JDinkIntegerType.getInstance(),
						Integer.valueOf(currentItem)));
		selectorSprite.setBrain(this);
		sprites.add(selectorSprite);

		for (JDinkSprite sprite: sprites) {
			sprite.setVisible(true);
		}

		controller.setChanged(true);

		context.getView().updateView();

//		int result = -1;
//		controller.sleep(15000);
//		synchronized (resultVariable) {
//			// wait up to 1h, then close the window
//			controller.waitFor(resultVariable, 60 * 60 * 1000);
//			Object value = resultVariable.getValue();
//			if (value instanceof Integer) {
//				Integer choiceIndexResult = choiceBySpriteNumberMap.get(value);
//				if (choiceIndexResult != null) {
//					result = ((Integer) choiceIndexResult).intValue();
//				}
//			}
//		}

//		if (log.isInfoEnabled()) {
//			log.info("choice result: " + result);
//		}

//		this.sleep(executionContext, 3000);
//		controller.removeSpriteLayer(spriteLayer);
	}

	private void processKeyEvents(JDinkContext context, JDinkSprite sprite) {
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		boolean select = false;
		JDinkController controller = context.getController();
		EventObject[] events = controller.getEvents();
		for (EventObject event: events) {
			if (event instanceof JDinkKeyEvent) {
				JDinkKeyEvent keyEvent = (JDinkKeyEvent) event;
				if (keyEvent.isKeyPressed()) {
					switch (keyEvent.getKeyCode()) {
					case JDinkKeyConstants.VK_UP:
						up = true;
						break;
					case JDinkKeyConstants.VK_DOWN:
						down = true;
						break;
					case JDinkKeyConstants.VK_LEFT:
						left = true;
						break;
					case JDinkKeyConstants.VK_RIGHT:
						right = true;
						break;
					case JDinkKeyConstants.VK_CONTROL:
						select = true;
						break;
					}
				}
			}
		}
//		if ((!up) && (!down)) {
//			select = controller.isKeyPressed(JDinkKeyConstants.VK_CONTROL);
//		}
		if ((up) && (down)) {
			up = false;
			down = false;
		}
		if ((left) && (right)) {
			left = false;
			right = false;
		}
		if ((up) || (down) || (left) || (right)) {
			int index = this.getCurrentItem(context, sprite);
			int columns = 4;
			int rows = 4;
			int column = index % columns;
			int row = index / columns;
			if (down) {
				row = (row + 1) % rows;
			}
			if (up) {
				row = (row + (rows - 1)) % rows;
			}
			if (right) {
				column = (column + 1) % columns;
			}
			if (left) {
				column = (column + (columns - 1)) % columns;
			}
			index = row * columns + column;
			setCurrentItem(context, sprite, index);
			JDinkPoint location = this.getItemLocation(index, false);
			sprite.setX(location.getX());
			sprite.setY(location.getY());
			controller.setChanged(true);
		} else if (select) {
			// TODO
			int index = this.getCurrentItem(context, sprite);
			hideItemMenu(context);
//			controller.removeSpriteLayer(controller.getCurrentSpriteLayer());
//			JDinkItem[] items = controller.getItems();
			JDinkItem oldItem = null;

			JDinkPlayer player = context.getCurrentPlayer();
			if (player == null) {
				throw new RuntimeException("no current player");
			}

			JDinkItem newItem = player.getItem(1 + index);
//			if (0 < items.length) {
//				newItem = items[0];
//			}
//			if (index < items.length) {
//				newItem = items[index];
//			}
			if ((newItem != null) && (newItem != oldItem)) {
				if (oldItem != null) {
					try {
						oldItem.disarm(context);
					} catch (Throwable e) {
						log.error("failed to execute disarm", e);
					}
				}
				if (newItem != null) {
					try {
						context.getGlobalVariables().currentWeapon.setInt(context, newItem.getItemNumber());
						newItem.arm(context);
					} catch (Throwable e) {
						log.error("failed to execute arm", e);
					}
				}
			}
			controller.setChanged(true);
		}
	}

}
