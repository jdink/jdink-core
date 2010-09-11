package de.siteof.jdink.status;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.model.IReadOnlyContextModel;
import de.siteof.jdink.model.JDinkAttachedGlobalIntVariable;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkGlobalVariables;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.model.JDinkSpriteLayer;
import de.siteof.jdink.model.JDinkVariableContextModel;

public class JDinkStatusManager implements Serializable {

	private static class JDinkItemContextModel implements IReadOnlyContextModel<JDinkItem> {

		private final JDinkAttachedGlobalIntVariable variable;
		private final boolean magic;

		public JDinkItemContextModel(JDinkAttachedGlobalIntVariable variable, boolean magic) {
			this.variable = variable;
			this.magic = magic;
		}

		@Override
		public JDinkItem getObject(JDinkContext context) {
			JDinkPlayer player = context.getCurrentPlayer();
			if (player == null) {
				throw new RuntimeException("no current player");
			}
			JDinkItem result = null;
			int itemNumber = this.variable.getInt(context);
			if (magic) {
				result = player.getMagicItem(itemNumber);
			} else {
				result = player.getItem(itemNumber);
			}
			return result;
		}

	}

	private static final long serialVersionUID = 1L;

//	private static final Log log = LogFactory.getLog(JDinkDrawStatusFunction.class);

	private static final String INSTANCE_KEY = "statusManager";

//	private static final String LEVEL_VARIABLE_NAME = "&level";
//	private static final String STRENGTH_VARIABLE_NAME = "&strength";
//	private static final String DEFENSE_VARIABLE_NAME = "&defense";
//	private static final String MAGIC_VARIABLE_NAME = "&magic";
//	private static final String GOLD_VARIABLE_NAME = "&gold";
//	private static final String EXPERIENCE_VARIABLE_NAME = "&exp";
//	private static final String LIFE_VARIABLE_NAME = "&life";
//	private static final String LIFE_MAX_VARIABLE_NAME = "&lifemax";
//	private static final String CUR_WEAPON_VARIABLE_NAME = "&cur_weapon";
//	private static final String CUR_MAGIC_VARIABLE_NAME = "&cur_magic";

	private final IReadOnlyContextModel<String> experienceTextModel;

	private boolean statusDrawn;

	private StatusView[] statusViews;

	private JDinkStatusManager() {
		experienceTextModel = new IReadOnlyContextModel<String>() {
			@Override
			public String getObject(JDinkContext context) {
				return getExperienceText(context);
			}
		};
	}

	public static JDinkStatusManager getInstance(JDinkContext context) {
		JDinkStatusManager statusManager = context.getMetaData(
				INSTANCE_KEY, JDinkStatusManager.class);
		if (statusManager == null) {
			statusManager = new JDinkStatusManager();
			context.setMetaData(INSTANCE_KEY, statusManager);
		}
		return statusManager;
	}

	private int getNextLevelExperience(int level) {
		return Math.min(99999, level * level * 100);
	}

	private void addIntLeadingZero(StringBuilder sb, int value, int digitCount) {
		String s = Integer.toString(value);
		for (int i = s.length(); i < digitCount; i++) {
			sb.append('0');
		}
		if (s.length() > digitCount) {
			s = s.substring(s.length() - digitCount);
		}
		sb.append(s);
	}

	private String getExperienceText(JDinkContext context) {
		int level = context.getGlobalVariables().level.getInt(context);
		int experience = context.getGlobalVariables().experience.getInt(context);
		int nextLevelExperience = getNextLevelExperience(level);
		if (experience > nextLevelExperience) {
			experience = nextLevelExperience;
		}
		StringBuilder sb = new StringBuilder(11);
		addIntLeadingZero(sb, experience, 5);
		sb.append('/');
		addIntLeadingZero(sb, nextLevelExperience, 5);
		return sb.toString();
	}

	public boolean update(JDinkContext context) {
		boolean result = false;
		if (statusViews != null) {
			NumericStatusView levelView = (NumericStatusView) this.statusViews[0];
			int level = context.getGlobalVariables().level.getInt(context);
			int characterLength = (level >= 10 ? 2 : 1);
			if (characterLength != levelView.getCharacterLength()) {
				JDinkController controller = context.getController();
				JDinkSpriteLayer previousSpriteLayer = controller.getCurrentSpriteLayer();
				JDinkSpriteLayer spriteLayer = controller.getSpriteLayer(0);
				try {
					controller.setCurrentSpriteLayer(spriteLayer);
					if  (characterLength == 2) {
						levelView.setX(523);
					} else {
						levelView.setX(528);
					}
					levelView.setCharacterLength(context, characterLength);
				} finally {
					controller.setCurrentSpriteLayer(previousSpriteLayer);
				}
				context.getController().setChanged(true);
			}
			for (int i = 0; i < statusViews.length; i++) {
				if (statusViews[i].update(context)) {
					result = true;
				}
			}
		}
		return result;
	}

	public void drawStatus(JDinkContext context) {
		if (!statusDrawn) {
			JDinkController controller = context.getController();
			JDinkSpriteHelper spriteHelper = context.getSpriteHelper();

			JDinkSpriteLayer previousSpriteLayer = controller.getCurrentSpriteLayer();
			JDinkSpriteLayer spriteLayer = controller.getSpriteLayer(0);
			try {
				controller.setCurrentSpriteLayer(spriteLayer);

				List<JDinkSprite> sprites = new LinkedList<JDinkSprite>();
				sprites.add(spriteHelper.newSpriteAbsolute(0, 0, 180, 1, false));
				sprites.add(spriteHelper.newSpriteAbsolute(620, 0, 180, 2, false));
				sprites.add(spriteHelper.newSpriteAbsolute(0, 400, 180, 3, false));

				JDinkGlobalVariables globalVariables = context.getGlobalVariables();

				IReadOnlyContextModel<Integer> lifeModel = new JDinkVariableContextModel<Integer>(
						globalVariables.life);
				IReadOnlyContextModel<Integer> lifeMaxModel = new JDinkVariableContextModel<Integer>(
						globalVariables.lifeMax);
				IReadOnlyContextModel<JDinkItem> currentItemModel = new JDinkItemContextModel(
						globalVariables.currentWeapon, false);
				IReadOnlyContextModel<JDinkItem> currentMagicItemModel = new JDinkItemContextModel(
						globalVariables.currentMagic, true);

				statusViews = new StatusView[] {
					new NumericStatusView(globalVariables.level, 528, 456, 442, 1),
					new NumericStatusView(globalVariables.strength, 81, 415, 182, 3),
					new NumericStatusView(globalVariables.defense, 81, 437, 183, 3),
					new NumericStatusView(globalVariables.magic, 81, 459, 184, 3),
					new NumericStatusView(globalVariables.gold, 298, 457, 185, 5),
					new TextStatusView(experienceTextModel, 404, 459, 181, 11),
					new ItemStatusView(currentItemModel, 557, 413),
					new ItemStatusView(currentMagicItemModel, 153, 413),
					new BarStatusView(lifeModel, lifeMaxModel,
							451, 190,
							284, 412,
							10,
							110, 220,
							5)
				};

				for (JDinkSprite sprite: sprites) {
					sprite.setVisible(true);
				}

				for (int i = 0; i < statusViews.length; i++) {
					statusViews[i].show(context);
				}
			} finally {
				controller.setCurrentSpriteLayer(previousSpriteLayer);
			}
			controller.setChanged(true);
		}
	}

}
