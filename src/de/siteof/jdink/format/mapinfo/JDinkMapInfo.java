/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.format.mapinfo;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkMapInfo {
	
	private String name;
	private int[] loc;
	private int[] music;
	private int[] indoor;
	private int[] v;
	private String s;
	private String buffer;

	public String getBuffer() {
		return buffer;
	}
	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}
	public int[] getIndoor() {
		return indoor;
	}
	public void setIndoor(int[] indoor) {
		this.indoor = indoor;
	}
	public int[] getLoc() {
		return loc;
	}
	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	public int[] getMusic() {
		return music;
	}
	public void setMusic(int[] music) {
		this.music = music;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public int[] getV() {
		return v;
	}
	public void setV(int[] v) {
		this.v = v;
	}
}
