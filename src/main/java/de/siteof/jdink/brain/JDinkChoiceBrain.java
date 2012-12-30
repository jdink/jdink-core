package de.siteof.jdink.brain;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.choice.JDinkChoicePlaceholder;
import de.siteof.jdink.choice.JDinkSaveGameInfoChoicePlaceholder;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.events.JDinkKeyConstants;
import de.siteof.jdink.events.JDinkKeyEvent;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkColorIndex;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.model.JDinkSpriteLayer;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkVariable;
import de.siteof.jdink.util.JDinkSpriteUtil;

/**
 * <p>Handles the choice menu.</p>
 * <p>This brain isn't assigned to any brain number.</p>
 */
public class JDinkChoiceBrain extends AbstractJDinkBrain {

	private static class View {
		private JDinkSpriteLayer spriteLayer;
		private List<JDinkSprite> choicesSprites;
		private JDinkVariable resultVariable;

		public JDinkSpriteLayer getSpriteLayer() {
			return spriteLayer;
		}

		public void setSpriteLayer(JDinkSpriteLayer spriteLayer) {
			this.spriteLayer = spriteLayer;
		}

		public JDinkVariable getResultVariable() {
			return resultVariable;
		}

		public void setResultVariable(JDinkVariable resultVariable) {
			this.resultVariable = resultVariable;
		}

		public List<JDinkSprite> getChoicesSprites() {
			return choicesSprites;
		}

		public void setChoicesSprites(List<JDinkSprite> choicesSprites) {
			this.choicesSprites = choicesSprites;
		}
	}

	private static final Log log = LogFactory.getLog(JDinkChoiceBrain.class);

	private final static String CHOICE_MENU_VIEW_KEY = "brain.choice.view";

	private final static String SET_Y = "set_y";
	private final static String SET_TITLE_COLOR = "set_title_color";
	private final static String TITLE_START = "title_start";
	private final static String TITLE_END = "title_end";

	public final static String SELECTED_PREFIX = JDinkColorIndex.getColorString(15);

	private static JDinkChoiceBrain instance = new JDinkChoiceBrain();

	private final Map<String, JDinkChoicePlaceholder> placeHolderMap;

	public JDinkChoiceBrain() {
		placeHolderMap = new HashMap<String, JDinkChoicePlaceholder>();
		placeHolderMap.put("&savegameinfo", new JDinkSaveGameInfoChoicePlaceholder());
	}

	public static JDinkChoiceBrain getInstance() {
		return instance;
	}

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		this.processKeyEvents(sprite, context);
	}

	private String processPlaceholders(
			JDinkExecutionContext executionContext,
			int index,
			String text) {
		StringBuilder sb = null;
		int currentIndex = 0;
		while (currentIndex < text.length()) {
			int placeholderIndex = text.indexOf('&');
			if (placeholderIndex < 0) {
				break;
			}
			for (Map.Entry<String, JDinkChoicePlaceholder> entry: placeHolderMap.entrySet()) {
				String placeholderString = entry.getKey();
				if (text.startsWith(placeholderString)) {
					String replacement = entry.getValue().getText(executionContext, index);
					if (sb == null) {
						sb = new StringBuilder(
								text.length() - placeholderString.length() + replacement.length());
					}
					if (placeholderIndex > currentIndex) {
						sb.append(text.substring(currentIndex, placeholderIndex));
					}
					sb.append(replacement);
					currentIndex = placeholderIndex + placeholderString.length();
				}
			}
		}
		String result = null;
		if (sb == null) {
			result = text;
		} else {
			if (currentIndex < sb.length()) {
				sb.append(text.substring(currentIndex));
			}
			result = sb.toString();
		}
		return result;
	}

	public void hideChoiceMenu(JDinkContext context) {
		View view = context.getMetaData(CHOICE_MENU_VIEW_KEY, View.class);
		if (view != null) {
			JDinkSpriteLayer spriteLayer = view.getSpriteLayer();
			JDinkVariable variable = view.getResultVariable();
			context.setMetaData(CHOICE_MENU_VIEW_KEY, null);

			if (spriteLayer != null) {
				context.getController().removeSpriteLayer(spriteLayer);
			}
			if (variable != null) {
				synchronized (variable) {
					variable.notifyAll();
				}
			}
		}
	}

	public int showChoiceMenuAndWait(
			JDinkExecutionContext executionContext,
			Object[] choiceArguments) throws Throwable {
		JDinkContext context = executionContext.getContext();
		hideChoiceMenu(context);
		List<Object> actualChoicesArguments = new ArrayList<Object>(choiceArguments.length);
		for (int i = 0; i < choiceArguments.length; i++) {
			if (log.isDebugEnabled()) {
				log.debug("choice[" + i + "]: " + choiceArguments[i]);
			}
			Object choiceArgument = executionContext.asValue(choiceArguments[i]);
			if (choiceArgument != null) {
				if (choiceArgument instanceof String) {
					choiceArgument = processPlaceholders(
							executionContext, i, (String) choiceArgument);
				}
				actualChoicesArguments.add(choiceArgument);
			} else {
				if (log.isInfoEnabled()) {
					log.info("ignoring not applicable choice item: " + choiceArguments[i]);
				}
				actualChoicesArguments.add(null);
			}
		}
		if (actualChoicesArguments.size() == 0) {
			log.debug("choice arguments empty");
		}
		log.info("actualChoices=" + actualChoicesArguments);
		JDinkController controller = context.getController();
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		JDinkSpriteLayer spriteLayer = controller.addNewSpriteLayer();
		View view = new View();
		view.setSpriteLayer(spriteLayer);
		context.setMetaData(CHOICE_MENU_VIEW_KEY, view);

		int x = 48;
		int y = 44;

		List<JDinkSprite> sprites = new ArrayList<JDinkSprite>();
		sprites.add(spriteHelper.newSpriteAbsolute(x, y, 30, 2, false));
		sprites.add(spriteHelper.newSpriteAbsolute(x + 169, y + 42, 30, 3, false));
		sprites.add(spriteHelper.newSpriteAbsolute(x + 169 + 180, y + 1, 30, 4, false));

		for (JDinkSprite sprite: sprites) {
			sprite.setVisible(true);
		}

		Map<Integer, Integer> choiceBySpriteNumberMap = new HashMap<Integer, Integer>(actualChoicesArguments.size());
		List<JDinkSprite> choicesSprites = new ArrayList<JDinkSprite>(actualChoicesArguments.size());
		int textX = x + 169;
		boolean first = true;
		int choiceIndex = 1;
		int choiceCount = 0;
		for (Object choice: actualChoicesArguments) {
			if ((choice != null) && (choice instanceof String)) {
				choiceCount++;
			}
		}

		int maxHeight = JDinkSpriteUtil.getBounds(context, sprites.get(1)).getBounds().getHeight();
		int textHeight = Math.min(30, (maxHeight - 10) / choiceCount);
		int textY = y + 42 + 20 - 20 + (maxHeight - textHeight * choiceCount) / 2;
		int titleY = y + 42 + 20;
		int titleColor = 0;
		boolean titleMode = false;

		for (Object choice: actualChoicesArguments) {
			if (choice == null) {
				choiceIndex++;
			} else if (choice instanceof Object[]) {
				Object[] a = (Object[]) choice;
				if (a.length > 0) {
					if (SET_Y.equals(a[0])) {
						if ((a.length >= 2) && (a[1] instanceof Integer)) {
							textY = ((Integer) a[1]).intValue();
						} else {
							log.warn("[showChoiceMenuAndWait] integer parameter expected for set_y");
						}
					} else if (SET_TITLE_COLOR.equals(a[0])) {
						if ((a.length >= 2) && (a[1] instanceof Integer)) {
							titleColor = ((Integer) a[1]).intValue();
						} else {
							log.warn("[showChoiceMenuAndWait] integer parameter expected for set_y");
						}
					} else {
						String s = a[0].toString();
						int pos = s.indexOf('(');
						String functionName = s.substring(0, pos).trim();
						if (TITLE_START.equals(functionName)) {
							titleMode = true;
						} else if (TITLE_END.equals(functionName)) {
							titleMode = false;
						}
					}
				}
			} else if (choice instanceof String) {
				String text = choice.toString();
				if (titleMode) {
					if (titleColor > 0) {
						text = JDinkColorIndex.getColorString(titleColor) + text;
					}
					spriteHelper.showText(executionContext,
							text,
							Integer.valueOf(textX), Integer.valueOf(titleY));
					titleY += textHeight;
				} else {
					if (first) {
						text = JDinkChoiceBrain.SELECTED_PREFIX + text;
						first = false;
					}
					JDinkSprite textSprite = spriteHelper.showText(executionContext,
							text,
							Integer.valueOf(textX), Integer.valueOf(textY));
					if (textSprite != null) {
						choicesSprites.add(textSprite);
						choiceBySpriteNumberMap.put(Integer.valueOf(textSprite.getSpriteNumber()),
								Integer.valueOf(choiceIndex));
					}
					textY += textHeight;
					choiceIndex++;
				}
			}
		}
		view.setChoicesSprites(choicesSprites);

		JDinkSprite choiceSprite = sprites.get(0);
		JDinkVariable resultVariable = new JDinkVariable(JDinkIntegerType.getInstance(), null);
		view.setResultVariable(resultVariable);
		choiceSprite.setBrain(JDinkChoiceBrain.getInstance());

		controller.setChanged(true);

		context.getView().updateView();

		int result = -1;
		synchronized (resultVariable) {
			// wait up to 1h, then close the window
			controller.waitFor(resultVariable, 60 * 60 * 1000);
			Object value = resultVariable.getValue();
			if (value instanceof Integer) {
				Integer choiceIndexResult = choiceBySpriteNumberMap.get(value);
				if (choiceIndexResult != null) {
					result = ((Integer) choiceIndexResult).intValue();
				}
			}
		}

		if (log.isInfoEnabled()) {
			log.info("choice result: " + result);
		}

		controller.removeSpriteLayer(spriteLayer);

		return result;
	}

	private List<JDinkSprite> getChoicesSprites(JDinkSprite sprite, JDinkContext context) {
		List<JDinkSprite> result = null;
		View view = context.getMetaData(CHOICE_MENU_VIEW_KEY, View.class);
		if (view != null) {
			result = view.getChoicesSprites();
		}
		return result;
	}

	private void setSelectionResult(JDinkSprite sprite, JDinkContext context, int selectedSpriteNumber) {
		View view = context.getMetaData(CHOICE_MENU_VIEW_KEY, View.class);
		if (view != null) {
			JDinkVariable choicesResultVariable = view.getResultVariable();
			if (choicesResultVariable != null) {
				synchronized (choicesResultVariable) {
					choicesResultVariable.setType(JDinkIntegerType.getInstance());
					choicesResultVariable.setValue(Integer.valueOf(selectedSpriteNumber));
					choicesResultVariable.notifyAll();
				}
			} else {
				log.warn("[setSelectionResult] result variable not found");
			}
		} else {
			log.warn("[setSelectionResult] view not found");
		}
	}

	private boolean isSelected(JDinkSprite textSprite) {
		String text = (textSprite != null ? textSprite.getText() : null);
		return (text != null) && (text.startsWith(SELECTED_PREFIX));
	}

	private void setSelected(JDinkSprite textSprite, boolean selected) {
		String text = (textSprite != null ? textSprite.getText() : null);
		if (text != null) {
			if (selected) {
				if (!text.startsWith(SELECTED_PREFIX)) {
					textSprite.setText(SELECTED_PREFIX + textSprite.getText());
				}
			} else {
				if (text.startsWith(SELECTED_PREFIX)) {
					textSprite.setText(text.substring(SELECTED_PREFIX.length()));
				}
			}
		}
	}

	private void processKeyEvents(JDinkSprite sprite, JDinkContext context) {
		boolean up = false;
		boolean down = false;
		boolean select = false;
		JDinkController controller = context.getController();
		EventObject[] events = controller.getEvents();
		for (EventObject event: events) {
			if (event instanceof JDinkKeyEvent) {
				JDinkKeyEvent keyEvent = (JDinkKeyEvent) event;
				if (keyEvent.isKeyReleased()) {
					switch (keyEvent.getKeyCode()) {
					case JDinkKeyConstants.VK_UP:
						up = true;
						break;
					case JDinkKeyConstants.VK_DOWN:
						down = true;
						break;
					case JDinkKeyConstants.VK_CONTROL:
						select = true;
						break;
					}
				}
			}
		}
		if ((up) || (down)) {
			List<JDinkSprite> sprites = getChoicesSprites(sprite, context);
			if (sprites != null) {
				int selectedIndex = -1;
				for (int i = 0; i < sprites.size(); i++) {
					JDinkSprite textSprite = sprites.get(i);
					if ((textSprite != null) && (isSelected(textSprite))) {
						setSelected(textSprite, false);
						selectedIndex = i;
						break;
					}
				}
				if (selectedIndex < 0) {
					selectedIndex = 0;
				} else if (up) {
					selectedIndex = ((selectedIndex + sprites.size() - 1) % sprites.size());
				} else if (down) {
					selectedIndex = ((selectedIndex + 1) % sprites.size());
				}
				JDinkSprite selectedSprite = sprites.get(selectedIndex);
				setSelected(selectedSprite, true);
				controller.setChanged(true);
			}
		} else if (select) {
			controller.clearCurrentEvents();
			List<JDinkSprite> sprites = getChoicesSprites(sprite, context);
			if (sprites != null) {
				int selectedSpriteNumber = -1;
				for (int i = 0; i < sprites.size(); i++) {
					JDinkSprite textSprite = sprites.get(i);
					if ((textSprite != null) && (isSelected(textSprite))) {
						selectedSpriteNumber = textSprite.getSpriteNumber();
						break;
					}
				}
				setSelectionResult(sprite, context, selectedSpriteNumber);
			}
		}
	}

}
