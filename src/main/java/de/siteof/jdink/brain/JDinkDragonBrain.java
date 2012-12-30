package de.siteof.jdink.brain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkDirection;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.util.JDinkScriptUtil;

/**
 * <p>Brain Number: 10</p>
 * <p>A dragon's brain.</p>
 */
public class JDinkDragonBrain extends AbstractJDinkBrain {

	private static final Log log = LogFactory.getLog(JDinkDragonBrain.class);

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		// TODO Auto-generated method stub
		// TODO pill_brain
		boolean processed = false;
		if (!processed) {
			processed = this.processDamage(context, sprite);
		}
		if (!processed) {
			processed = this.processBehaviours(context, sprite);
		}
		if (!processed) {
			if (!sprite.isFrozen()) {
				processDragonMovement(context, sprite);
			} else if (sprite.getBaseIdle() > 0) {
				this.setAnimationSequence(context, sprite, sprite.getBaseIdle() + sprite.getDirectionIndex());
			}
		}

/*
    int hold;


    if  (spr[h].damage > 0)
    {
        //got hit
        //SoundPlayEffect( 1,3000, 800 );
        if (spr[h].hitpoints > 0)
        {
            draw_damage(h);
            if (spr[h].damage > spr[h].hitpoints) spr[h].damage = spr[h].hitpoints;
            spr[h].hitpoints -= spr[h].damage;

            if (spr[h].hitpoints < 1)
            {
                //they killed it

                check_for_kill_script(h);
                if (spr[h].brain == 10)
                {
                    add_kill_sprite(h);
                    spr[h].active = false;

                }

                return;

            }
        }
        spr[h].damage = 0;
    }


    if (spr[h].move_active)
    {
        process_move(h);
        return;
    }


    if (spr[h].freeze) return;


    if (spr[h].follow > 0)
    {
        process_follow(h);
        return;
    }

    if (spr[h].target != 0)
        if (spr[h].attack_wait < thisTickCount)
        {
            if (spr[h].script != 0)
            {

                if (locate(spr[h].script, "ATTACK")) run_script(spr[h].script);
            }
        }



        if (spr[h].seq == 0)
        {
recal:
            if (((rand() % 12)+1) == 1 )
            {
                hold = ((rand() % 9)+1);
                if (  (hold != 1) &&   (hold != 3) &&  (hold != 7) && (hold != 9) && (hold != 5))
                {
                    changedir(hold,h,spr[h].base_walk);

                }

            } else
            {
                //keep going the same way
                spr[h].seq = spr[h].seq_orig;
                if (spr[h].seq_orig == 0) goto recal;
            }

        }


        if (spr[h].y > playy)

        {
            changedir(8,h,spr[h].base_walk);
        }

        if (spr[h].x > x)
        {
            changedir(4,h,spr[h].base_walk);
        }

        if (spr[h].y < 0)
        {
            changedir(2,h,spr[h].base_walk);
        }

        if (spr[h].x < 0)
        {
            changedir(6,h,spr[h].base_walk);
        }

        automove(h);

        if (check_if_move_is_legal(h) != 0)

        {

            int mydir = autoreverse(h);

            //  Msg("Real dir now is %d, autoresver changed to %d.",spr[h].dir, mydir);

            changedir(mydir,h,spr[h].base_walk);

            Msg("real dir changed to %d",spr[h].dir);
        }
 */
	}

	private void processDragonMovement(JDinkContext context, JDinkSprite sprite) {
		boolean processed = this.processFollowMovement(context, sprite);
		if (!processed) {
			processed = this.processTargetMovement(context, sprite);
		}

		autoChangeDirectionOnBounds(context, sprite,
				this.getDefaultMovementBounds(context, sprite));

	    if (automove(context, sprite) != null) {
//	        sprite.setMoveWaitTime(getTime(context) + 400);
	        changeDirection(context, sprite, autoreverse_diag(context, sprite));
	    }
		autoRepeatAnimation(context, sprite);
	    context.getController().notifyChanged(sprite, JDinkController.ALL_CHANGE);
	}

	@Override
	protected boolean isValidDirectionIndex(JDinkContext context, JDinkSprite sprite,
			int directionIndex) {
		return JDinkDirection.isStraight(directionIndex);
	}

	@Override
	protected boolean processTargetMovement(
			JDinkContext context, JDinkSprite sprite,
			JDinkSprite targetSprite, JDinkRectangle movementBounds) {
		boolean result = false;
		if (sprite.getAttackWaitTime() < getTime(context)) {
			try {
				JDinkScriptUtil.callScript(context, sprite.getScriptInstance(), "attack");
			} catch (Throwable e) {
				log.warn("[processTargetMovement] failed to execute attack", e);
			}

			if (sprite.getAnimationSequenceNumber() == 0) {
				boolean changeDirection =
					(sprite.getOriginalAnimationSequenceNumber() == 0) ||
					(randomInt(context, 12) == 0);
				if (changeDirection) {
					changeRandomDirection(context, sprite);
				} else {
					sprite.setAnimationFrameNumber(sprite.getOriginalAnimationSequenceNumber());
				}
			}
		}
		return result;
/*
        if (spr[h].attack_wait < thisTickCount)
        {
            if (spr[h].script != 0)
            {

                if (locate(spr[h].script, "ATTACK")) run_script(spr[h].script);
            }
        }



        if (spr[h].seq == 0)
        {
recal:
            if (((rand() % 12)+1) == 1 )
            {
                hold = ((rand() % 9)+1);
                if (  (hold != 1) &&   (hold != 3) &&  (hold != 7) && (hold != 9) && (hold != 5))
                {
                    changedir(hold,h,spr[h].base_walk);

                }

            } else
            {
                //keep going the same way
                spr[h].seq = spr[h].seq_orig;
                if (spr[h].seq_orig == 0) goto recal;
            }

 */
	}

	@Override
	protected void processKilled(JDinkContext context, JDinkSprite sprite) {
		int brainNumber = sprite.getBrainNumber();
		callDieScript(context, sprite);
		if (brainNumber == sprite.getBrainNumber()) {
			this.add_kill_sprite(context, sprite);
			context.getController().releaseSprite(sprite);
		}
	}

}
