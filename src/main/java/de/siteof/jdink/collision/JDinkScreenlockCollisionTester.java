package de.siteof.jdink.collision;

import java.util.List;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkSprite;

public class JDinkScreenlockCollisionTester extends AbstractJDinkCollisionTester {
	
	private final JDinkCollisionTester collisionTester;
	
	public JDinkScreenlockCollisionTester(JDinkCollisionTester collisionTester) {
		this.collisionTester = collisionTester;
	}

	@Override
	public JDinkCollision getCollisionAt(JDinkCollisionTestType testType, int x, int y, JDinkSprite excludeSprite) {
		// TODO not sure why we need these condition
        if ( x < 0 && x > -5 ) {
        	x = 0;
        } else if (x > 599 && x < 605) {
        	x = 599;
        }

        if (y < 0 && y > -5) {
        	y = 0;
        } else if (y > 399 && x < 405) {
        	y = 399;
        }
		return collisionTester.getCollisionAt(testType, x, y, excludeSprite);
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, int x, int y,
			JDinkSprite excludeSprite) {
		// TODO not sure why we need these condition
        if ( x < 0 && x > -5 ) {
        	x = 0;
        } else if (x > 599 && x < 605) {
        	x = 599;
        }

        if (y < 0 && y > -5) {
        	y = 0;
        } else if (y > 399 && x < 405) {
        	y = 399;
        }
		return collisionTester.getCollisionsAt(testType, x, y, excludeSprite);
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(
			JDinkCollisionTestType testType, JDinkShape shape) {
		return collisionTester.getCollisionsAt(testType, shape);
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(
			JDinkCollisionTestType testType, JDinkShape shape,
			JDinkSprite excludeSprite) {
		return collisionTester.getCollisionsAt(testType, shape, excludeSprite);
	}

}
