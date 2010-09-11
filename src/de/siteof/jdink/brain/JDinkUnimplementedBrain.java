package de.siteof.jdink.brain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;

public class JDinkUnimplementedBrain extends AbstractJDinkBrain {

	private static final Log log = LogFactory.getLog(JDinkUnimplementedBrain.class);

	private final int brainNumber;
	private final String name;

	public JDinkUnimplementedBrain(int brainNumber, String name) {
		this.brainNumber = brainNumber;
		this.name = name;
	}

	@Override
	public void update(JDinkContext context, JDinkSprite sprite) {
		log.info("[update] unimplemented brain: brainNumber=[" + brainNumber +
				"], name=[" + name + "], spriteNumber=[" + sprite.getSpriteNumber() + "]");
	}

}
