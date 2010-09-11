package de.siteof.jdink.collision;

import java.util.LinkedList;
import java.util.List;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkSprite;

public class JDinkCollisionTesterImpl extends AbstractJDinkCollisionTester {

	private JDinkHardnessMap hardnessMap;
	private List<JDinkSprite> sprites;

	public JDinkCollisionTesterImpl() {
	}

	public JDinkCollisionTesterImpl(JDinkHardnessMap hardnessMap, List<JDinkSprite> sprites) {
		this.hardnessMap = hardnessMap;
		this.sprites = sprites;
	}

//	private boolean isSpriteCollitionAt(JDinkSprite sprite, int x, int y) {
//		return
//	}

	@Override
	public JDinkCollision getCollisionAt(JDinkCollisionTestType testType, int x, int y, JDinkSprite excludeSprite) {
		JDinkCollision result = null;
		List<JDinkSprite> sprites = this.sprites;
		for (JDinkSprite sprite: sprites) {
			if ((sprite != null) && (sprite != excludeSprite) && (sprite.isCollisionAt(testType, x, y))) {
				result = new JDinkSpriteCollisionImpl(sprite);
				break;
			}
		}
		if (result == null) {
			JDinkHardnessMap hardnessMap = this.hardnessMap;
			if (hardnessMap != null) {
				byte hardness = hardnessMap.getHardnessAt(x, y, (byte) 0);
				if (hardness != 0) {
					result = new JDinkHardnessCollisionImpl(hardness);
				}
			}
		}
		return result;
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, int x, int y,
			JDinkSprite excludeSprite) {
		List<JDinkCollision> result = new LinkedList<JDinkCollision>();
		List<JDinkSprite> sprites = this.sprites;
		for (JDinkSprite sprite: sprites) {
			if ((sprite != null) && (sprite != excludeSprite) && (sprite.isCollisionAt(testType, x, y))) {
				result.add(new JDinkSpriteCollisionImpl(sprite));
			}
		}
		JDinkHardnessMap hardnessMap = this.hardnessMap;
		if (hardnessMap != null) {
			byte hardness = hardnessMap.getHardnessAt(x, y, (byte) 0);
			if (hardness != 0) {
				result.add(new JDinkHardnessCollisionImpl(hardness));
			}
		}
		return result;
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(
			JDinkCollisionTestType testType, JDinkShape shape) {
		return getCollisionsAt(testType, shape, null);
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(
			JDinkCollisionTestType testType, JDinkShape shape,
			JDinkSprite excludeSprite) {
		List<JDinkCollision> result = new LinkedList<JDinkCollision>();
		List<JDinkSprite> sprites = this.sprites;
		for (JDinkSprite sprite: sprites) {
			if ((sprite != null) && (sprite != excludeSprite) && (sprite.isCollisionAt(testType, shape))) {
				result.add(new JDinkSpriteCollisionImpl(sprite));
			}
		}
		return result;
	}

}
