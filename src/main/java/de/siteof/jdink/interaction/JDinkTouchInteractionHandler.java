package de.siteof.jdink.interaction;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.collision.JDinkCollision;
import de.siteof.jdink.collision.JDinkCollisionTestType;
import de.siteof.jdink.collision.JDinkSpriteCollision;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkScriptFunction;
import de.siteof.jdink.script.JDinkScriptInstance;

public class JDinkTouchInteractionHandler extends AbstractJDinkInteractionHandler {

	private static final Log log = LogFactory.getLog(JDinkTouchInteractionHandler.class);


	@Override
	public boolean interact(JDinkContext context, JDinkSprite sprite) {
		boolean result = false;
		long time = context.getTime();
		if ((sprite.isNoTouch()) && (sprite.getNoTouchTime() < time)) {
			sprite.setNoTouch(false);
		}
		if (!sprite.isNoTouch()) {
			int x1 = sprite.getX();
			int y1 = sprite.getY();
			int x2 = x1;
			int y2 = y1;
			int directionIndex = sprite.getDirectionIndex();
			int amount = 0;
			int amounty = 0;
			switch (directionIndex) {
			case JDinkDirectionIndexConstants.RIGHT:
				x2 += amount;
				break;
			case JDinkDirectionIndexConstants.LEFT:
				x1 -= amount;
				break;
			case JDinkDirectionIndexConstants.DOWN:
				y2 += amounty;
				break;
			case JDinkDirectionIndexConstants.UP:
				y1 -= amounty;
				break;
			}

			int border = 2;
			JDinkRectangle r = JDinkRectangle.between(x1 - border, y1 - border, x2 + border, y2 + border);
			List<JDinkCollision> collisions = context.getCollisionTester().getCollisionsAt(
					JDinkCollisionTestType.TOUCH, r, sprite);
			for (JDinkCollision collision: collisions) {
				if (collision instanceof JDinkSpriteCollision) {
					JDinkSpriteCollision spriteCollision = (JDinkSpriteCollision) collision;
					JDinkSprite otherSprite = spriteCollision.getSprite();
//					if ((otherSprite.isNoTouch()) && (otherSprite.getNoTouchTime() < time)) {
//						otherSprite.setNoTouch(false);
//					}
//					if ((!otherSprite.isNoTouch()) && (otherSprite.getTouchDamage() != 0)) {
					if (otherSprite.getTouchDamage() != 0) {
						if (otherSprite.getTouchDamage() > 0) {
							sprite.setNoTouch(true);
							sprite.setNoTouchTime(time + 400);
							sprite.setLastHitSpriteNumber(otherSprite.getSpriteNumber());
						}
						JDinkScriptInstance scriptInstance = otherSprite.getScriptInstance();
						if (scriptInstance != null) {
							JDinkScriptFunction function = scriptInstance.getFunctionByName("touch");
							if (function != null) {
								try {
									if (log.isInfoEnabled()) {
										log.info("touching to sprite " + otherSprite.getSpriteNumber());
									}
									scriptInstance.callFunction(context, function, new Object[0]);
								} catch (Throwable e) {
									log.error("failed to execute touch method due to " + e, e);
								}
							}
							result = true;
						}
						if (hurtSprite(context, sprite, otherSprite.getTouchDamage()) > 0) {
							randomBlood(context, sprite, sprite.getX(), sprite.getY() - 40);
						}
						/*
						 *
	                        spr[h].notouch = true;
	                        spr[h].notouch_timer = thisTickCount+400;
	                        spr[h].last_hit = i;
	                        if (spr[i].script != 0)
	                            if (locate(spr[i].script, "TOUCH")) run_script(spr[i].script);
	                        if (hurt_thing(h, spr[i].touch_damage, 0) > 0)
	                            random_blood(spr[h].x, spr[h].y-40, h);

						 */
					}
				}
			}
		}

		return result;
	}

}
