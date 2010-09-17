package de.siteof.jdink.brain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.collision.JDinkCollision;
import de.siteof.jdink.collision.JDinkCollisionTestType;
import de.siteof.jdink.collision.JDinkCollisionTester;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.functions.script.sprite.JDinkMoveFunction;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkDirectionIndexConstants;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFunction;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.util.JDinkDirectionUtil;
import de.siteof.jdink.util.ObjectUtil;

/**
 * <p>Abstract class for {@link JDinkBrain} implementations.</p>
 */
public abstract class AbstractJDinkBrain implements JDinkBrain {

	private static class DistanceAndDirection {
		private final int distance;
		private final int directionIndex;

		public DistanceAndDirection(int distance, int directionIndex) {
			this.distance = distance;
			this.directionIndex = directionIndex;
		}

		public int getDistance() {
			return distance;
		}

		public int getDirectionIndex() {
			return directionIndex;
		}
	}

	private static final Log log = LogFactory.getLog(JDinkMoveFunction.class);

	protected Boolean toBoolean(Object o, Boolean defaultValue) {
		return ObjectUtil.toBoolean(o, defaultValue);
	}

	protected JDinkScope requestSpriteScope(JDinkContext context, JDinkSprite sprite) {
		return sprite.requestScope(context);
	}

	protected long getTime(JDinkContext context) {
		return context.getTime();
	}

	protected int getBaseTiming(JDinkContext context) {
		return context.getController().getBaseTiming();
	}

	protected int randomInt(JDinkContext context, int count) {
		return context.getRandom().nextInt(count);
	}

	protected boolean processBehaviours(JDinkContext context, JDinkSprite sprite) {
		boolean result;
		JDinkFunction behaviourFunction = sprite.getCurrentBehaviour();
		if (behaviourFunction != null) {
			try {
				JDinkExecutionContext childExecutionContext = new JDinkExecutionContext();
				childExecutionContext.setContext(context);
				childExecutionContext.setArguments(new Object[] { sprite });
				Boolean behaviourResult = (Boolean) behaviourFunction.invoke(childExecutionContext);
				if (!behaviourResult.booleanValue()) {
					sprite.removeBehaviour(behaviourFunction);
				}
			} catch (Throwable e) {
				log.error("[processBehaviours] failed to execute behaviour", e);
				behaviourFunction = null;
			}
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	private boolean isValidSequence(JDinkContext context, int sequenceNumber) {
		return context.getSequence(sequenceNumber, false) != null;
	}

	protected boolean in_this_base(int seq, int base) {
		return ((seq - (seq % 10)) == base);
	}

	public JDinkRectangle getPlayerMapBounds(JDinkContext context) {
		return context.getController().getPlayerMapBounds();
	}

	protected void changeDirection(JDinkContext context, JDinkSprite sprite,
			int dir1) {
		this.changeDirection(context, sprite, dir1, sprite.getBaseWalk());
	}

	protected void changeDirection(JDinkContext context, JDinkSprite sprite,
			int dir1, int base) {
	    int hspeed;
	    int speed = sprite.getSpeed();
	    if ((sprite != null) && (sprite.getBrainNumber() != 9) && (sprite.getBrainNumber() != 10)) {
	        //if (mbase_timing > 20) mbase_timing = 20;

	        //   Msg(",base_timing is %d", base_timing);
	        hspeed = speed * (getBaseTiming(context) / 4);
	        if (hspeed > 49) {
	            log.info("Speed was " + hspeed);
	            speed = 49;
	        } else {
	            speed = hspeed;
	        }
	    }
	    int old_seq = sprite.getAnimationSequenceNumber();
	    int seq = old_seq;
	    int mx = sprite.getMx();
	    int my = sprite.getMy();
	    sprite.setDirectionIndex(dir1);

	    if (dir1 == 1) {
	    	mx = (0 - speed ) + (speed / 3);
	        my = speed - (speed / 3);

	        if (base != -1) {
	        	seq = base + 1;
	        	if (!isValidSequence(context, seq)) {
	        		seq = base + 9;
	        	}
	        }
	    }

	    if (dir1 == 2) {
	        mx = 0;
	        my = speed;
	        if (base != -1) {
	            seq = base + 2;
	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 3))) {
	        		seq = base + 3;
	        	}
	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 1))) {
	        		seq = base + 1;
	        	}
	        }
	    }

	    if (dir1 == 3)
	    {
	        mx = speed - (speed / 3);
	        my = speed - (speed / 3);
	        if (base != -1) {
	            seq = base + 3;
	        	if (!isValidSequence(context, seq)) {
	        		seq = base + 7;
	        	}
	        }
	    }

	    if (dir1 == 4)
	    {
	        mx = (0 - speed);
	        my = 0;
	        if (base != -1) {
	            seq = base + 4;
	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 7))) {
	        		seq = base + 7;
	        	}
	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 1))) {
	        		seq = base + 1;
	        	}
	        }
	    }

	    if (dir1 == 6)
	    {
	        mx = speed;
	        my = 0;
	        if (base != -1) {
	            seq = base + 6;

	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 3))) {
	        		seq = base + 3;
	        	}

	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 9))) {
	        		seq = base + 9;
	        	}
	        }
	    }

	    if (dir1 == 7)
	    {
	        mx = (0 - speed) + (speed / 3);
	        my = (0 - speed)+ (speed / 3);
	        if (base != -1)
	        {
	            seq = base + 7;

	        	if (!isValidSequence(context, seq)) {
	        		seq = base + 3;
	        	}
	        }
	    }
	    if (dir1 == 8)
	    {
	        mx = 0;
	        my = (0 - speed);
	        if (base != -1) {
	            seq = base + 8;

	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 7))) {
	        		seq = base + 7;
	        	}

	        	if ((!isValidSequence(context, seq)) &&
	        			(isValidSequence(context, base + 9))) {
	        		seq = base + 9;
	        	}
	        }
	    }


	    if (dir1 == 9)
	    {
	        mx = speed- (speed / 3);
	        my = (0 - speed)+ (speed / 3);
	        if (base != -1)
	        {
	            seq = base + 9;
	        	if (!isValidSequence(context, seq)) {
	        		seq = base + 1;
	        	}
	        }
	    }

    	if (!isValidSequence(context, seq)) {
    		seq = old_seq;
    	}

    	sprite.setMx(mx);
    	sprite.setMy(my);

	    if (old_seq != seq) {
//	    	sprite.setAnimationSequenceNumber(seq);
////			sprite.setSequence(context.getSequence(seq, true));
//	    	sprite.setAnimationFrameNumber(0);
////			sprite.resetFrameNumber();
//			sprite.setFrameDelay(0);
//			sprite.setNextAnimationTime(0);
			setAnimationSequence(context, sprite, seq);
	    }
	}

	protected void setAnimationSequence(JDinkContext context, JDinkSprite sprite, int sequenceNumber) {
    	sprite.setAnimationSequenceNumber(sequenceNumber);
    	sprite.setAnimationFrameNumber(0);
		sprite.setFrameDelay(0);
		sprite.setNextAnimationTime(0);
//		sprite.setAutoReverse(true);
	}

	protected JDinkCollision automove(JDinkContext context, JDinkSprite sprite) {
		return automove(context, sprite, true);
	}

	protected JDinkCollision automove(JDinkContext context, JDinkSprite sprite, boolean checkCollision) {
	    char kindx,kindy;
	    int speedx = 0;
	    int speedy = 0;

	    int mx = sprite.getMx();
	    int my = sprite.getMy();

	    if (mx != 0) {
	        if (mx < 0) {
	            kindx = '-';
	        } else {
	        	kindx = '+';
	        }
	        if (kindx == '-') {
	        	speedx = (mx - (mx * 2));
	        } else {
	        	speedx = mx;
	        }
	    } else {
	    	kindx = '0';
	    }

	    if (my != 0) {
	        if (my < 0) {
	        	kindy = '-';
	        } else {
	        	kindy = '+';
	        }
	        if (kindy == '-') {
	        	speedy = (my - (my * 2));
	        } else {
	        	speedy = my;
	        }
	    } else {
	    	kindy = '0';
	    }

	    int speed = speedx;
	    if (speedy > speedx) {
	    	speed = speedy;
	    }

	    JDinkCollision collision = null;
	    if (speed > 0) {
	    	collision = move(context, sprite, speed, kindx, kindy, checkCollision);
	    }
	    return collision;
	}

	protected JDinkCollision move(JDinkContext context, JDinkSprite sprite, int amount, char kind, char kindy,
			boolean checkCollision) {
	    int mx = 0;
	    int my = 0;
	    boolean clearx;
	    boolean cleary;
	    clearx = false;
	    cleary = false;

	    int x = sprite.getX();
	    int y = sprite.getY();

		JDinkCollisionTester collisionTester = context.getCollisionTester();
		JDinkCollision collision = null;

	    for (int i=1; i <= amount; i++) {
//	        spr[u].moveman++;
		    int tempX = x;
		    int tempY = y;

	        if (mx >= sprite.getMx()) {
	        	clearx = true;
	        }
	        if (my >= sprite.getMy()) {
	        	clearx = true;
	        }

	        if ((clearx) && (cleary)) {
	            mx = 0;
	            my = 0;
	            clearx = false;
	            cleary = false;
	        }


	        if (kind == '+') {
	            if (mx < sprite.getMx()) {
	            	tempX++;
	            }
	            mx++;

	        }
	        if (kind == '-') {
	        	// TODO this is "-sprite.getMx()"
	            if (mx < (sprite.getMx() - (sprite.getMx() * 2))) {
	            	tempX--;
	            }
	            mx++;
	        }

	        if (kindy == '+')
	        {
	            if (my < sprite.getMy()) {
	            	tempY++;
	            }
	            my++;
	        }
	        if (kindy == '-')
	        {
	        	// TODO this is "-sprite.getMy()"
	            if (my < (sprite.getMy() - (sprite.getMy() * 2))) {
	            	tempY--;
	            }
	            my++;
	        }

	        if (checkCollision) {
				collision = (collisionTester.getCollisionAt(
						JDinkCollisionTestType.WALK,
						tempX, tempY, sprite));
	        }
			if (collision == null) {
				x = tempX;
				y = tempY;
			} else {
				break;
			}

//	        spr[u].lpx[spr[u].moveman] = spr[u].x;
//	        spr[u].lpy[spr[u].moveman] = spr[u].y;
	    }

	    sprite.setX(x);
	    sprite.setY(y);

	    return collision;
	}

	private int autoreverse_diag(JDinkContext context, int directionIndex) {
//	    if (spr[j].dir == 0) spr[j].dir = 7;
	    int r = randomInt(context, 2);
	    int result;
	    switch (directionIndex) {
	    case 1:
	    case 3:
	    	result = (r == 0 ? 9 : 7);
	    	break;
	    case 6:
	    	result = (r == 0 ? 7 : 1);
	    	break;
	    case 9:
	    case 8:
	    	result = (r == 0 ? 1 : 7);
	    	break;
	    case 0:
	    case 4:
	    case 7:
	    	result = (r == 0 ? 3 : 9);
	    	break;
	    default:
	    	// invalid direction index
	    	result = 0;
	    }
	    return result;
	}

	protected int autoreverse_diag(JDinkContext context, JDinkSprite sprite) {
		int result = autoreverse_diag(context, sprite.getDirectionIndex());
		if (result == 0) {
			log.info("Auto Reverse Diag was sent a dir " + sprite.getDirectionIndex() +
					" sprite, base " + sprite.getBaseWalk() + " walk.");
		}
	    return result;
	}

	protected JDinkRectangle getDefaultMovementBounds(JDinkContext context, JDinkSprite sprite) {
		JDinkRectangle playerMapBounds = this.getPlayerMapBounds(context);
		JDinkRectangle movementBounds = null;
		if (playerMapBounds != null) {
			int x = 15;
			int y = 15;
			movementBounds = new JDinkRectangle(
					15, 15, playerMapBounds.getX() + playerMapBounds.getWidth() - 15 - x,
					playerMapBounds.getY() + playerMapBounds.getHeight() - 15 - y);
		}
		return movementBounds;
	}

	protected void autoChangeDirectionOnBounds(
			JDinkContext context, JDinkSprite sprite, JDinkRectangle movementBounds) {
		if (movementBounds != null) {
		    if (sprite.getY() >= (movementBounds.getY() + movementBounds.getHeight())) {
		    	if (this.randomInt(context, 2) == 0) {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.UP_RIGHT);
		    	} else {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.UP_LEFT);
		    	}
		    }

		    if (sprite.getX() >= (movementBounds.getX() + movementBounds.getWidth())) {
		    	if (this.randomInt(context, 2) == 0) {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.UP_LEFT);
		    	} else {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.DOWN_LEFT);
		    	}
		    }

		    if (sprite.getY() < movementBounds.getY()) {
		    	if (this.randomInt(context, 2) == 0) {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.DOWN_RIGHT);
		    	} else {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.DOWN_LEFT);
		    	}
		    }

		    if (sprite.getX() < movementBounds.getX()) {
		    	if (this.randomInt(context, 2) == 0) {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.UP_RIGHT);
		    	} else {
		    		changeDirection(context, sprite, JDinkDirectionIndexConstants.DOWN_RIGHT);
		    	}
		    }
		}
	}

	protected DistanceAndDirection getDistanceAndDirection(
			JDinkSprite sprite, JDinkSprite targetSprite) {
		return getDistanceAndDirection(sprite, targetSprite, false);
	}


	protected DistanceAndDirection getDistanceAndDirection(
			JDinkSprite sprite, JDinkSprite targetSprite, boolean smoothFollow) {
		// TODO handle smoothFollow (using multiplies of 4?)
		int directionIndex;
//		int deltaX = sprite.getX() - targetSprite.getX();
//		int deltaY = sprite.getY() - targetSprite.getY();
		int deltaX = targetSprite.getX() - sprite.getX();
		int deltaY = targetSprite.getY() - sprite.getY();
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);
		boolean diagonal = ((deltaX != 0) && (deltaY != 0));
		if ((deltaX == 0) && (deltaY == 0)) {
			directionIndex = 0;
		} else if (diagonal) {
			if (deltaY > 0) {
				if (deltaX > 0) {
					directionIndex = JDinkDirectionIndexConstants.DOWN_RIGHT;
				} else {
					directionIndex = JDinkDirectionIndexConstants.DOWN_LEFT;
				}
			} else {
				if (deltaX > 0) {
					directionIndex = JDinkDirectionIndexConstants.UP_RIGHT;
				} else {
					directionIndex = JDinkDirectionIndexConstants.UP_LEFT;
				}
			}
		} else if (absDeltaX > absDeltaY) {
			if (deltaX > 0) {
				directionIndex = JDinkDirectionIndexConstants.RIGHT;
			} else {
				directionIndex = JDinkDirectionIndexConstants.LEFT;
			}
		} else {
			if (deltaY > 0) {
				directionIndex = JDinkDirectionIndexConstants.DOWN;
			} else {
				directionIndex = JDinkDirectionIndexConstants.UP;
			}
		}
		// TODO should this perhaps calculate the real distance?
		int distance = Math.max(absDeltaX, absDeltaY);
		return new DistanceAndDirection(distance, directionIndex);
	}

	protected void processTarget(
			JDinkContext context,
			JDinkSprite sprite, JDinkSprite targetSprite, JDinkRectangle movementBounds) {
		DistanceAndDirection distanceResult = this.getDistanceAndDirection(
				sprite, targetSprite);
		if (distanceResult.getDistance() >= sprite.getDistance()) {
			changeDirection(context, sprite, distanceResult.getDirectionIndex());
			automove(context, sprite);
		}
	}

	protected boolean processTargetMovement(
			JDinkContext context, JDinkSprite sprite) {
		return processTargetMovement(context, sprite, getDefaultMovementBounds(context, sprite));
	}

	protected boolean processTargetMovement(
			JDinkContext context, JDinkSprite sprite, JDinkRectangle movementBounds) {
		boolean result = false;
//		if (!result) {
//			return result;
//		}
		int targetSpriteNumber = sprite.getTargetSpriteNumber();
		JDinkSprite targetSprite = null;
		if (targetSpriteNumber > 0) {
			targetSprite = context.getSprite(targetSpriteNumber, false);
			if (targetSprite == null) {
				log.info("[processTargetMovement] target no longer reachable, targetSpriteNumber=" +
						targetSpriteNumber);
				sprite.setTargetSpriteNumber(0);
			} else if (!targetSprite.isActive()) {
				log.info("[processTargetMovement] target not active, targetSpriteNumber=" +
						targetSpriteNumber);
				sprite.setTargetSpriteNumber(0);
				targetSprite = null;
			}
		}
		if (targetSprite != null) {
			if (in_this_base(sprite.getAnimationSequenceNumber(),
					sprite.getBaseAttack())) {
				// still attacking
			} else {
				int spriteDistance = sprite.getDistance();
				if (spriteDistance == 0) {
					spriteDistance = 5;
					sprite.setDistance(spriteDistance);
				}
				long time = getTime(context);
				DistanceAndDirection distanceResult = this.getDistanceAndDirection(
						sprite, targetSprite);
				if ((distanceResult.getDistance() < spriteDistance) &&
						(sprite.getAttackWaitTime() < getTime(context))) {
					if (sprite.getBaseAttack() > 0) {
						DistanceAndDirection distanceResult2 = this.getDistanceAndDirection(
								sprite, targetSprite, false);

						sprite.setDirectionIndex(distanceResult2.getDirectionIndex());
						sprite.setAnimationSequenceNumber(sprite.getBaseAttack() + sprite.getDirectionIndex());
						sprite.setAnimationFrameNumber(0);

						JDinkScriptInstance scriptInstance = sprite.getScriptInstance();
						if (scriptInstance != null) {
							JDinkScriptFunction function = scriptInstance.getFunctionByName("attack");
							if (function != null) {
								try {
									scriptInstance.callFunction(context, function);
								} catch (Throwable e) {
									log.warn("[processTargetMovement] failed to execute attach script due to " + e, e);
								}
							} else {
								sprite.setMoveWaitTime(time + (randomInt(context, 300) + 10));
							}
						}
					    context.getController().notifyChanged(sprite, JDinkController.ALL_CHANGE);
					    result = true;
					}
				}

				if (!result) {
					// not attacked
					if (sprite.getMoveWaitTime() < time) {
						this.processTarget(context, sprite, targetSprite, movementBounds);
						result = true;
					}
				}
			}
		}

/*
 *
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

 */
		return result;
	}

	protected void processRandomMovement(
			JDinkContext context, JDinkSprite sprite) {
		processRandomMovement(context, sprite, getDefaultMovementBounds(context, sprite));
	}

	protected void processRandomMovement(
			JDinkContext context, JDinkSprite sprite, JDinkRectangle movementBounds) {
		long time = getTime(context);
		if ((sprite.getAnimationSequenceNumber() == 0) &&
				((sprite.getBaseWalk() != -1) && (sprite.getMoveWaitTime() < time))) {
			boolean changeDirection = (randomInt(context, 12) == 0);
            if ((sprite.getAnimationSequenceNumber() == 0) ||
            		(in_this_base(sprite.getAnimationSequenceNumber(), sprite.getBaseAttack()))) {
            	changeDirection = true;
            }
			if (changeDirection) {
				int randomDirectionIndex = randomInt(context, 9) + 1;
	            if ((randomDirectionIndex == JDinkDirectionIndexConstants.UP_LEFT) ||
	            		(randomDirectionIndex == JDinkDirectionIndexConstants.UP_RIGHT) ||
	            		(randomDirectionIndex == JDinkDirectionIndexConstants.DOWN_LEFT) ||
	            		(randomDirectionIndex == JDinkDirectionIndexConstants.DOWN_RIGHT)) {
	            	changeDirection(context, sprite, randomDirectionIndex);
	                sprite.setMoveWaitTime(time + (randomInt(context, 2000) + 200));
	            }
			} else {
	            //keep going the same way
			}
		}

		autoChangeDirectionOnBounds(context, sprite, movementBounds);

	    if (automove(context, sprite) != null) {
	    	// walked into something
	        sprite.setMoveWaitTime(time + 400);
	        changeDirection(context, sprite, autoreverse_diag(context, sprite));
	    }
	    context.getController().notifyChanged(sprite, JDinkController.ALL_CHANGE);
	}

	protected void processMovement(JDinkContext context, JDinkSprite sprite) {
		if (!this.processTargetMovement(context, sprite)) {
			processRandomMovement(context, sprite);
		}
	}

	protected void showStatUpdateText(JDinkContext context, JDinkSprite sprite,
			String text, int offsetX, int offsetY) {
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		int x = sprite.getX();
		int y = sprite.getY();
		JDinkShape spriteBounds = sprite.getBounds();
		if (spriteBounds != null) {
			JDinkRectangle r = spriteBounds.getBounds();
			x = x + r.getX() + r.getWidth() / 5 + offsetX;
			y = y + r.getY() - r.getHeight() / 3 + offsetY;
		}
		JDinkSprite statSprite = spriteHelper.newSprite(x, y, 0, 0, false);
		spriteHelper.setSpriteBrain(statSprite, 8); // text brain
		statSprite.setMy(-1);
		statSprite.setDirectionIndex(JDinkDirectionIndexConstants.UP);
		statSprite.setText(text);
		statSprite.setTextBounds(JDinkRectangle.getInstance(
				0, 0, 50, 50));
		statSprite.setVisible(true);
	}

	protected void showDamage(JDinkContext context, JDinkSprite sprite) {
		// this should show in white
		showStatUpdateText(context, sprite,
				"`%" + sprite.getDamage(), 0, 0);
//		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
//		int x = sprite.getX();
//		int y = sprite.getY();
//		JDinkShape spriteBounds = sprite.getBounds();
//		if (spriteBounds != null) {
//			JDinkRectangle r = spriteBounds.getBounds();
//			x = x + r.getX() + r.getWidth() / 5;
//			y = y + r.getY() - r.getHeight() / 3;
//		}
//		JDinkSprite damageSprite = spriteHelper.newSprite(x, y, 0, 0, false);
//		spriteHelper.setSpriteBrain(damageSprite, 8); // text brain
//		damageSprite.setMy(-1);
//		damageSprite.setDirectionIndex(JDinkDirectionIndexConstants.UP);
//		damageSprite.setText(Integer.toString(sprite.getDamage()));
//		damageSprite.setTextBounds(JDinkRectangle.getInstance(
//				0, 0, 50, 50));
//		damageSprite.setVisible(true);
/*
    int crap2 = add_sprite(spr[h].x,spr[h].y,8,0,0);

    spr[crap2].y -= k[seq[spr[h].pseq].frame[spr[h].pframe]].yoffset;
    spr[crap2].x -= k[seq[spr[h].pseq].frame[spr[h].pframe]].xoffset;
    spr[crap2].y -= k[seq[spr[h].pseq].frame[spr[h].pframe]].box.bottom / 3;
    spr[crap2].x += k[seq[spr[h].pseq].frame[spr[h].pframe]].box.right / 5;

    spr[crap2].speed = 1;
    spr[crap2].hard = 1;
    spr[crap2].brain_parm = h;
    spr[crap2].my = -1;
    spr[crap2].kill = 1000;
    spr[crap2].dir = 8;
    spr[crap2].damage = spr[h].damage;

 */
	}

	protected boolean callDieScript(JDinkContext context, JDinkSprite sprite) {
		boolean result = false;
		JDinkScriptInstance scriptInstance = sprite.getScriptInstance();
		if (scriptInstance != null) {
			JDinkScriptFunction function = scriptInstance.getFunctionByName("die");
			if (function != null) {
				try {
					scriptInstance.callFunction(context, function);
				} catch (Throwable e) {
					log.warn("[callDieScript] failed to execute die script due to " + e, e);
				}
				result = true;
			}
		}
		return result;
	}

	protected int getDiagonalDirectionIndex(int directionIndex) {
		int result;
		switch (directionIndex) {
		case JDinkDirectionIndexConstants.UP:
			result = JDinkDirectionIndexConstants.UP_LEFT;
			break;
		case JDinkDirectionIndexConstants.DOWN:
			result = JDinkDirectionIndexConstants.DOWN_RIGHT;
			break;
		case JDinkDirectionIndexConstants.LEFT:
			result = JDinkDirectionIndexConstants.DOWN_LEFT;
			break;
		case JDinkDirectionIndexConstants.RIGHT:
			result = JDinkDirectionIndexConstants.UP_RIGHT;
			break;
		default:
			result = directionIndex;
		}
		return result;
/*
    if (*dir == 8) *dir = 7;
    if (*dir == 4) *dir = 1;
    if (*dir == 2) *dir = 3;
    if (*dir == 6) *dir = 9;
 */
	}

	protected void add_exp(JDinkContext context, JDinkSprite sprite,
			int experience) {
		add_exp(context, sprite, experience, true);
	}

	protected void add_exp(JDinkContext context, JDinkSprite sprite,
			int experience, boolean checkLastHit) {
		if (experience > 0) {
			JDinkPlayer player = context.getCurrentPlayer();
			if ((checkLastHit) && (sprite.getLastHitSpriteNumber() != player.getSpriteNumber())) {
				log.info("lastHit is not player");
			} else {
				int currentExperience = context.getGlobalVariables().experience.getInt(context);
				int updatedExperience = Math.min(99999, currentExperience + experience);
				if (updatedExperience != currentExperience) {
					context.getGlobalVariables().experience.setInt(context, updatedExperience);
				}

				// this should show in yellow, about 30px above the damage
				showStatUpdateText(context, sprite,
						"`$" + experience, 0, -30);
			}
		}
/*
    //redink1 fix - made work with all sprites when using add_exp DinkC command
    if (addEvenIfNotLastSpriteHit == false)
    {
        if (spr[h].last_hit != 1) return;
    }

    if (num > 0)
    {
        //add experience


        *pexper += num;


        int crap2 = add_sprite(spr[h].x,spr[h].y,8,0,0);

        spr[crap2].y -= k[seq[spr[h].pseq].frame[spr[h].pframe]].yoffset;
        spr[crap2].x -= k[seq[spr[h].pseq].frame[spr[h].pframe]].xoffset;
        spr[crap2].y -= k[seq[spr[h].pseq].frame[spr[h].pframe]].box.bottom / 3;
        spr[crap2].x += k[seq[spr[h].pseq].frame[spr[h].pframe]].box.right / 5;
        spr[crap2].y -= 30;
        spr[crap2].speed = 1;
        spr[crap2].hard = 1;
        spr[crap2].brain_parm = 5000;
        spr[crap2].my = -1;
        spr[crap2].kill = 1000;
        spr[crap2].dir = 8;
        spr[crap2].damage = num;


        if (*pexper > 99999) *pexper = 99999;


    }

 */
	}

	protected void add_kill_sprite(JDinkContext context, JDinkSprite sprite) {
		int directionIndex = sprite.getDirectionIndex();
		if ((directionIndex < 0) || (directionIndex > 9)) {
			directionIndex = 3;
			sprite.setDirectionIndex(directionIndex);
		}

		int base = sprite.getBaseDie();
		if (base <= 0) {
			if (context.getSequence(sprite.getBaseWalk() + 5) != null) {
				base = sprite.getBaseWalk();
				directionIndex = 5;
			} else {
				base = 164;
				directionIndex = 0;
			}
		}

		JDinkSequence sequence = context.getSequence(base + directionIndex);
		if (sequence == null) {
			directionIndex = JDinkDirectionUtil.getInvertedDirection(directionIndex);
			sequence = context.getSequence(base + directionIndex);
			if (sequence == null) {
				log.info("no die sequence found for sprite, spriteNumber=" + sprite.getSpriteNumber());
				directionIndex = JDinkDirectionIndexConstants.DOWN_RIGHT;
			}
		}

		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		JDinkSprite killSprite = spriteHelper.newSprite(
				sprite.getX(), sprite.getY(), base + directionIndex, 1, false);
		if (base == 164) {
			spriteHelper.setSpriteBrain(killSprite, 7); // one time
		} else {
			spriteHelper.setSpriteBrain(killSprite, 5); // one time background
		}
		killSprite.setSpeed(0);
		killSprite.setBaseWalk(0);
		killSprite.setAnimationSequenceNumber(base + directionIndex);
		killSprite.setSize(sprite.getSize());
		killSprite.setVisible(true);

		this.add_exp(context, sprite, sprite.getExperience());
		// TODO add_exp

/*
    int crap2 = add_sprite(spr[h].x,spr[h].y,5,base +dir,1);
    spr[crap2].speed = 0;
    spr[crap2].base_walk = 0;
    spr[crap2].seq = base + dir;

    if (base == 164) spr[crap2].brain = 7;

    spr[crap2].size = spr[h].size;

    add_exp(spr[h].exp, h);

 */

/*
    if ( (spr[h].dir > 9) || (spr[h].dir < 1) )
    {
        Msg("Error:  Changing sprites dir from %d (!?) to 3.", spr[h].dir);
        spr[h].dir = 3;

    }


    int dir = spr[h].dir;
    int base = spr[h].base_die;

    //Msg("Base die is %d", base);
    if (base == -1)
    {

        if (seq[spr[h].base_walk+5].active == true)
        {
            add_exp(spr[h].exp, h);

            int crap2 = add_sprite(spr[h].x,spr[h].y,5,spr[h].base_walk +5,1);
            spr[crap2].speed = 0;
            spr[crap2].seq = spr[h].base_walk + 5;
            //redink1 added this so corpses are the same size
            spr[crap2].size = spr[h].size;
            return;
        } else
        {
            dir = 0;
            base = 164;

        }
    }



    if (seq[base+dir].active == false)
    {

        if (dir == 1) dir = 9;
        else if (dir == 3) dir = 7;
        else if (dir == 7) dir = 3;
        else if (dir == 9) dir = 1;

        else if (dir == 4) dir = 6;
        else if (dir == 6) dir = 4;
        else if (dir == 8) dir = 2;
        else if (dir == 2) dir = 8;


    }
    if (seq[base+dir].active == false)

    {
        Msg("Can't make a death sprite for dir %d!", base+dir);
    }



    int crap2 = add_sprite(spr[h].x,spr[h].y,5,base +dir,1);
    spr[crap2].speed = 0;
    spr[crap2].base_walk = 0;
    spr[crap2].seq = base + dir;

    if (base == 164) spr[crap2].brain = 7;

    spr[crap2].size = spr[h].size;

    add_exp(spr[h].exp, h);

 */
	}

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
