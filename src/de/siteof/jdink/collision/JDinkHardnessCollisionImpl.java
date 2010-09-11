package de.siteof.jdink.collision;

public class JDinkHardnessCollisionImpl implements JDinkHardnessCollision {

	private final byte hardness;

	public JDinkHardnessCollisionImpl(byte hardness) {
		this.hardness = hardness;
	}

	public byte getHardness() {
		return hardness;
	}

}
