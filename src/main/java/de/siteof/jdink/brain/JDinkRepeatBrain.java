package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain Number: 6</p>
 * <p>Repeats a sequence.</p>
 */
public class JDinkRepeatBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		// TODO repeat_brain
		this.processBehaviours(context, sprite);

//		if (sprite.getOriginalAnimationSequenceNumber() == 0) {
//			sprite.setOriginalAnimationSequenceNumber(sprite.getAnimationSequenceNumber());
//			sprite
//		}

		if (sprite.getAnimationSequenceNumber() == 0) {
			sprite.setAnimationSequenceNumber(sprite.getOriginalAnimationSequenceNumber());
		}

/*
 *         if (spr[h].seq_orig == 0) if (spr[h].sp_index != 0)
    {
        spr[h].seq_orig = pam.sprite[spr[h].sp_index].seq;
        spr[h].frame = pam.sprite[spr[h].sp_index].frame;
        spr[h].wait = 0;

        //pam.sprite[spr[h].sp_index].frame;

    }

    if (spr[h].seq == 0) spr[h].seq = spr[h].seq_orig;

 */
	}

}
