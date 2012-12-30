package de.siteof.jdink.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import de.siteof.jdink.script.JDinkVariable;

public class JDinkSaveGame implements Serializable {

	private static final long serialVersionUID = 1L;

	private int version;
	private String gameInfo;
	private int minutes;
	private int x,y,die, size, defense, dir, pframe, pseq, seq, frame, strength, baseWalk, baseIdle,
        baseHit,que;

	private Map<Integer, JDinkItem> items;
	private Map<Integer, JDinkItem> magicItems;

    private int curitem;

    /**
     * not used
     */
    @Deprecated
    private int unused;

    /**
     * not used
     */
    @Deprecated
    private int counter;

    /**
     * not used
     */
    @Deprecated
    private boolean idle;

    private Map<Integer, JDinkMapState> mapStateMap;

    /**
     * The buttons define the mapping of the buttons.
     */
    private int[] buttons;

    /**
     * Map from scope to variables. Although only global variables (scope 0) would make sense
     * in this context as script instances will not be saved.
     */
	private Map<Integer, Map<String, JDinkVariable>> variableScopeMap;

    private boolean pushActive;
    private int pushDir;
    private long pushTimer;
    private int lastTalk;
    private int mouse;
    private boolean itemMagic;
    private int lastMap;

    // crap doesn't seem to be used
//    private int crap;

    // buff doesn't seem to be used
//    private int buff[95];
    // dbuff doesn't seem to be used
//    private long dbuff[20];

    // lbuff doesn't seem to be used
//    private long lbuff[10];

    //redink1... use wasted space for storing file location of map.dat, dink.dat, palette, and tiles
    private String mapdat;
    private String dinkdat;

    /**
     * Bitmap resource name used for a custom palette.
     */
    private String palette;

    /**
     * Contains replaced tiles loaded through load_tile function
     * (Maps tile id to image resource name)
     */
    private Map<Integer, String> tileMap;

    /**
     * Contains global functions declared using the "make_global_function".
     * (Maps filename to function names)
     */
    private Map<String, Collection<String>> globalFunctionMap;

    /**
     * Not used
     */
    @Deprecated
    private String cbuff;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(String gameinfo) {
		this.gameInfo = gameinfo;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDie() {
		return die;
	}

	public void setDie(int die) {
		this.die = die;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getPframe() {
		return pframe;
	}

	public void setPframe(int pframe) {
		this.pframe = pframe;
	}

	public int getPseq() {
		return pseq;
	}

	public void setPseq(int pseq) {
		this.pseq = pseq;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getBaseWalk() {
		return baseWalk;
	}

	public void setBaseWalk(int baseWalk) {
		this.baseWalk = baseWalk;
	}

	public int getBaseIdle() {
		return baseIdle;
	}

	public void setBaseIdle(int baseIdle) {
		this.baseIdle = baseIdle;
	}

	public int getBaseHit() {
		return baseHit;
	}

	public void setBaseHit(int baseHit) {
		this.baseHit = baseHit;
	}

	public int getQue() {
		return que;
	}

	public void setQue(int que) {
		this.que = que;
	}

	public Map<Integer, JDinkItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, JDinkItem> items) {
		this.items = items;
	}

	public Map<Integer, JDinkItem> getMagicItems() {
		return magicItems;
	}

	public void setMagicItems(Map<Integer, JDinkItem> magicItems) {
		this.magicItems = magicItems;
	}

	public int getCuritem() {
		return curitem;
	}

	public void setCuritem(int curitem) {
		this.curitem = curitem;
	}

	public int getUnused() {
		return unused;
	}

	public void setUnused(int unused) {
		this.unused = unused;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	public Map<Integer, JDinkMapState> getMapStateMap() {
		return mapStateMap;
	}

	public void setMapStateMap(Map<Integer, JDinkMapState> mapStateMap) {
		this.mapStateMap = mapStateMap;
	}

	public int[] getButtons() {
		return buttons;
	}

	public void setButtons(int[] buttons) {
		this.buttons = buttons;
	}

	public Map<Integer, Map<String, JDinkVariable>> getVariableScopeMap() {
		return variableScopeMap;
	}

	public void setVariableScopeMap(Map<Integer, Map<String, JDinkVariable>> variableScopeMap) {
		this.variableScopeMap = variableScopeMap;
	}

	public boolean isPushActive() {
		return pushActive;
	}

	public void setPushActive(boolean pushActive) {
		this.pushActive = pushActive;
	}

	public int getPushDir() {
		return pushDir;
	}

	public void setPushDir(int pushDir) {
		this.pushDir = pushDir;
	}

	public long getPushTimer() {
		return pushTimer;
	}

	public void setPushTimer(long pushTimer) {
		this.pushTimer = pushTimer;
	}

	public int getLastTalk() {
		return lastTalk;
	}

	public void setLastTalk(int lastTalk) {
		this.lastTalk = lastTalk;
	}

	public int getMouse() {
		return mouse;
	}

	public void setMouse(int mouse) {
		this.mouse = mouse;
	}

	public boolean isItemMagic() {
		return itemMagic;
	}

	public void setItemMagic(boolean itemMagic) {
		this.itemMagic = itemMagic;
	}

	public int getLastMap() {
		return lastMap;
	}

	public void setLastMap(int lastMap) {
		this.lastMap = lastMap;
	}

	public String getMapdat() {
		return mapdat;
	}

	public void setMapdat(String mapdat) {
		this.mapdat = mapdat;
	}

	public String getDinkdat() {
		return dinkdat;
	}

	public void setDinkdat(String dinkdat) {
		this.dinkdat = dinkdat;
	}

	public String getPalette() {
		return palette;
	}

	public void setPalette(String palette) {
		this.palette = palette;
	}

	public Map<Integer, String> getTileMap() {
		return tileMap;
	}

	public void setTileMap(Map<Integer, String> tileMap) {
		this.tileMap = tileMap;
	}

	public Map<String, Collection<String>> getGlobalFunctionMap() {
		return globalFunctionMap;
	}

	public void setGlobalFunctionMap(
			Map<String, Collection<String>> globalFunctionMap) {
		this.globalFunctionMap = globalFunctionMap;
	}

	public String getCbuff() {
		return cbuff;
	}

	public void setCbuff(String cbuff) {
		this.cbuff = cbuff;
	}

}
