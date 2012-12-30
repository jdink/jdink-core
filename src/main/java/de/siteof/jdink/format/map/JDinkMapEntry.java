/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.format.map;

import java.io.Serializable;


/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkMapEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private JDinkMapTile[] tiles;
	private int[] v;
	private String s;
	private JDinkMapSpritePlacement[] spritePlacements;
	private String scriptName;
	private String random;
	private String load;
	private String buffer;

	public String getBuffer() {
		return buffer;
	}
	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}
	public String getLoad() {
		return load;
	}
	public void setLoad(String load) {
		this.load = load;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String script) {
		this.scriptName = script;
	}
	public JDinkMapSpritePlacement[] getSpritePlacements() {
		return spritePlacements;
	}
	public void setSpritePlacements(JDinkMapSpritePlacement[] spritePlacements) {
		this.spritePlacements = spritePlacements;
	}
	public JDinkMapTile[] getTiles() {
		return tiles;
	}
	public void setTiles(JDinkMapTile[] tiles) {
		this.tiles = tiles;
	}
	public int[] getV() {
		return v;
	}
	public void setV(int[] v) {
		this.v = v;
	}
}
