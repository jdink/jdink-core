package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain Number: 0</p>
 * <p>The default brain.</p>
 */
public class JDinkDefaultBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		if (!this.processBehaviours(context, sprite)) {
		}
	}

}
