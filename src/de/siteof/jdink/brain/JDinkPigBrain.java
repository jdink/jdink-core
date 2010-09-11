package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain Number: 4</p>
 * <p>A pig's brain.</p>
 */
public class JDinkPigBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context,JDinkSprite sprite) {
		// TODO Auto-generated method stub
		// TODO pill_brain
		if (!this.processBehaviours(context, sprite)) {
			if (!sprite.isFrozen()) {
				processPigMovement(context, sprite);
			}
		}

/*
    int hold;


start:

    if (spr[h].move_active)
    {
        process_move(h);
        return;
    }

    if (   (spr[h].damage > 0) )
    {
        //SoundPlayEffect( 1,3000, 800 );
        draw_damage(h);
        spr[h].hitpoints -= spr[h].damage;
        spr[h].damage = 0;
        if (spr[h].hitpoints < 1)
        {
            add_exp(spr[h].exp, h);
            spr[h].damage = 0;
            //lets kill the duck here, ha.
            check_for_kill_script(h);
            spr[h].speed = 0;
            spr[h].base_walk = -1;
            spr[h].seq = 164;
            spr[h].brain = 7;
        }

        return;
    }


    if (spr[h].freeze) return;



    if (spr[h].seq == 0 )
    {

        if (((rand() % 12)+1) == 1 )
        {
            hold = ((rand() % 9)+1);

            if (  (hold != 4) &&   (hold != 6) &&  (hold != 2) && (hold != 8) && (hold != 5))
            {
                changedir(hold,h,spr[h].base_walk);

            }
            else
            {
                int junk = spr[h].size;

                if (junk >=  100)
                    junk = 18000 - (junk * 50);

                if (junk < 100)
                    junk = 16000 + (junk * 100);


                hold = ((rand() % 4)+1);

                if (!playing(spr[h].last_sound)) spr[h].last_sound = 0;

                if (spr[h].last_sound == 0)
                {


                    if (hold == 1)
                        spr[h].last_sound = SoundPlayEffect( 2,junk, 800 ,h,0);
                    if (hold == 2)
                        spr[h].last_sound = SoundPlayEffect( 3,junk, 800,h ,0);
                    if (hold == 3)
                        spr[h].last_sound = SoundPlayEffect( 4,junk, 800 ,h,0);
                    if (hold == 4)
                        spr[h].last_sound = SoundPlayEffect( 5,junk, 800,h,0 );

                }

                spr[h].mx = 0;
                spr[h].my = 0;
                spr[h].wait = thisTickCount + (rand() % 300)+200;

            }

        }
        else
        {

            if ((spr[h].mx != 0) || (spr[h].my != 0))

            {
                spr[h].seq = spr[h].seq_orig;

            }

        }
    }


    if (spr[h].y > (playy-k[getpic(h)].box.bottom / 4))
    {
        changedir(9,h,spr[h].base_walk);
    }

    if (spr[h].x > (x-k[getpic(h)].box.right-10))
    {
        changedir(1,h,spr[h].base_walk);
    }

    if (spr[h].y < 10)
    {
        changedir(1,h,spr[h].base_walk);
    }

    if (spr[h].x < 10)
    {
        changedir(3,h,spr[h].base_walk);
    }

    automove(h);

    if (check_if_move_is_legal(h) != 0)

    {
        changedir(autoreverse_diag(h),h,spr[h].base_walk);
    }

 */
	}

	private void processPigMovement(JDinkContext context, JDinkSprite sprite) {
		long time = getTime(context);
		if ((sprite.getAnimationSequenceNumber() == 0) &&
				((sprite.getBaseWalk() != -1) || (sprite.getMoveWaitTime() < getTime(context)))) {
			boolean changeDirection = (randomInt(context, 12) == 0);
            if ((sprite.getAnimationSequenceNumber() == 0) ||
            		(in_this_base(sprite.getAnimationSequenceNumber(), sprite.getBaseAttack()))) {
            	changeDirection = true;
            }
			if (changeDirection) {
				int hold = randomInt(context, 9) + 1;
	            if ((hold != 4) &&   (hold != 6) &&  (hold != 2) && (hold != 8) && (hold != 5)) {
	            	changeDirection(context, sprite, hold);
	                sprite.setMoveWaitTime(time +(randomInt(context, 300) + 200));
	            } else {
	            	sprite.setMx(0);
	            	sprite.setMy(0);
	                sprite.setMoveWaitTime(time +(randomInt(context, 300) + 200));
	            }
			} else {
	            //keep going the same way
			}
		}

		// TODO the pig's brain had different movement boundaries...
		// but the one for x2 wasn't implemented correctly
		// probably was never necessary?
		autoChangeDirectionOnBounds(context, sprite,
				this.getDefaultMovementBounds(context, sprite));

	    if (automove(context, sprite) != null) {
	        sprite.setMoveWaitTime(getTime(context) + 400);
	        changeDirection(context, sprite, autoreverse_diag(context, sprite));
	    }
	    context.getController().setChanged(true);
	}

}
