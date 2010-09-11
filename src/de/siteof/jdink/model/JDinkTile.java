/*
 * Created on 04.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.model;

import de.siteof.jdink.geom.JDinkRectangle;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkTile {

	private JDinkTileSet tileSet;
	private JDinkRectangle sourceRectangle;
	private int x;
	private int y;
	private byte[][] hardness;

	public JDinkRectangle getSourceRectangle() {
		return sourceRectangle;
	}
	public void setSourceRectangle(JDinkRectangle sourceRectangle) {
		this.sourceRectangle = sourceRectangle;
	}
	public JDinkTileSet getTileSet() {
		return tileSet;
	}
	public void setTileSet(JDinkTileSet tileSet) {
		this.tileSet = tileSet;
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
	/**
	 * @return the hardness
	 */
	public byte[][] getHardness() {
		return hardness;
	}
	/**
	 * @param hardness the hardness to set
	 */
	public void setHardness(byte[][] hardness) {
		this.hardness = hardness;
	}
}
