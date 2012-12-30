package de.siteof.jdink.functions.script;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.model.JDinkSpriteLayer;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.status.JDinkStatusManager;

/**
 * <p>Function: void draw_status()</p>
 */
public class JDinkDrawStatusFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkDrawStatusFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("draw status");
//		log.warn("draw_status not implemented");
		JDinkContext context = executionContext.getContext();
		JDinkController controller = context.getController();
		
		JDinkStatusManager statusManager = JDinkStatusManager.getInstance(context);
		statusManager.drawStatus(context);
		
//		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
//		
//		JDinkSpriteLayer previousSpriteLayer = controller.getCurrentSpriteLayer();
//		JDinkSpriteLayer spriteLayer = controller.getSpriteLayer(0);
//		try {
//			controller.setCurrentSpriteLayer(spriteLayer);
//			
//			List<JDinkSprite> sprites = new LinkedList<JDinkSprite>();
//			sprites.add(spriteHelper.newSpriteAbsolute(0, 0, 180, 1, false));
//			sprites.add(spriteHelper.newSpriteAbsolute(620, 0, 180, 2, false));
//			sprites.add(spriteHelper.newSpriteAbsolute(0, 400, 180, 3, false));
//			
//			JDinkScope globalScope = context.getGlobalScope();
//			Integer strength = toInteger(globalScope.getVariableValue("&strength"), Integer.valueOf(0));
//			log.info("strength=" + strength);
//			
//			for (JDinkSprite sprite: sprites) {
//				sprite.setVisible(true);
//			}
//		} finally {
//			controller.setCurrentSpriteLayer(previousSpriteLayer);
//		}
		controller.setChanged(true);
		return null;
	}

}
