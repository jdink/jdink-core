package de.siteof.jdink.functions.ini.util;

import java.io.Serializable;

public class JDinkSpriteInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Integer offsetX;
	private final Integer offsetY;
	private final Integer hardX1;
	private final Integer hardY1;
	private final Integer hardX2;
	private final Integer hardY2;
	
	public JDinkSpriteInfo(
			Integer offsetX,
			Integer offsetY,
			Integer hardX1,
			Integer hardY1,
			Integer hardX2,
			Integer hardY2) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.hardX1 = hardX1;
		this.hardY1 = hardY1;
		this.hardX2 = hardX2;
		this.hardY2 = hardY2;
	}

	public Integer getOffsetX() {
		return offsetX;
	}

	public Integer getOffsetY() {
		return offsetY;
	}

	public Integer getHardX1() {
		return hardX1;
	}

	public Integer getHardY1() {
		return hardY1;
	}

	public Integer getHardX2() {
		return hardX2;
	}

	public Integer getHardY2() {
		return hardY2;
	}

}
