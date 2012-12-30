package de.siteof.jdink.collision;

import java.util.List;


public abstract class AbstractJDinkCollisionTester implements JDinkCollisionTester {

	@Override
	public JDinkCollision getCollisionAt(JDinkCollisionTestType testType, int x, int y) {
		return getCollisionAt(testType, x, y, null);
	}

	@Override
	public List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, int x, int y) {
		return getCollisionsAt(testType, x, y, null);
	}

}
