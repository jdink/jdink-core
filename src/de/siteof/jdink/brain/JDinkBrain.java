package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Implements a certain "brain" which is a behaviour pattern.</p>
 */
public interface JDinkBrain {

	void update(JDinkContext context, JDinkSprite sprite);

}
