package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.text.util.JDinkTextUtils;

public abstract class AbstractJDinkTextFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

//	private static final JDinkBrain TEXT_BRAIN = new JDinkTextBrain();

	protected Integer processSay(
			JDinkExecutionContext executionContext,
			String text,
			Integer x,
			Integer y,
			Integer spriteNumber,
			boolean stop)
			throws Exception {
		return Integer.valueOf(JDinkTextUtils.processSay(executionContext, text, x, y, spriteNumber, stop));
		/*
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
			if (stop) {
				while (sprite.getText() != null) {
					//executionContext.getContext().getView().updateView();
					//this.sleep(executionContext, 50);
					this.updateFrame(executionContext);
					this.sleep(executionContext, 50);
				}
			}

//			this.updateFrame(executionContext);
//			executionContext.getContext().getController().setChanged(true);
//			this.sleep(executionContext);
//			sprite.setText(null);
		}
		//log.warn("say_stop_xy not implemented");
		return Integer.valueOf(textSpriteNumber);
		*/
	}

}
