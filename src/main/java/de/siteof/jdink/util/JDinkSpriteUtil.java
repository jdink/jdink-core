package de.siteof.jdink.util;

import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.view.JDinkImage;

public final class JDinkSpriteUtil {
	
	private JDinkSpriteUtil() {
		// prevent instantiation
	}

	public static int getEffectiveFrameDelay(JDinkContext context, JDinkSprite sprite) {
		int delay = sprite.getFrameDelay();
		if (delay == 0) {
			delay = sprite.getTiming();
		}
		if (delay == 0) {
			JDinkSequenceFrameAttributes frameAttributes =
				context.getFrameAttributes(sprite.getAnimationSequenceNumber(),
						sprite.getAnimationFrameNumber(), false);
			if (frameAttributes != null) {
				delay = frameAttributes.getDelay();
			}
//			JDinkSequence sequence = sprite.getSequence();
//			if (sequence != null) {
//				JDinkSequenceFrame frame = sequence.getFrame(
//						sprite.getAnimationFrameNumber(), false);
//				if (frame != null) {
//					delay = frame.getDelay();
//				}
//			}
		}
		return delay;
	}

	public static JDinkShape getBounds(JDinkContext context, JDinkSprite sprite) {
		JDinkShape bounds = sprite.getBounds();
		if (bounds == null) {
			JDinkSequence sequence = sprite.getSequence();
			JDinkSequenceFrame frame = (sequence != null ? sequence.getFrame(sprite.getFrameNumber(), false) : null);
			if (frame != null) {
				JDinkImage image = context.getImage(sequence, frame);
				if (image != null) {
					bounds = JDinkRectangle.getInstance(0, 0, image.getWidth(), image.getHeight());
				}
			}
		}
		if (bounds == null) {
			bounds = JDinkRectangle.EMPTY;
		}
		return bounds;
	}

}
