package de.siteof.jdink.model.view;

import java.util.Map;

import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSprite;

public final class JDinkSpriteDisplayInformation {
	
	private final int spriteNumber;
	private final int sequenceNumber;
	private final JDinkSequence sequence;
	private final JDinkSequenceFrame frame;
	private final int x;
	private final int y;
	private final JDinkPoint offset;
	private final int size;
	private final JDinkSpriteDisplayInformation parent;
	private final JDinkShape clipShape;
	private final JDinkShape collisionShape;
	private final String text;
	private final boolean positionAbsolute;
	private final int layerNumber;
	private final boolean noClip;
	private final boolean fill;
	
	private JDinkSpriteDisplayInformation(
			JDinkSprite sprite,
			Map<Integer, JDinkSpriteDisplayInformation> spriteDisplayInformationMap) {
		spriteDisplayInformationMap.put(Integer.valueOf(sprite.getSpriteNumber()),
				this);
		this.spriteNumber = sprite.getSpriteNumber();
		this.sequenceNumber = sprite.getSequenceNumber();
		this.sequence = sprite.getSequence();
		this.frame = (sequence != null ? sequence.getFrame(sprite.getFrameNumber(), false) : null);
		this.x = sprite.getX();
		this.y = sprite.getY();
		this.offset = (sprite.getOffset() != null ? sprite.getOffset() : JDinkPoint.EMPTY);
		this.size = sprite.getSize();
		JDinkSprite parentSprite = sprite.getParentSprite();
		if (parentSprite != null) {
			this.parent = forSprite(parentSprite, spriteDisplayInformationMap);
		} else {
			this.parent = null;
		}
		this.clipShape = sprite.getClipShape();
		this.collisionShape = sprite.getCollisionShape();
		this.text = sprite.getText();
		this.positionAbsolute = sprite.isPositionAbsolute();
		this.layerNumber = sprite.getLayerNumber();
		this.noClip = sprite.isNoClip();
		this.fill = sprite.isFill();
	}
	
	public static JDinkSpriteDisplayInformation forSprite(
			JDinkSprite sprite,
			Map<Integer, JDinkSpriteDisplayInformation> spriteDisplayInformationMap) {
		JDinkSpriteDisplayInformation result = spriteDisplayInformationMap.get(
				Integer.valueOf(sprite.getSpriteNumber()));
		if (result == null) {
			result = new JDinkSpriteDisplayInformation(sprite, spriteDisplayInformationMap);			
		}
		return result;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public JDinkSequence getSequence() {
		return sequence;
	}

	public JDinkSequenceFrame getFrame() {
		return frame;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public JDinkSpriteDisplayInformation getParent() {
		return parent;
	}

	public JDinkShape getCollisionShape() {
		return collisionShape;
	}

	public String getText() {
		return text;
	}

	public int getSpriteNumber() {
		return spriteNumber;
	}

	public int getSize() {
		return size;
	}

	public boolean isPositionAbsolute() {
		return positionAbsolute;
	}

	public int getLayerNumber() {
		return layerNumber;
	}

	public boolean isNoClip() {
		return noClip;
	}

	public JDinkShape getClipShape() {
		return clipShape;
	}

	public boolean isFill() {
		return fill;
	}

	public JDinkPoint getOffset() {
		return offset;
	}

}
