package de.siteof.jdink.functions.script.sprite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSprite;

/**
 * <p>Function: int create_sprite(int x, int y, int brainNumber, int pseq, int pframe)</p>
 */
public class JDinkCreateSpriteFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkCreateSpriteFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer x = toInteger(executionContext.getArgument(0), null);
		Integer y = toInteger(executionContext.getArgument(1), null);
		Integer brainNumber = toInteger(executionContext.getArgument(2), null);
		Integer pseq = toInteger(executionContext.getArgument(3), null);
		Integer pframe = toInteger(executionContext.getArgument(4), null);
		assertNotNull(x, "create sprite: x missing");
		assertNotNull(y, "create sprite: y missing");
		assertNotNull(brainNumber, "create sprite: brain missing");
		assertNotNull(pseq, "create sprite: pseq missing");
		assertNotNull(pframe, "create sprite: pframe missing");
		if (log.isDebugEnabled()) {
			log.debug("create sprite: x=" + x + " y=" + y + " brain=" + brainNumber + " pseq=" + pseq + " pframe=" + pframe);
		}
		int spriteNumber = executionContext.getContext().getController().allocateSprite();
		JDinkSprite sprite = executionContext.getContext().getController().getSprite(spriteNumber, false);
		if (sprite != null) {
			sprite.setX(x.intValue());
			sprite.setY(y.intValue());
			sprite.setBrainNumber(brainNumber.intValue());
			sprite.setBrain(executionContext.getContext().getBrain(brainNumber.intValue()));
			sprite.setSequenceNumber(pseq.intValue());
			sprite.setFrameNumber(pframe.intValue());
			sprite.setSequence(executionContext.getContext().getSequence(sprite.getSequenceNumber(), false));
			sprite.setNoHardness(true);
			sprite.setVisible(true);
			executionContext.getContext().getController().setChanged(true);
		}
		return Integer.valueOf(spriteNumber);
	}

}
