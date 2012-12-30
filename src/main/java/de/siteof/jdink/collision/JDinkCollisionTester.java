package de.siteof.jdink.collision;

import java.util.List;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkSprite;

public interface JDinkCollisionTester {

	JDinkCollision getCollisionAt(JDinkCollisionTestType testType, int x, int y);
	
	JDinkCollision getCollisionAt(JDinkCollisionTestType testType, int x, int y, JDinkSprite excludeSprite);

	List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, int x, int y);

	List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, int x, int y, JDinkSprite excludeSprite);

	List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, JDinkShape shape);

	List<JDinkCollision> getCollisionsAt(JDinkCollisionTestType testType, JDinkShape shape, JDinkSprite excludeSprite);

}
