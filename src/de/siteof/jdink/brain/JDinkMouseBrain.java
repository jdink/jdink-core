/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.brain;

import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSprite;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkMouseBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		JDinkSequence sequence = sprite.getSequence();
		JDinkSequenceFrame frame = (sequence != null ? sequence.getFrame(sprite.getFrameNumber(), false) : null);
		if ((frame != null) && (frame.getBounds() != null)) {
			frame.setBounds(frame.getBounds().getLocatedTo(0, 0));
//			frame.getBounds().setLocation(0, 0);
		}
		if (context.getController().getMousePosition() != null) {
			JDinkPoint mousePosition = context.getController().getMousePosition();
			int x = mousePosition.getX();
			int y = mousePosition.getY();
			if ((x != sprite.getX()) || (y != sprite.getY())) {
				sprite.setX(x);
				sprite.setY(y);
				context.getController().setChanged(true);
//				log.debug("mouse position changed (" + x + ", " + y + ")");
//				log.debug("seq=" + sprite.getSeqNumber() + "=" + sprite.getSequence() + " frame=" +
//						sprite.getFrameNumber() + "=" + (sprite.getSequence() != null ? sprite.getSequence().getFrame(
//								sprite.getFrameNumber(), false) : null));
			} else {
				//log.debug("mouse position did not change (" + x + ", " + y + ")");
			}
		}
	}

}
