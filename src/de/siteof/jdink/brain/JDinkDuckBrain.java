package de.siteof.jdink.brain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.script.util.JDinkScriptUtil;

/**
 * <p>Brain Number: 3</p>
 * <p>A duck's brain.</p>
 */
public class JDinkDuckBrain extends AbstractJDinkBrain {

	private static final Log log = LogFactory.getLog(JDinkDuckBrain.class);

	private final int deadDuckBodyBaseSequenceNumber = 110;
	private final int deadDuckHeadBaseSequenceNumber = 120;
	private final int explosionSequenceNumber = 164;

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
				processDuckMovement(context, sprite);
			} else if (sprite.getBaseIdle() > 0) {
				this.setAnimationSequence(context, sprite, sprite.getBaseIdle() + sprite.getDirectionIndex());
			}
		}

/*
    int hold;


start:


    if (   (spr[h].damage > 0) && (in_this_base(spr[h].pseq, 110)  ) )
    {

        check_for_duck_script(h);

        //hit a dead duck
        int crap2 = add_sprite(spr[h].x,spr[h].y,7,164,1);
        spr[crap2].speed = 0;
        spr[crap2].base_walk = 0;
        spr[crap2].seq = 164;
        draw_damage(h);
        spr[h].damage = 0;
        add_exp(spr[h].exp, h);

        kill_sprite_all(h);

        return;
    }


    if (   (spr[h].damage > 0) && (in_this_base(spr[h].pseq, spr[h].base_walk)  ) )
    {
        //SoundPlayEffect( 1,3000, 800 );
        draw_damage(h);
        add_exp(spr[h].exp, h);
        spr[h].damage = 0;

        //lets kill the duck here, ha.
        check_for_kill_script(h);
        spr[h].follow = 0;
        int crap = add_sprite(spr[h].x,spr[h].y,5,1,1);
        spr[crap].speed = 0;
        spr[crap].base_walk = 0;
        spr[crap].size = spr[h].size;
        spr[crap].speed =  ((rand() % 3)+1);


        spr[h].base_walk = 110;
        spr[h].speed = 1;
        spr[h].timer = 0;
        spr[h].wait = 0;
        spr[h].frame = 0;

        if (spr[h].dir == 0) spr[h].dir = 1;
        if (spr[h].dir == 4) spr[h].dir = 7;
        if (spr[h].dir == 6) spr[h].dir = 3;

        changedir(spr[h].dir,h,spr[h].base_walk);
        spr[crap].dir = spr[h].dir;
        spr[crap].base_walk = 120;
        changedir(spr[crap].dir,crap,spr[crap].base_walk);


        automove(h);
        return;
    }


    if (spr[h].move_active)
    {
        process_move(h);
        return;
    }

    if (spr[h].freeze)
    {
        return;
    }


    if (spr[h].follow > 0)
    {
        process_follow(h);
        return;
    }



    if (spr[h].base_walk == 110)
    {
        if ( (rand() % 100)+1 == 1)
            random_blood(spr[h].x, spr[h].y-18, h);
        goto walk;
    }





    if (spr[h].seq == 0 )
    {

        if (((rand() % 12)+1) == 1 )
        {
            hold = ((rand() % 9)+1);

            if ((hold != 2) && (hold != 8) && (hold != 5))
            {

                //Msg("random dir change started.. %d", hold);
                changedir(hold,h,spr[h].base_walk);

            }
            else
            {
                int junk = spr[h].size;

                if (junk >=  100)
                    junk = 18000 - (junk * 50);

                if (junk < 100)
                    junk = 16000 + (junk * 100);

                SoundPlayEffect( 1,junk, 800,h ,0);
                spr[h].mx = 0;
                spr[h].my = 0;
                spr[h].wait = thisTickCount + (rand() % 300)+200;

            }
            return;
        }

        if ((spr[h].mx != 0) || (spr[h].my != 0))

        {
            spr[h].seq = spr[h].seq_orig;

        }

    }


walk:
    if (spr[h].y > playy)

    {
        changedir(9,h,spr[h].base_walk);
    }



    if (spr[h].x > playx-30)

    {
        changedir(7,h,spr[h].base_walk);
    }

    if (spr[h].y < 10)
    {
        changedir(1,h,spr[h].base_walk);
    }

    if (spr[h].x < 30)
    {
        changedir(3,h,spr[h].base_walk);
    }

    //   Msg("Duck dir is %d, seq is %d.", spr[h].dir, spr[h].seq);
    automove(h);

    if (check_if_move_is_legal(h) != 0)

    {
        if (spr[h].dir != 0)
            changedir(autoreverse_diag(h),h,spr[h].base_walk);
    }
 */
	}

	private void processDuckMovement(JDinkContext context, JDinkSprite sprite) {
//		processMovement(context, sprite);
//		processRandomMovement(context, sprite);
		long time = getTime(context);
		if ((sprite.getAnimationSequenceNumber() == 0) &&
				((sprite.getBaseWalk() != -1))) {
			if (randomInt(context, 12) == 0) {
				int hold = randomInt(context, 9) + 1;
	            if ((hold != JDinkDirectionIndexConstants.DOWN) &&
	            		(hold != JDinkDirectionIndexConstants.UP) &&
	            		(hold != 5)) {
	            	changeDirection(context, sprite, hold);
	                sprite.setMoveWaitTime(time +(randomInt(context, 300) + 200));
	            } else {
	            	// TODO sound
	            	sprite.setMx(0);
	            	sprite.setMy(0);
	                sprite.setMoveWaitTime(time +(randomInt(context, 300) + 200));
	            }
			}
			if ((sprite.getAnimationSequenceNumber() == 0) &&
					(sprite.getMx() != 0) || (sprite.getMy() != 0)) {
				sprite.setAnimationSequenceNumber(sprite.getOriginalAnimationSequenceNumber());
			}
		}

		autoChangeDirectionOnBounds(context, sprite,
				this.getDefaultMovementBounds(context, sprite));

	    if (automove(context, sprite) != null) {
	        sprite.setMoveWaitTime(getTime(context) + 400);
	        changeDirection(context, sprite, autoreverse_diag(context, sprite));
	    }
		autoRepeatAnimation(context, sprite);
	    context.getController().notifyChanged(sprite, JDinkController.ALL_CHANGE);
	}

	@Override
	protected boolean processDamage(JDinkContext context, JDinkSprite sprite) {
		boolean result = false;
		int damage = sprite.getDamage();

		if ((damage > 0) && (in_this_base(sprite.getSequenceNumber(), deadDuckBodyBaseSequenceNumber))) {
			showDamage(context, sprite);
			this.add_exp(context, sprite, sprite.getExperience());
			sprite.setDamage(0);

			try {
				JDinkScriptUtil.callScript(context, sprite.getScriptInstance(), "duckdie");
			} catch (Throwable e) {
				log.warn("[processDamage] failed to call duckdie", e);
			}

			if (sprite.isActive()) {
				JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
				JDinkSprite explosionSprite = spriteHelper.newSprite(
						sprite.getX(), sprite.getY(), explosionSequenceNumber, 1, false);
				spriteHelper.setSpriteBrain(explosionSprite, 7); // one time
				explosionSprite.setSpeed(0);
				explosionSprite.setBaseWalk(0);
				explosionSprite.setAnimationSequenceNumber(explosionSequenceNumber);
				explosionSprite.setVisible(true);

				context.getController().releaseSprite(sprite);
			}
			result = true;
		}

		if ((damage > 0) && (in_this_base(sprite.getSequenceNumber(), sprite.getBaseWalk()))) {
			showDamage(context, sprite);
			this.add_exp(context, sprite, sprite.getExperience());
			sprite.setDamage(0);

			try {
				JDinkScriptUtil.callScript(context, sprite.getScriptInstance(), "die");
			} catch (Throwable e) {
				log.warn("[processDamage] failed to call die", e);
			}

			if (sprite.isActive()) {
				JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
				JDinkSprite headSprite = spriteHelper.newSprite(sprite.getX(), sprite.getY(), 1, 1, false);
				spriteHelper.setSpriteBrain(headSprite, 5); // one time background
				headSprite.setBaseWalk(0);
				headSprite.setSize(sprite.getSize());
				headSprite.setSpeed(1 + randomInt(context, 3));

				sprite.setFollowSpriteNumber(0);
				sprite.setBaseWalk(deadDuckBodyBaseSequenceNumber);
				sprite.setSpeed(1);
				sprite.setTiming(0);
				sprite.setNextAnimationTime(0);
				sprite.setAnimationFrameNumber(0);

				int directionIndex = sprite.getDirectionIndex();
				switch (directionIndex) {
				case 0:
					directionIndex = JDinkDirectionIndexConstants.DOWN_LEFT;
					break;
				case JDinkDirectionIndexConstants.LEFT:
					directionIndex = JDinkDirectionIndexConstants.UP_LEFT;
					break;
				case JDinkDirectionIndexConstants.RIGHT:
					directionIndex = JDinkDirectionIndexConstants.DOWN_RIGHT;
					break;
				}
				this.changeDirection(context, sprite, directionIndex);

				headSprite.setBaseWalk(deadDuckHeadBaseSequenceNumber);
				this.changeDirection(context, headSprite, directionIndex);
				headSprite.setVisible(true);

				automove(context, sprite);
			}
			result = true;
		}
		return result;
	}

}
