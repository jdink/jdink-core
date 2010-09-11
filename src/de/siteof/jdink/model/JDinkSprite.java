package de.siteof.jdink.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.siteof.jdink.brain.JDinkBrain;
import de.siteof.jdink.collision.JDinkCollisionTestType;
import de.siteof.jdink.format.map.JDinkMapSpritePlacement;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptConstants;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.script.JDinkVariable;

/**
 * <p>A sprite is a movable object.</p>
 */
public class JDinkSprite {

	@Deprecated
	public static final int UP_LEFT = JDinkDirectionIndexConstants.UP_LEFT;
	@Deprecated
	public static final int UP = JDinkDirectionIndexConstants.UP;
	@Deprecated
	public static final int UP_RIGHT = JDinkDirectionIndexConstants.UP_RIGHT;
	@Deprecated
	public static final int LEFT = JDinkDirectionIndexConstants.LEFT;
	@Deprecated
	public static final int RIGHT = JDinkDirectionIndexConstants.RIGHT;
	@Deprecated
	public static final int DOWN_LEFT = JDinkDirectionIndexConstants.DOWN_LEFT;
	@Deprecated
	public static final int DOWN = JDinkDirectionIndexConstants.DOWN;
	@Deprecated
	public static final int DOWN_RIGHT = JDinkDirectionIndexConstants.DOWN_RIGHT;

	/**
	 * The spriteNumber is a unique id of the sprite.
	 */
	private final int spriteNumber;

	private int sequenceNumber;
	private JDinkSequence sequence;

	private int animationSequenceNumber;
	private int originalAnimationSequenceNumber;

	private int x;
	private int y;
	private int brainNumber;
	private int[] brainParameters;

	/**
	 * The size in percentage. i.e. "100" would be normal size.
	 */
	private int size = 100;

	/**
	 * The frameNumber is the index to a sequence (e.g. animation or image for a
	 * particular direction)
	 */
	private int frameNumber;

	/**
	 * The animationFrameNumber is the frame index of the current animation. May be the same as frameNumber;
	 */
	private int animationFrameNumber;

	private int depthHint;
	private int speed;

	private boolean noClip;

	private boolean noHit;

	/**
	 * TODO
	 */
	private boolean noTouch;

	/**
	 * TODO
	 */
	private boolean noHardness;

	private int frameDelay;
	private JDinkScriptInstance scriptInstance;
	private int touchDamage;
	private JDinkBrain brain;
	private JDinkScope scope;
	private String text;
	private int baseWalk;
	private int baseIdle;
	private int baseAttack;
	private int baseHit;
	private int baseDeath;
	private int directionIndex;
	private int timing;
	private int distance;
	private boolean frozen;
	private int experience;
	private int defense;
	private int strength;
	private boolean reverse;
	private boolean autoReverse;
	private boolean positionAbsolute;

	/**
	 * Defines the when an animation should continue (milliseconds).
	 * (This is called "delay" in dink)
	 */
	private long nextAnimationTime;

	private int hitPoints;
	private int targetSpriteNumber;
	private boolean active = true;
	private boolean visible = true;
	private JDinkSprite parentSprite = null;

	/**
	 * not used, behaviour is used instead
	 */
	private final List<JDinkMovement> movementQueue = new ArrayList<JDinkMovement>(2);
	private final List<JDinkFunction> behaviourQueue = new ArrayList<JDinkFunction>(2);
	private long moveWaitTime;
	private long attackWaitTime;
	private int mx;
	private int my;
	private int collisionType;
	private int editorSpriteNumber;
	private JDinkMapSpritePlacement spritePlacement;

	private JDinkShape clipShape;

	/**
	 * If true, the image will be repeated to fill the clip shape (clip shape must not be null).
	 */
	private boolean fill;

	/**
	 * If true, the player won't have any control.
	 */
	private boolean noControl;

	private boolean busy;

	private JDinkPoint offset;

	private int level;

	private final int layerNumber;

	public JDinkSprite(int layer, int spriteNumber) {
		this.layerNumber = layer;
		this.spriteNumber = spriteNumber;
	}

	public JDinkScope requestScope(JDinkContext context) {
		final JDinkSprite sprite = this;
		JDinkScope scope = sprite.getScope();
		if (scope == null) {
			scope = new JDinkScope(context.getGlobalScope());
			sprite.setScope(scope);
			JDinkVariable currentSpriteVariable = scope
					.getVariable(JDinkScriptConstants.CURRENT_SPRITE_VARNAME);
			if (currentSpriteVariable == null) {
				currentSpriteVariable = new JDinkVariable();
				currentSpriteVariable.setType(JDinkIntegerType
						.getInstance());
				scope.addVariable(JDinkScriptConstants.CURRENT_SPRITE_VARNAME,
						currentSpriteVariable);
			}
			currentSpriteVariable
					.setValue(new Integer(sprite.getSpriteNumber()));
		}
		return scope;
	}

	public void addMovement(JDinkMovement movement) {
		synchronized (movementQueue) {
			movementQueue.add(movement);
		}
	}

	public void pollMovements(Collection<JDinkMovement> target) {
		synchronized (movementQueue) {
			if (!movementQueue.isEmpty()) {
				target.addAll(movementQueue);
				movementQueue.clear();
			}
		}
	}

	public void addBehaviour(JDinkFunction behaviour) {
		synchronized (behaviourQueue) {
			behaviourQueue.add(behaviour);
		}
	}

	public JDinkFunction getCurrentBehaviour() {
		JDinkFunction result;
		synchronized (behaviourQueue) {
			if (!behaviourQueue.isEmpty()) {
				result = behaviourQueue.get(0);
			} else {
				result = null;
			}
		}
		return result;
	}

	public void removeBehaviour(JDinkFunction behaviour) {
		synchronized (behaviourQueue) {
			behaviourQueue.remove(behaviour);
		}
	}

	public boolean containsBehaviour(JDinkFunction behaviour) {
		synchronized (behaviourQueue) {
			return behaviourQueue.contains(behaviour);
		}
	}

	public boolean containsBehaviour(Class<?> behaviourClass) {
		boolean result = false;
		synchronized (behaviourQueue) {
			for (JDinkFunction function: behaviourQueue) {
				if (behaviourClass.isInstance(function)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	public int getDepthValue() {
		return (depthHint != 0 ? depthHint : y);
	}

	public boolean isFlying() {
		return false;
	}

	public JDinkSequenceFrame getFrame() {
		JDinkSequence sequence = this.getSequence();
		return (sequence != null ? sequence.getFrame(this.getFrameNumber(), false) : null);
	}

	public JDinkShape getCollisionShape() {
		JDinkShape result = null;
		if (!this.isNoHardness()) {
			JDinkSequence sequence = this.getSequence();
			if (sequence != null) {
				JDinkSequenceFrame frame = sequence.getFrame(this.getFrameNumber(),
						false);
				if (frame != null) {
					result = frame.getCollisionShape();
					if (result == null) {
						result = sequence.getCollisionShape();
					}
				} else {
					result = sequence.getCollisionShape();
				}
			}
			if ((result != null) && (this.isPositionAbsolute())) {
				result = result.getLocatedTo(0, 0);
			}
		}
		return result;
	}

	public JDinkShape getBounds() {
		JDinkSequenceFrame frame = this.getFrame();
		JDinkShape result = (frame != null ? frame.getBounds() : null);
		if ((result != null) && (this.isPositionAbsolute())) {
			result = result.getLocatedTo(0, 0);
		}
		return result;
	}

	public boolean isCollisionAt(JDinkCollisionTestType testType, int x, int y) {
		boolean result = false;
		if ((JDinkCollisionTestType.ALL.equals(testType)) || (this.getCollisionType() == 0)) {
			JDinkShape shape = this.getCollisionShape();
			result = ((shape != null) && (shape.contains(x - this.x, y - this.y)));
		}
		if ((!result) && (JDinkCollisionTestType.ALL.equals(testType))) {
			JDinkShape bounds = this.getBounds();
			result = ((bounds != null) && (bounds.contains(x - this.x, y - this.y)));
		}
		return result;
	}

	public final boolean canInteractUsingBounds(JDinkCollisionTestType testType) {
		boolean result;
		switch (testType) {
		case ALL:
			result = true;
			break;
		case TOUCH:
			result = (this.getTouchDamage() != 0);
			break;
		case HIT:
			result = ((!this.isNoHit()) || (this.getScriptInstance() != null));
			break;
		case INTERACT:
			result = true;
			break;
		default:
			result = false;
		}
		return result;
	}

	public final boolean canInteractUsingCollisionShape(JDinkCollisionTestType testType) {
		boolean result;
		switch (testType) {
		case ALL:
			result = true;
			break;
		case WALK:
			result = (this.getCollisionType() == 0);
			break;
		case TOUCH:
			result = true;
			break;
		default:
			result = false;
		}
		return result;
	}

	public boolean isCollisionAt(JDinkCollisionTestType testType, JDinkShape shape) {
		boolean result = false;
		if (canInteractUsingCollisionShape(testType)) {
			JDinkShape collisionShape = this.getCollisionShape();
			result = ((collisionShape != null) && (collisionShape.intersects(
					shape.getTranslated(-this.x, -this.y))));
		}
		if ((!result) && (canInteractUsingBounds(testType))) {
			JDinkShape bounds = this.getBounds();
			result = ((bounds != null) && (bounds.intersects(
					shape.getTranslated(-this.x, -this.y))));
		}
		return result;
	}

	public void resetFrameNumber() {
		this.setFrameNumber(1);
	}

	public void nextFrame() {
		JDinkSequence sequence = this.getSequence();
		if (sequence != null) {
			int frameNumber = this.getFrameNumber();
			if (frameNumber == sequence.getLastFrameNumber()) {
				frameNumber = sequence.getFirstFrameNumber();
			} else {
				frameNumber = sequence.getNextFrameNumber(frameNumber);
			}
			this.setFrameNumber(frameNumber);
		}
	}

	public void setBrainParameter(int index, int value) {
		int[] brainParameters = this.brainParameters;
		if (brainParameters == null) {
			brainParameters = new int[2];
			this.brainParameters = brainParameters;
		}
		brainParameters[index] = value;
	}

	public int getBrainParameter(int index) {
		int result;
		int[] brainParameters = this.brainParameters;
		if (brainParameters != null) {
			result = brainParameters[index];
		} else {
			result = 0;
		}
		return result;
	}

	// plain getter/setter

	public int getSpriteNumber() {
		return spriteNumber;
	}

	public int getBrainNumber() {
		return brainNumber;
	}

	public void setBrainNumber(int brainNumber) {
		this.brainNumber = brainNumber;
	}

	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public JDinkSequence getSequence() {
		return sequence;
	}

	public void setSequence(JDinkSequence sequence) {
		this.sequence = sequence;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDepthHint() {
		return depthHint;
	}

	public void setDepthHint(int depthHint) {
		this.depthHint = depthHint;
	}

	public boolean isNoClip() {
		return noClip;
	}

	public void setNoClip(boolean noClip) {
		this.noClip = noClip;
	}

	public int getFrameDelay() {
		return frameDelay;
	}

	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
	}

	public int getAnimationSequenceNumber() {
		return animationSequenceNumber;
	}

	public void setAnimationSequenceNumber(int animationSequenceNumber) {
		this.animationSequenceNumber = animationSequenceNumber;
	}

	public int getTouchDamage() {
		return touchDamage;
	}

	public void setTouchDamage(int touchDamage) {
		this.touchDamage = touchDamage;
	}

	public JDinkBrain getBrain() {
		return brain;
	}

	public void setBrain(JDinkBrain brain) {
		this.brain = brain;
	}

	public JDinkScriptInstance getScriptInstance() {
		return scriptInstance;
	}

	public void setScriptInstance(JDinkScriptInstance scriptInstance) {
		this.scriptInstance = scriptInstance;
	}

	public JDinkScope getScope() {
		return scope;
	}

	public void setScope(JDinkScope scope) {
		this.scope = scope;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getBaseWalk() {
		return baseWalk;
	}

	public void setBaseWalk(int baseWalk) {
		this.baseWalk = baseWalk;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public void setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;
	}

	public int getDirectionIndex() {
		return directionIndex;
	}

	public void setDirectionIndex(int directionIndex) {
		this.directionIndex = directionIndex;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean freezed) {
		this.frozen = freezed;
	}

	public int getTiming() {
		return timing;
	}

	public void setTiming(int timing) {
		this.timing = timing;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitpoints) {
		this.hitPoints = hitpoints;
	}

	public int getTargetSpriteNumber() {
		return targetSpriteNumber;
	}

	public void setTargetSpriteNumber(int targetSpriteNumber) {
		this.targetSpriteNumber = targetSpriteNumber;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the parentSprite
	 */
	public JDinkSprite getParentSprite() {
		return parentSprite;
	}

	/**
	 * @param parentSprite
	 *            the parentSprite to set
	 */
	public void setParentSprite(JDinkSprite parentSprite) {
		this.parentSprite = parentSprite;
	}

	public int getCollisionType() {
		return collisionType;
	}

	public void setCollisionType(int collisionType) {
		this.collisionType = collisionType;
	}

	public JDinkMapSpritePlacement getSpritePlacement() {
		return spritePlacement;
	}

	public void setSpritePlacement(JDinkMapSpritePlacement spritePlacement) {
		this.spritePlacement = spritePlacement;
	}

	public int getEditorSpriteNumber() {
		return editorSpriteNumber;
	}

	public void setEditorSpriteNumber(int editorSpriteNumber) {
		this.editorSpriteNumber = editorSpriteNumber;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public long getNextAnimationTime() {
		return nextAnimationTime;
	}

	public void setNextAnimationTime(long nextAnimationTime) {
		this.nextAnimationTime = nextAnimationTime;
	}

	public int getAnimationFrameNumber() {
		return animationFrameNumber;
	}

	public void setAnimationFrameNumber(int animationFrameNumber) {
		this.animationFrameNumber = animationFrameNumber;
	}

	public int getOriginalAnimationSequenceNumber() {
		return originalAnimationSequenceNumber;
	}

	public void setOriginalAnimationSequenceNumber(
			int originalAnimationSequenceNumber) {
		this.originalAnimationSequenceNumber = originalAnimationSequenceNumber;
	}

	public int getLayerNumber() {
		return layerNumber;
	}

	public boolean isNoHit() {
		return noHit;
	}

	public void setNoHit(boolean noHit) {
		this.noHit = noHit;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isPositionAbsolute() {
		return positionAbsolute;
	}

	public void setPositionAbsolute(boolean positionAbsolute) {
		this.positionAbsolute = positionAbsolute;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public JDinkShape getClipShape() {
		return clipShape;
	}

	public void setClipShape(JDinkShape clipShape) {
		this.clipShape = clipShape;
	}

	public long getMoveWaitTime() {
		return moveWaitTime;
	}

	public void setMoveWaitTime(long moveWaitTime) {
		this.moveWaitTime = moveWaitTime;
	}

	public int getMx() {
		return mx;
	}

	public void setMx(int mx) {
		this.mx = mx;
	}

	public int getMy() {
		return my;
	}

	public void setMy(int my) {
		this.my = my;
	}

	public int getBaseIdle() {
		return baseIdle;
	}

	public void setBaseIdle(int baseIdle) {
		this.baseIdle = baseIdle;
	}

	public int getBaseHit() {
		return baseHit;
	}

	public void setBaseHit(int baseHit) {
		this.baseHit = baseHit;
	}

	public boolean isAutoReverse() {
		return autoReverse;
	}

	public void setAutoReverse(boolean autoReverse) {
		this.autoReverse = autoReverse;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public JDinkPoint getOffset() {
		return offset;
	}

	public void setOffset(JDinkPoint offset) {
		this.offset = offset;
	}

	public boolean isNoControl() {
		return noControl;
	}

	public void setNoControl(boolean noControl) {
		this.noControl = noControl;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isNoTouch() {
		return noTouch;
	}

	public void setNoTouch(boolean noTouch) {
		this.noTouch = noTouch;
	}

	public boolean isNoHardness() {
		return noHardness;
	}

	public void setNoHardness(boolean noHardness) {
		this.noHardness = noHardness;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public int getBaseDeath() {
		return baseDeath;
	}

	public void setBaseDeath(int baseDeath) {
		this.baseDeath = baseDeath;
	}

	public long getAttackWaitTime() {
		return attackWaitTime;
	}

	public void setAttackWaitTime(long attackWaitTime) {
		this.attackWaitTime = attackWaitTime;
	}
}
