package de.siteof.jdink.functions.script.sprite;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int sp_editor_num(int spriteNumber)</p>
 */
public class JDinkSpEditorNumFunction extends
		AbstractJDinkNoParameterSpriteFunction {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected Object invoke(JDinkExecutionContext executionContext,
			JDinkSprite sprite) {
		return Integer.valueOf(sprite.getEditorSpriteNumber());
	}

}
