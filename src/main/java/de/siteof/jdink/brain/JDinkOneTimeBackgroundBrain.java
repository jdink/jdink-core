package de.siteof.jdink.brain;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain: 7</p>
 * <p>(one_time_brain_for_real)</p>
 */
public class JDinkOneTimeBackgroundBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		JDinkController controller = context.getController();
		if (sprite.getAnimationSequenceNumber() == 0) {
//			controller.releaseSprite(sprite.getSpriteNumber());
			if (!sprite.isBackground()) {
				sprite.setBackground(true);
				controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
			}
		}
	}

}
