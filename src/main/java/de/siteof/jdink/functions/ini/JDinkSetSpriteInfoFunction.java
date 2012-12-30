package de.siteof.jdink.functions.ini;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.functions.ini.util.JDinkSequenceLazyLoader;
import de.siteof.jdink.functions.ini.util.JDinkSpriteInfo;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.loader.JDinkLazyLoader;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;

/**
 * <p>Implementation of {@link JDinkFunction}.<p>
 * <p>Name: SET_SPRITE_INFO</p>
 */
public class JDinkSetSpriteInfoFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSetSpriteInfoFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		// name   seq    speed       offsetx     offsety       hardx      hardy
		Integer sequenceNumber = this.toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = null;
		Integer offsetX = null;
		Integer offsetY = null;
		Integer hardX1 = null;
		Integer hardY1 = null;
		Integer hardX2 = null;
		Integer hardY2 = null;
		try {
			frameNumber = this.toInteger(executionContext.getArgument(1), null);
			offsetX = this.toInteger(executionContext.getArgument(2), null);
			offsetY = this.toInteger(executionContext.getArgument(3), null);
			hardX1 = this.toInteger(executionContext.getArgument(4), null);
			hardY1 = this.toInteger(executionContext.getArgument(5), null);
			hardX2 = this.toInteger(executionContext.getArgument(6), null);
			hardY2 = this.toInteger(executionContext.getArgument(7), null);
//			assertNotNull(frameNumber, "frameNumber missing");
//			assertNotNull(offsetX, "offsetX missing");
//			assertNotNull(offsetY, "offsetY missing");
//			assertNotNull(hardX1, "hardX1 missing");
//			assertNotNull(hardY1, "hardY1 missing");
//			assertNotNull(hardX2, "hardX2 missing");
//			assertNotNull(hardY2, "hardY2 missing");
		} catch (Exception e) {
			log.error("error parsing sequence parameters (" + Arrays.asList(executionContext.getArguments()) + ")" + " - " + e, e);
		}
		JDinkSpriteInfo spriteInfo = new JDinkSpriteInfo(
				offsetX, offsetY, hardX1, hardY1, hardX2, hardY2);
		JDinkSequence sequence = executionContext.getContext().getSequence(sequenceNumber.intValue(), false);
		if (sequence == null) {
			log.warn("cannot set sprite info, sequence not loaded: sequenceNumber=[" +
					sequenceNumber + "]");
		} else {
			JDinkLazyLoader lazyLoader = sequence.getLazyLoader();
			if (lazyLoader instanceof JDinkSequenceLazyLoader) {
				JDinkSequenceLazyLoader sequenceLazyLoader = (JDinkSequenceLazyLoader) lazyLoader;
				if (log.isDebugEnabled()) {
					log.debug("setting sprite info on lazy loader, sequenceNumer=[" +
							sequenceNumber + "], frameNumber=[" + frameNumber + "]");
				}
				sequenceLazyLoader.setSpriteInfo(frameNumber.intValue(), spriteInfo);
			}
			JDinkSequenceFrame frame = sequence.getFrame(frameNumber.intValue(), false);
			if (frame != null) {
				if ((spriteInfo.getOffsetX() != null) && (spriteInfo.getOffsetY() != null) &&
						(spriteInfo.getHardX1() != null) && (spriteInfo.getHardY1() != null) &&
						(spriteInfo.getHardX2() != null) && (spriteInfo.getHardY2() != null)) {
					if (log.isDebugEnabled()) {
						log.debug("setting sprite info on loaded frame, sequenceNumer=[" +
								sequenceNumber + "], frameNumber=[" + frameNumber + "]");
					}
					JDinkRectangle bounds = frame.getBounds();
					int frameBoundsX = -spriteInfo.getOffsetX().intValue();
					int frameBoundsY = -spriteInfo.getOffsetY().intValue();
					if (bounds == null) {
						// create a new bounds rectangle with no size
						bounds = new JDinkRectangle(frameBoundsX, frameBoundsY, 0, 0);
					} else {
						// set the location of the bounds
						bounds = bounds.getLocatedTo(frameBoundsX, frameBoundsY);
					}
					frame.setBounds(bounds);
					frame.setCollisionShape(JDinkRectangle.between(
							spriteInfo.getHardX1().intValue(),
							spriteInfo.getHardY1().intValue(),
							spriteInfo.getHardX2().intValue(),
							spriteInfo.getHardY2().intValue()));
				}
			}
		}
		return null;
	}
}
