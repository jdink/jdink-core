package de.siteof.jdink.interaction;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;

public abstract class AbstractJDinkInteractionHandler implements JDinkInteractionHandler {

	protected int hurtSprite(JDinkContext context, JDinkSprite sprite, int damage) {
		int actualDamage;
		if ((damage > 0) && (sprite.getHitPoints() > 0)) {
			actualDamage = Math.max(0, damage - sprite.getDefense());
			if (actualDamage == 0) {
				// if the damage would be zero, then randomly damage the sprite by 1
				if (context.getRandom().nextBoolean()) {
					actualDamage = 1;
				}
			}
			if (actualDamage > 0) {
				sprite.setDamage(sprite.getDamage() + actualDamage);
			}
		} else {
			actualDamage = 0;
		}
		return actualDamage;
		/*
		 *    //lets hurt this sprite but good
    if (damage < 1) return(0);
    int num = damage - spr[h].defense;

    //  Msg("num is %d.. defense was %d.of sprite %d", num, spr[h].defense, h);
    if (num < 1) num = 0;

    if (num == 0)
    {
        if ((rand() % 2)+1 == 1) num = 1;
    }

    spr[h].damage += num;
    return(num);

		 */
	}

	protected void randomBlood(JDinkContext context, JDinkSprite sprite, int x, int y) {
		int baseBloodSequenceNumber = sprite.getBaseBloodSequenceNumber();
		int bloodSequenceCount = sprite.getBloodSequenceCount();
		if ((baseBloodSequenceNumber <= 0) || (bloodSequenceCount <= 0)) {
			baseBloodSequenceNumber = 187;
			bloodSequenceCount = 3;
		}
		int sequenceNumber = baseBloodSequenceNumber + context.getRandom().nextInt(bloodSequenceCount);
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		JDinkSprite bloodSprite = spriteHelper.newSprite(x, y, sequenceNumber, 1, false);
		bloodSprite.setSpeed(0);
		bloodSprite.setBaseWalk(-1);
		bloodSprite.setNoHit(true);
		bloodSprite.setAnimationSequenceNumber(sequenceNumber);
		spriteHelper.setSpriteBrain(bloodSprite, 5); // one time background brain
		if (sprite  != null) {
			bloodSprite.setDepthHint(sprite.getY() + 1);
		}
		bloodSprite.setVisible(true);
	}

	/*
	 * void random_blood(int mx, int my, int h)
{
    //if ((rand() % 2) == 1) myseq = 188; else myseq = 187;
    //redink1 - customizable blood depending on what sprite we hit!!
    int myseq;
    int randy;

    if (spr[h].bloodseq > 0 && spr[h].bloodnum > 0)
    {
        myseq = spr[h].bloodseq;
        randy = spr[h].bloodnum;
    }
    else
    {
        myseq = 187;
        randy = 3;
    }

    myseq += (rand() % randy);

    int crap2 = add_sprite(mx,my,5,myseq,1);
    spr[crap2].speed = 0;
    spr[crap2].base_walk = -1;
    spr[crap2].nohit = 1;
    spr[crap2].seq = myseq;
    if (h > 0)
        spr[crap2].que = spr[h].y+1;

}
	 */

}
