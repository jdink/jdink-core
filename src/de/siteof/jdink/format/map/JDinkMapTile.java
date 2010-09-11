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
public class JDinkMapTile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int num;
	private int property;
	private int althard;
	private int more2;
	private byte more3;
	private byte more4;
	private int[] buffer;
	
	
	public int getAlthard() {
		return althard;
	}
	public void setAlthard(int althard) {
		this.althard = althard;
	}
	public int[] getBuffer() {
		return buffer;
	}
	public void setBuffer(int[] buffer) {
		this.buffer = buffer;
	}
	public int getMore2() {
		return more2;
	}
	public void setMore2(int more2) {
		this.more2 = more2;
	}
	public byte getMore3() {
		return more3;
	}
	public void setMore3(byte more3) {
		this.more3 = more3;
	}
	public byte getMore4() {
		return more4;
	}
	public void setMore4(byte more4) {
		this.more4 = more4;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getProperty() {
		return property;
	}
	public void setProperty(int property) {
		this.property = property;
	}
}
