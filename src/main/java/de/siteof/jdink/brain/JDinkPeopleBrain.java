package de.siteof.jdink.brain;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Brain Number: 16</p>
 * <p>A person's brain.</p>
 */
public class JDinkPeopleBrain extends AbstractJDinkBrain {

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		// TODO people_brain
		if (!this.processBehaviours(context, sprite)) {
			// do people movement
			if (!sprite.isFrozen()) {
				processPeopleMovement(context, sprite);
			} else if (sprite.getBaseIdle() > 0) {
				this.setAnimationSequence(context, sprite, sprite.getBaseIdle() + sprite.getDirectionIndex());
			}
		}


/*
 *     int hold;

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

                if (spr[h].brain == 9)
                {
                    if (spr[h].dir == 0) spr[h].dir = 3;
                    change_dir_to_diag(&spr[h].dir);
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

    }


    if (spr[h].target != 0)
    {

        if (in_this_base(spr[h].seq, spr[h].base_attack))
        {
            //still attacking
            return;
        }





        int dir;
        if (spr[h].distance == 0) spr[h].distance = 5;
        int distance = get_distance_and_dir(h, spr[h].target, &dir);

        if (distance < spr[h].distance) if (spr[h].attack_wait < thisTickCount)
        {
            //  Msg("base attack is %d.",spr[h].base_attack);
            if (spr[h].base_attack != -1)
            {
                int attackdir;
                bool old_smooth_follow = smooth_follow;
                smooth_follow = false;
                get_distance_and_dir(h, spr[h].target, &attackdir);
                smooth_follow = old_smooth_follow;
                //Msg("attacking with %d..", spr[h].base_attack+dir);

                spr[h].dir = attackdir;

                spr[h].seq = spr[h].base_attack+spr[h].dir;
                spr[h].frame = 0;

                if (spr[h].script != 0)
                    if (locate(spr[h].script, "ATTACK")) run_script(spr[h].script); else
                        spr[h].move_wait = thisTickCount + ((rand() % 300)+10);;
                return;

            }

        }



        if (spr[h].move_wait  < thisTickCount)
        {
            process_target(h);
            spr[h].move_wait = thisTickCount + 200;

        }
        else
        {
            /*  automove(h);

            if (check_if_move_is_legal(h) != 0)
            {

            }
            * /

            goto walk_normal;
        }

        return;
    }



walk_normal:

    if (spr[h].base_walk != -1)
    {
        if ( spr[h].seq == 0) goto recal;
    }

    if (( spr[h].seq == 0) && (spr[h].move_wait < thisTickCount))
    {
recal:
        if (((rand() % 12)+1) == 1 )
        {
            hold = ((rand() % 9)+1);
            if (  (hold != 4) &&   (hold != 6) &&  (hold != 2) && (hold != 8) && (hold != 5))
            {
                changedir(hold,h,spr[h].base_walk);
                spr[h].move_wait = thisTickCount +((rand() % 2000)+200);

            }

        } else
        {
            //keep going the same way
            if (in_this_base(spr[h].seq_orig, spr[h].base_attack)) goto recal;
            spr[h].seq = spr[h].seq_orig;
            if (spr[h].seq_orig == 0) goto recal;
        }

    }



    if (spr[h].y > (playy - 15))

    {
        changedir(9,h,spr[h].base_walk);
    }

    if (spr[h].x > (playx - 15))

    {
        changedir(1,h,spr[h].base_walk);
    }

    if (spr[h].y < 18)
    {
        changedir(1,h,spr[h].base_walk);
    }

    if (spr[h].x < 18)
    {
        changedir(3,h,spr[h].base_walk);
    }

    automove(h);

    if (check_if_move_is_legal(h) != 0)
    {
        spr[h].move_wait = thisTickCount + 400;
        changedir(autoreverse_diag(h),h,spr[h].base_walk);
    }


    //              changedir(hold,h,spr[h].base_walk);

 */
	}

	private void processPeopleMovement(JDinkContext context, JDinkSprite sprite) {
		processRandomMovement(context, sprite);
	}

}
