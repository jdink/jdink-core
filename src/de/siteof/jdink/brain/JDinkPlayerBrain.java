package de.siteof.jdink.brain;

import java.util.EventObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.collision.JDinkCollision;
import de.siteof.jdink.collision.JDinkCollisionTestType;
import de.siteof.jdink.collision.JDinkCollisionTester;
import de.siteof.jdink.collision.JDinkSpriteCollision;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.events.JDinkKeyConstants;
import de.siteof.jdink.events.JDinkKeyEvent;
import de.siteof.jdink.format.map.JDinkMapSpritePlacement;
import de.siteof.jdink.interaction.JDinkInteractionType;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkMapTransition;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.util.JDinkScriptUtil;
import de.siteof.jdink.util.debug.JDinkObjectOutputUtil;

/**
 * <p>Brain: 1</p>
 */
public class JDinkPlayerBrain extends AbstractJDinkBrain {

	private static final Log log = LogFactory.getLog(JDinkPlayerBrain.class);

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		boolean processed = false;
		// TODO find where this is being done in the original game
		int life = context.getGlobalVariables().life.getInt(context);
		sprite.setHitPoints(life);
		if (!processed) {
			processed = this.processDamage(context, sprite);
			int updatedLife = sprite.getHitPoints();
			context.getGlobalVariables().life.setInt(context, updatedLife);
			if ((updatedLife <= 0) && (updatedLife != life)) {
				try {
					log.info("[update] player died, calling dinfo.die");
					JDinkScriptUtil.callStatelessScript(context, "dinfo", "die");
					processed = true;
				} catch (Throwable e) {
					log.warn("[update] failed to execute dinfo.die due to " + e, e);
				}
			}
		}
		if (!processed) {
			processed = this.processBehaviours(context, sprite);
		}
		if (!processed) {
			if (!sprite.isBusy()) {
				sprite.setBusy(true);
				try {
					this.processPlayerMovement(context, sprite);
				} finally {
					sprite.setBusy(false);
				}
			}
		}
	}

	public void processPlayerMovement(JDinkContext context, JDinkSprite sprite) {
		JDinkController controller = context.getController();
		boolean left = (controller.isKeyPressed(JDinkKeyConstants.VK_LEFT)) || (controller.isKeyPressed(JDinkKeyConstants.VK_4));
		boolean right = (controller.isKeyPressed(JDinkKeyConstants.VK_RIGHT)) || (controller.isKeyPressed(JDinkKeyConstants.VK_6));
		boolean up = (controller.isKeyPressed(JDinkKeyConstants.VK_UP)) || (controller.isKeyPressed(JDinkKeyConstants.VK_8));
		boolean down = (controller.isKeyPressed(JDinkKeyConstants.VK_DOWN)) || (controller.isKeyPressed(JDinkKeyConstants.VK_2));
		for (EventObject event: controller.getEvents()) {
			if (event instanceof JDinkKeyEvent) {
				JDinkKeyEvent keyEvent = (JDinkKeyEvent) event;
				if (keyEvent.isKeyPressed()) {
					switch (keyEvent.getKeyCode()) {
					case JDinkKeyConstants.VK_UP:
						up = true;
						break;
					case JDinkKeyConstants.VK_DOWN:
						down = true;
						break;
					case JDinkKeyConstants.VK_LEFT:
						left = true;
						break;
					case JDinkKeyConstants.VK_RIGHT:
						right = true;
						break;
					}
				}
			}
		}
		if ((left) && (right)) {
			// we can't go into the opposite direction at the same time
			left	= false;
			right	= false;
		}
		if ((up) && (down)) {
			// we can't go into the opposite direction at the same time
			up		= false;
			down	= false;
		}
		if (sprite.isNoControl()) {
			left	= false;
			right	= false;
			up		= false;
			down	= false;
		}
		// determine the direction index based on the keys pressed
		int directionIndex = 0;
		if (up) {
			if (left) {
				directionIndex	= JDinkDirectionIndexConstants.UP_LEFT;
			} else if (right) {
				directionIndex	= JDinkDirectionIndexConstants.UP_RIGHT;
			} else {
				directionIndex	= JDinkDirectionIndexConstants.UP;
			}
		} else if (down) {
			if (left) {
				directionIndex	= JDinkDirectionIndexConstants.DOWN_LEFT;
			} else if (right) {
				directionIndex	= JDinkDirectionIndexConstants.DOWN_RIGHT;
			} else {
				directionIndex	= JDinkDirectionIndexConstants.DOWN;
			}
		} else {
			if (left) {
				directionIndex	= JDinkDirectionIndexConstants.LEFT;
			} else if (right) {
				directionIndex	= JDinkDirectionIndexConstants.RIGHT;
			}
		}
		if (directionIndex > 0) {
			sprite.setDirectionIndex(directionIndex);
		}
		int deltaX = 0;
		int deltaY = 0;
		int deltaValue = 5;
		int deltaDiagonal = Math.max(1, deltaValue - deltaValue / 3);
		switch (directionIndex) {
		case JDinkDirectionIndexConstants.UP_LEFT:
			deltaX = -deltaDiagonal;
			deltaY = -deltaDiagonal;
			break;
		case JDinkDirectionIndexConstants.UP:
			deltaX = 0;
			deltaY = -deltaValue;
			break;
		case JDinkDirectionIndexConstants.UP_RIGHT:
			deltaX = deltaDiagonal;
			deltaY = -deltaDiagonal;
			break;
		case JDinkDirectionIndexConstants.LEFT:
			deltaX = -deltaValue;
			deltaY = 0;
			break;
		case JDinkDirectionIndexConstants.RIGHT:
			deltaX = deltaValue;
			deltaY = 0;
			break;
		case JDinkDirectionIndexConstants.DOWN_LEFT:
			deltaX = -deltaDiagonal;
			deltaY = deltaDiagonal;
			break;
		case JDinkDirectionIndexConstants.DOWN:
			deltaX = 0;
			deltaY = deltaValue;
			break;
		case JDinkDirectionIndexConstants.DOWN_RIGHT:
			deltaX = deltaDiagonal;
			deltaY = deltaDiagonal;
			break;
		}
//		if (up) {
//			deltaY -= deltaValue;
//		}
//		if (down) {
//			deltaY += deltaValue;
//		}
//		if (left) {
//			deltaX -= deltaValue;
//		}
//		if (right) {
//			deltaX += deltaValue;
//		}
//		if ((deltaX != 0) && (deltaY != null))

		JDinkSprite collisionSprite = null;

		if ((deltaX != 0) || (deltaY != 0)) {
			// we got a direction, check whether nothing is blocking us
			JDinkCollisionTester collisionTester = context.getCollisionTester();
			int x = sprite.getX();
			int y = sprite.getY();
			int steps = Math.max(3, Math.max(deltaX, deltaY));
			for (int i = 2; i <= steps; i++) {
				int tempX = sprite.getX() + (deltaX * i / steps);
				int tempY = sprite.getY() + (deltaY * i / steps);
				JDinkCollision collision = (collisionTester.getCollisionAt(
						JDinkCollisionTestType.WALK,
						tempX, tempY, sprite));
				if (collision == null) {
					x = tempX;
					y = tempY;
				} else {
					if (collision instanceof JDinkSpriteCollision) {
						collisionSprite = ((JDinkSpriteCollision) collision).getSprite();
					}
					break;
				}
			}
			sprite.setX(x);
			sprite.setY(y);
		}

		int sequenceNumber;
		if ((deltaX != 0) || (deltaY != 0)) {
			// walking
			sequenceNumber = sprite.getBaseWalk() + sprite.getDirectionIndex();
		} else if (sprite.getBaseIdle() > 0) {
			// idle
			sequenceNumber = sprite.getBaseIdle() + sprite.getDirectionIndex();
		} else {
			// no base idle set
//			sequenceNumber = sprite.getBaseWalk() + sprite.getDirectionIndex();
			sequenceNumber = 0;
		}
		if ((sequenceNumber > 0) && (sequenceNumber != sprite.getAnimationSequenceNumber())) {
			if ((sprite.getAnimationSequenceNumber() == 0) ||
					(in_this_base(sprite.getAnimationSequenceNumber(), sprite.getBaseWalk())) ||
					(in_this_base(sprite.getAnimationSequenceNumber(), sprite.getBaseIdle()))) {
				setAnimationSequence(context, sprite, sequenceNumber);
			}
			context.getController().setChanged(true);
		}
		if (directionIndex > 0) {
			JDinkMapTransition transition = context.getController().getMapTransition(context, sprite);
			if (transition != null) {
				context.getController().setMapTransition(context, sprite, transition);
			}
			context.getController().setChanged(true);
		}

		if (!sprite.isNoControl()) {
			this.processKeyEvents(context, sprite);
			if (!context.getInteractionManager().interact(
					JDinkInteractionType.TOUCH, context, sprite)) {
			}

			if (collisionSprite != null) {
				JDinkMapSpritePlacement spritePlacement = collisionSprite.getSpritePlacement();
				if ((spritePlacement != null) && (spritePlacement.getProp() == 1)) {
					// warp
					controller.warpTo(context, sprite, spritePlacement.getWarpMap(),
							spritePlacement.getWarpX(), spritePlacement.getWarpY());
				}
			}
		}
	}

	public void onAnimationSequenceEnd(JDinkContext context, JDinkSprite sprite) {
		JDinkPlayer player = context.getPlayer(sprite);
		if ((player != null) && (in_this_base(
				sprite.getOriginalAnimationSequenceNumber(), player.getBasePush()))) {

		}
	}

	private void processKeyEvents(JDinkContext context, JDinkSprite sprite) {
		JDinkController controller = context.getController();
		boolean spacePressed = false;
		EventObject[] events = controller.getEvents();
		for (EventObject event: events) {
			if (event instanceof JDinkKeyEvent) {
				JDinkKeyEvent keyEvent = (JDinkKeyEvent) event;
//				log.info("[processKeyEvents] keyEvent: " + event);
				if (keyEvent.isKeyPressed()) {
					if ((!spacePressed) && (keyEvent.getKeyChar() == ' ')) {
						spacePressed = true;
//						log.info("[processKeyEvents] space pressed");
						if (!JDinkTextBrain.stopAll(context)) {
							if (!context.getInteractionManager().interact(
									JDinkInteractionType.TALK, context, sprite)) {
								// TODO no talk
								log.info("[processKeyEvents] nothing to talk to");
							}
						} else {
							log.info("[processKeyEvents] stopAll failed");
						}
					} else if (keyEvent.getKeyCode() == JDinkKeyConstants.VK_CONTROL) {
//						log.info("[processKeyEvents] control pressed");
//						int sequenceNumber = sprite.getBaseHit() + sprite.getDirectionIndex();
//						if (sequenceNumber != sprite.getAnimationSequenceNumber()) {
//							setAnimationSequence(sprite, context, sequenceNumber);
//						}
						processUseWeapon(context, sprite);
//						if (!context.getInteractionManager().interact(
//								JDinkInteractionType.HIT, context, sprite)) {
//						}
					} else if (keyEvent.getKeyCode() == JDinkKeyConstants.VK_ENTER) {
						showItemMenu(context);
					}
				}
			}
		}
	}

	private void processUseWeapon(JDinkContext context, JDinkSprite sprite) {
		int itemNumber = context.getGlobalVariables().currentWeapon.getInt(context);
		JDinkPlayer player = context.getCurrentPlayer();
		if (player == null) {
			throw new RuntimeException("no current player");
		}
		JDinkItem item = player.getItem(itemNumber);

		if (item != null) {
			try {
				log.info("[processUseWeapon] calling use");
				item.use(context);
			} catch (Throwable e) {
				log.error("use failed due to " + e, e);
			}
		}
	}

	private void showItemMenu(JDinkContext context) {
		JDinkItemMenuBrain.getInstance().showItemMenu(context);
	}

	@Override
	protected boolean processDamage(JDinkContext context, JDinkSprite sprite) {
		boolean result = false;
		int damage = sprite.getDamage();
		if (damage > 0) {
			int hitPoints = sprite.getHitPoints();
			if (hitPoints > 0) {
				showDamage(context, sprite);
				hitPoints = Math.max(0, sprite.getHitPoints() - damage);
				sprite.setHitPoints(hitPoints);
				if (hitPoints == 0) {
					callDieScript(context, sprite);
					// TODO do something if the brain was changed to a people brain (16)?
				}
			}
			sprite.setDamage(0);
		}
		return result;
		/*
		 *
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

                if (spr[h].brain == 16)
                {
                    if (spr[h].dir == 0) spr[h].dir = 3;
                    spr[h].brain = 0;
                    change_dir_to_diag(&spr[h].dir);
                    add_kill_sprite(h);
                    spr[h].active = false;
                }
                return;

            }
        }
        spr[h].damage = 0;

    }

		 */
	}

}
