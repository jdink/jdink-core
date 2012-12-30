package de.siteof.jdink.brain;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain: 5</p>
 * <p>(one_time_brain)</p>
 * <p>The last frame will be used as a static image. The sprite becomes inactive though</p>
 */
public class JDinkOneTimeBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		JDinkController controller = context.getController();
		if (sprite.getAnimationSequenceNumber() == 0) {
			controller.releaseSprite(sprite.getSpriteNumber());
		}
	}

}
