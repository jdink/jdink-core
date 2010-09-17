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

public class JDinkHitInteractionHandler extends AbstractJDinkInteractionHandler {

	private static final Log log = LogFactory.getLog(JDinkHitInteractionHandler.class);


	@Override
	public boolean interact(JDinkContext context, JDinkSprite sprite) {
		int x1 = sprite.getX();
		int y1 = sprite.getY();
		int x2 = x1;
		int y2 = y1;
		int directionIndex = sprite.getDirectionIndex();
		// TODO confirm boundaries
		int amount = 50;
		int amounty = 35;
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

		boolean result = false;

		int border = 10;
		JDinkRectangle r = JDinkRectangle.between(x1 - border, y1 - border, x2 + border, y2 + border);
		List<JDinkCollision> collisions = context.getCollisionTester().getCollisionsAt(
				JDinkCollisionTestType.HIT, r, sprite);
		for (JDinkCollision collision: collisions) {
			if (collision instanceof JDinkSpriteCollision) {
				JDinkSpriteCollision spriteCollision = (JDinkSpriteCollision) collision;
				JDinkSprite otherSprite = spriteCollision.getSprite();
				otherSprite.setLastHitSpriteNumber(sprite.getSpriteNumber());
				JDinkScriptInstance scriptInstance = otherSprite.getScriptInstance();
				if (scriptInstance != null) {
					JDinkScriptFunction function = scriptInstance.getFunctionByName("hit");
					if (function != null) {
						try {
							if (log.isInfoEnabled()) {
								log.info("hit to sprite " + otherSprite.getSpriteNumber());
							}
							scriptInstance.callFunction(context, function, new Object[0]);
						} catch (Throwable e) {
							log.error("failed to execute hit method due to " + e, e);
						}
					}
					result = true;
				}
				int damage = 1 + sprite.getStrength() / 2 +
						context.getRandom().nextInt(Math.max(1, sprite.getStrength() / 2));
				if (hurtSprite(context, otherSprite, damage) > 0) {
					randomBlood(context, sprite, sprite.getX(), sprite.getY() - 40);
				}
			}
		}

		return result;
	}

}
