package de.siteof.jdink.model;


/**
 * <p>Helper class to access common global variables. It is also designed to make the access fast.</p>
 */
public class JDinkGlobalVariables {

	// the following attached variables are not static because they are attached to a context
	public final JDinkAttachedGlobalIntVariable currentWeapon = new JDinkAttachedGlobalIntVariable("&cur_weapon");
	public final JDinkAttachedGlobalIntVariable currentMagic = new JDinkAttachedGlobalIntVariable("&cur_magic");
	public final JDinkAttachedGlobalIntVariable defense = new JDinkAttachedGlobalIntVariable("&defense");
	public final JDinkAttachedGlobalIntVariable experience = new JDinkAttachedGlobalIntVariable("&exp");
	public final JDinkAttachedGlobalIntVariable gold = new JDinkAttachedGlobalIntVariable("&gold");
	public final JDinkAttachedGlobalIntVariable level = new JDinkAttachedGlobalIntVariable("&level");
	public final JDinkAttachedGlobalIntVariable life = new JDinkAttachedGlobalIntVariable("&life");
	public final JDinkAttachedGlobalIntVariable lifeMax = new JDinkAttachedGlobalIntVariable("&lifemax");
	public final JDinkAttachedGlobalIntVariable magic = new JDinkAttachedGlobalIntVariable("&magic");
	public final JDinkAttachedGlobalIntVariable playerMap = new JDinkAttachedGlobalIntVariable("&player_map");
	public final JDinkAttachedGlobalIntVariable strength = new JDinkAttachedGlobalIntVariable("&strength");
	public final JDinkAttachedGlobalIntVariable vision = new JDinkAttachedGlobalIntVariable("&vision");

	private final JDinkAttachedGlobalIntVariable[] variables = {
			currentWeapon,
			currentMagic,
			defense,
			experience,
			gold,
			level,
			life,
			lifeMax,
			magic,
			playerMap,
			strength,
			vision
	};

	public void detach() {
		for (int i = 0; i < variables.length; i++) {
			variables[i].detach();
		}
	}

//	public int getPlayerMap(JDinkContext context) {
//		return getInt(context, VariableId.PLAYER_MAP);
//	}
//
//	public void setPlayerMap(JDinkContext context, int value) {
//		setInt(context, VariableId.PLAYER_MAP, value);
//	}
//
//	public int getVision(JDinkContext context) {
//		return getInt(context, VariableId.VISION);
//	}
//
//	public void setVision(JDinkContext context, int value) {
//		setInt(context, VariableId.VISION, value);
//	}
//
//	public int getCurWeapon(JDinkContext context) {
//		return getInt(context, VariableId.CUR_WEAPON);
//	}
//
//	public void setCurWeapon(JDinkContext context, int value) {
//		setInt(context, VariableId.CUR_WEAPON, value);
//	}

}
