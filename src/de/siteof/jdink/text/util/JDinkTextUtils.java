package de.siteof.jdink.text.util;

import de.siteof.jdink.brain.JDinkBrain;
import de.siteof.jdink.brain.JDinkTextBrain;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSprite;

public final class JDinkTextUtils {

	private static final JDinkBrain TEXT_BRAIN = new JDinkTextBrain();
	
	private JDinkTextUtils() {
		// prevent instantiation
	}

	public static int processSay(
			JDinkExecutionContext executionContext,
			String text,
			Integer x,
			Integer y,
			Integer spriteNumber,
			boolean stop) {
		int textSpriteNumber = executionContext.getContext().getController().allocateSprite();
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(textSpriteNumber, false);
		if (sprite != null) {
			JDinkSprite parentSprite = null;
			if (spriteNumber != null) {
				parentSprite = executionContext.getContext().getController().getSprite(
						spriteNumber.intValue(), false);
				sprite.setParentSprite(parentSprite);
			}
			if (x != null) {
				sprite.setX(x.intValue());
			} else if (parentSprite != null) {
				// set X offset for text, using strength var since it's unused
				// spr[crap2].strength = 75;
				sprite.setX(parentSprite.getX() - 75);
			}
			if (y != null) {
				sprite.setY(y.intValue());
			} else if (parentSprite != null) {
				// spr[crap2].defense = ( ((k[getpic(spr[crap2].owner)].box.bottom) - k[getpic(spr[crap2].owner)].yoffset) + 100);
//				parentSprite.getSequence().getOffsetY()
				int textOffsetY = -100;
				JDinkSequence sequence = parentSprite.getSequence();
				if (sequence != null) {
					JDinkSequenceFrame frame = sequence.getFrame(parentSprite.getFrameNumber(), false);
//					textOffsetY += sequence.getOffsetY();
					if (frame != null) {
						JDinkRectangle bounds = frame.getBounds();
						if (bounds != null) {
							textOffsetY -= bounds.getY();
							textOffsetY += bounds.getHeight();
						}
					}
				}
				sprite.setY(parentSprite.getY() + textOffsetY);
			}
			sprite.setText(text);
			sprite.setBrain(TEXT_BRAIN);
			executionContext.getContext().getController().setChanged(true);
			if (stop) {
				executionContext.getContext().getController().waitForView(executionContext.getContext());
				while (sprite.getText() != null) {
					//executionContext.getContext().getView().updateView();
					//this.sleep(executionContext, 50);
//					executionContext.getContext().getController().nextFrame(executionContext.getContext());
					executionContext.getContext().getView().updateView();
					executionContext.getContext().getController().sleep(50);
//					this.updateFrame(executionContext);
//					this.sleep(executionContext, 50);
				}
			}

//			this.updateFrame(executionContext);
//			executionContext.getContext().getController().setChanged(true);
//			this.sleep(executionContext);
//			sprite.setText(null);
		}
		//log.warn("say_stop_xy not implemented");
		return textSpriteNumber;
	}

}
