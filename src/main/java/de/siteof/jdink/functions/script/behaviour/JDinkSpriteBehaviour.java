package de.siteof.jdink.functions.script.behaviour;

import java.io.Serializable;

import de.siteof.jdink.model.JDinkSprite;

public interface JDinkSpriteBehaviour extends Serializable {

	public boolean execute(JDinkSprite sprite);

}
