/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.format.map;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Loaded from the map. This provides the initial data for a sprite.
 */
public class JDinkMapSpritePlacement implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int INVISIBLE_1_TYPE  = 0;
	public static final int PLAYER_CREATURE_TYPE  = 1;
	public static final int INVISIBLE_2_TYPE  = 2;

	private int x;
	private int y;
	private int sequenceNumber;
	private int frameNumber;
	private int type;
	private int size;
	private boolean active;
	private int rotation;
	private int special;
	private int brainNumber;
	private String scriptName;
	private String hit;
	private String die;
	private String talk;
	private int speed;
	private int baseWalk;
	private int baseIdle;
	private int baseAttack;
	private int baseHit;
	private int timer;
	
	/**
	 * The "que" is used as a depth hint. Sprites with a higher que may be moved on top of those with a lower que.
	 */
	private int que;
	private int hard;
	private int altX1;
	private int altY1;
	private int altX2;
	private int altY2;
	private int prop;
	private int warpMap;
	private int warpX;
	private int warpY;
	private int paramSeq;
	private int baseDie;
	private int gold;
	private int hitPoints;
	private int strength;
	private int defense;
	private int exp;
	private int sound;
	private int vision;
	private int nohit;
	private int touchDamage;
	private int[] buffer;
	
	@Override
	public String toString() {
		return "JDinkMapSpritePlacement [active=" + active + ", altX1=" + altX1
				+ ", altX2=" + altX2 + ", altY1=" + altY1 + ", altY2=" + altY2
				+ ", baseAttack=" + baseAttack + ", baseDie=" + baseDie
				+ ", baseHit=" + baseHit + ", baseIdle=" + baseIdle
				+ ", baseWalk=" + baseWalk + ", brainNumber=" + brainNumber
				+ ", buffer=" + Arrays.toString(buffer) + ", defense="
				+ defense + ", die=" + die + ", exp=" + exp + ", frameNumber="
				+ frameNumber + ", gold=" + gold + ", hard=" + hard + ", hit="
				+ hit + ", hitPoints=" + hitPoints + ", nohit=" + nohit
				+ ", paramSeq=" + paramSeq + ", prop=" + prop + ", que=" + que
				+ ", rotation=" + rotation + ", scriptName=" + scriptName
				+ ", sequenceNumber=" + sequenceNumber + ", size=" + size
				+ ", sound=" + sound + ", special=" + special + ", speed="
				+ speed + ", strength=" + strength + ", talk=" + talk
				+ ", timer=" + timer + ", touchDamage=" + touchDamage
				+ ", type=" + type + ", vision=" + vision + ", warpMap="
				+ warpMap + ", warpX=" + warpX + ", warpY=" + warpY + ", x="
				+ x + ", y=" + y + "]";
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getAltX1() {
		return altX1;
	}
	public void setAltX1(int altX1) {
		this.altX1 = altX1;
	}
	public int getAltX2() {
		return altX2;
	}
	public void setAltX2(int altX2) {
		this.altX2 = altX2;
	}
	public int getAltY1() {
		return altY1;
	}
	public void setAltY1(int altY1) {
		this.altY1 = altY1;
	}
	public int getAltY2() {
		return altY2;
	}
	public void setAltY2(int altY2) {
		this.altY2 = altY2;
	}
	public int getBaseAttack() {
		return baseAttack;
	}
	public void setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;
	}
	public int getBaseDie() {
		return baseDie;
	}
	public void setBaseDie(int baseDie) {
		this.baseDie = baseDie;
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
	public void setBaseHit(int baseInit) {
		this.baseHit = baseInit;
	}
	public int getBaseWalk() {
		return baseWalk;
	}
	public void setBaseWalk(int baseWalk) {
		this.baseWalk = baseWalk;
	}
	public int getBrainNumber() {
		return brainNumber;
	}
	public void setBrainNumber(int brainNumber) {
		this.brainNumber = brainNumber;
	}
	public int[] getBuffer() {
		return buffer;
	}
	public void setBuffer(int[] buffer) {
		this.buffer = buffer;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public String getDie() {
		return die;
	}
	public void setDie(String die) {
		this.die = die;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getHard() {
		return hard;
	}
	public void setHard(int hard) {
		this.hard = hard;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public int getHitPoints() {
		return hitPoints;
	}
	public void setHitPoints(int hitpoints) {
		this.hitPoints = hitpoints;
	}
	public int getNohit() {
		return nohit;
	}
	public void setNohit(int nohit) {
		this.nohit = nohit;
	}
	public int getParamSeq() {
		return paramSeq;
	}
	public void setParamSeq(int paramSeq) {
		this.paramSeq = paramSeq;
	}
	public int getProp() {
		return prop;
	}
	public void setProp(int prop) {
		this.prop = prop;
	}
	public int getQue() {
		return que;
	}
	public void setQue(int que) {
		this.que = que;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getSound() {
		return sound;
	}
	public void setSound(int sound) {
		this.sound = sound;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public String getTalk() {
		return talk;
	}
	public void setTalk(String talk) {
		this.talk = talk;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public int getTouchDamage() {
		return touchDamage;
	}
	public void setTouchDamage(int touchDamage) {
		this.touchDamage = touchDamage;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getVision() {
		return vision;
	}
	public void setVision(int vision) {
		this.vision = vision;
	}
	public int getWarpMap() {
		return warpMap;
	}
	public void setWarpMap(int warpMap) {
		this.warpMap = warpMap;
	}
	public int getWarpX() {
		return warpX;
	}
	public void setWarpX(int warpX) {
		this.warpX = warpX;
	}
	public int getWarpY() {
		return warpY;
	}
	public void setWarpY(int warpY) {
		this.warpY = warpY;
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
}
