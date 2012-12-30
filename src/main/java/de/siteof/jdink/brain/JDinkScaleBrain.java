package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain Number: 12</p>
 * <p>Scales the sprite down and kills it once it hits a certain size</p>
 */
public class JDinkScaleBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		int brainParameter = sprite.getBrainParameter(0);
		int size = sprite.getSize();
	    if (size == brainParameter) {
	    	context.getController().releaseSprite(sprite.getSpriteNumber());
	    }

	    int num = 5 * (context.getController().getBaseTiming() / 4);

	    if (size > brainParameter) {
	        size = Math.max(brainParameter, size - num);
	    } else if (size < brainParameter) {
	        size = Math.min(brainParameter, size - num);
	    }

	    if (size != sprite.getSize()) {
	    	sprite.setSize(size);
	    	context.getController().setChanged(true);
	    }

	    // TODO:
//	    if (spr[h].move_active)
//	    {
//	        process_move(h);
//	        return;
//	    }
//
//	    if (spr[h].dir > 0)
//	    {
//	        changedir(spr[h].dir,h,-1);
//	        automove(h);
//	    }
	}

}
