package de.siteof.jdink.model;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.text.util.JDinkTextUtils;

public class JDinkSpriteHelper {

	private final JDinkContext context;

	public JDinkSpriteHelper(JDinkContext context) {
		this.context = context;
	}

	public void setSpriteSequence(JDinkSprite sprite, int sequenceNumber) {
		sprite.setSequenceNumber(sequenceNumber);
		sprite.setSequence(context.getSequence(sprite.getSequenceNumber(), false));
	}

	public void setSpriteFrame(JDinkSprite sprite, int frameNumber) {
		sprite.setFrameNumber(frameNumber);
	}

	public void setSpriteSequenceAndFrame(JDinkSprite sprite, int sequenceNumber, int frameNumber) {
		setSpriteSequence(sprite, sequenceNumber);
		setSpriteFrame(sprite, frameNumber);
	}

	public void setSpriteBrain(JDinkSprite sprite, int brainNumber) {
		sprite.setBrainNumber(brainNumber);
		sprite.setBrain(context.getBrain(brainNumber));
	}

	public void adjustAbsolute(JDinkSprite sprite) {
		// make absolute
		sprite.setPositionAbsolute(true);
//		JDinkSequence sequence = sprite.getSequence();
//		if (sequence != null) {
//			sequence.requireLoaded();
//		}
//		JDinkShape shape = sprite.getBounds();
//		if (shape != null) {
//			JDinkRectangle r = shape.getBounds();
//			sprite.setX(sprite.getX() - r.getX());
//			sprite.setY(sprite.getY() - r.getY());
//		} else {
//			sprite.setX(sprite.getX() + sequence.getOffsetX());
//			sprite.setY(sprite.getY() + sequence.getOffsetY());
//		}
	}

	public JDinkSprite newSprite() {
		JDinkController controller = context.getController();
		return controller.getSprite(controller.allocateSprite(), false);
	}

	public JDinkSprite newSprite(int x, int y, int sequenceNumber, int frameNumber, boolean visible) {
		JDinkSprite sprite = this.newSprite();
		sprite.setX(x);
		sprite.setY(y);
		this.setSpriteSequenceAndFrame(sprite, sequenceNumber, frameNumber);
		sprite.setSpeed(1);
		sprite.setTiming(33);
		sprite.setSize(100);
		sprite.setNoHardness(true);
		sprite.setVisible(visible);
		return sprite;
	}

	public JDinkSprite newSpriteAbsolute(int x, int y, int sequenceNumber, int frameNumber, boolean visible) {
		JDinkSprite sprite = newSprite(x, y, sequenceNumber, frameNumber, false);
		adjustAbsolute(sprite);
		sprite.setVisible(visible);
		return sprite;
	}

	public JDinkSprite showText(
			JDinkExecutionContext executionContext,
			String text,
			Integer x,
			Integer y) {
		JDinkSprite sprite = processSay(executionContext, text, x, y, null, false);
		sprite.setBrain(null);
		return sprite;
	}


	public JDinkSprite processSay(
			JDinkExecutionContext executionContext,
			String text,
			Integer x,
			Integer y) {
		return processSay(executionContext, text, x, y, null, false);
	}

	public JDinkSprite processSay(
			JDinkExecutionContext executionContext,
			String text,
			Integer x,
			Integer y,
			Integer spriteNumber,
			boolean stop) {
		int textSpriteNumber = JDinkTextUtils.processSay(executionContext, text, x, y, spriteNumber, stop);
		return executionContext.getContext().getController().getSprite(textSpriteNumber, false);
	}

}
