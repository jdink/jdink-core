package de.siteof.jdink.functions.ini;


import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.ini.util.JDinkSequenceLazyLoader;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.view.ColorConstants;


/**
 * <p>dink.ini method. Loads a sequence.</p>
 *
 * <p>Signature: load_sequence imagePrefix sequenceNumber frameNumber offsetX offsetY hardX1 hardY1 hardX2 hardY2</p>
 */
public class JDinkLoadSequenceFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkLoadSequenceFunction.class);


	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		//log.debug("JDinkLoadSequenceFunction!");
		return invoke(executionContext, false);
	}

	public Object invoke(JDinkExecutionContext executionContext, boolean loadImages) throws Exception {
		String fileNamePrefix = executionContext.getArgumentAsString(0, "");
		if (fileNamePrefix.length() > 0) {
			Integer sequenceNumber = this.toInteger(executionContext.getArgument(1), null);
			Integer frameNumber = null;
			Integer offsetX = null;
			Integer offsetY = null;
			Integer hardX1 = null;
			Integer hardY1 = null;
			Integer hardX2 = null;
			Integer hardY2 = null;
			boolean animation = true;
			boolean leftAligned = false;
			assertNotNull(sequenceNumber, "sequenceNumber missing");
			int backgroundColor	= ColorConstants.NONE;
			try {
				String sequenceType = executionContext.getArgumentAsString(2, "");
				if (sequenceType.equals("BLACK")) {
					backgroundColor = ColorConstants.BLACK;
					animation = false;
				} else if (sequenceType.equals("NOTANIM")) {
					// TODO is there a bug in dinkvar.h that this should actually still treat it as an animation?
					animation = false;
					backgroundColor = ColorConstants.WHITE;
				} else if (sequenceType.equals("LEFTALIGN")) {
					leftAligned = true;
				} else {
//					Object frameParameter = executionContext.getArgument(2);
//					if ("NOTANIM".equals(frameParameter)) {
//						// do nothing
//						log.info("NOTANIM not implemented - " + Arrays.asList(executionContext.getArguments()));
//					} else if ("LEFTALIGN".equals(frameParameter)) {
//						// do nothing
//						log.info("LEFTALIGN not implemented - " + Arrays.asList(executionContext.getArguments()));
//					} else if ("BLACK".equals(frameParameter)) {
//						// do nothing
//						log.info("BLACK not implemented - " + Arrays.asList(executionContext.getArguments()));
// 					} else {
 						backgroundColor = ColorConstants.WHITE;
 						frameNumber = this.toInteger(executionContext.getArgument(2), null);
						offsetX = this.toInteger(executionContext.getArgument(3), null);
						offsetY = this.toInteger(executionContext.getArgument(4), null);
						hardX1 = this.toInteger(executionContext.getArgument(5), null);
						hardY1 = this.toInteger(executionContext.getArgument(6), null);
						hardX2 = this.toInteger(executionContext.getArgument(7), null);
						hardY2 = this.toInteger(executionContext.getArgument(8), null);
	//				assertNotNull(speed, "speed missing");
	//				assertNotNull(offsetX, "offsetX missing");
	//				assertNotNull(offsetY, "offsetY missing");
	//				assertNotNull(hardX1, "hardX1 missing");
	//				assertNotNull(hardY1, "hardY1 missing");
	//				assertNotNull(hardX2, "hardX2 missing");
	//				assertNotNull(hardY2, "hardY2 missing");
// 					}
				}
			} catch (Exception e) {
				log.error("error parsing sequence parameters (" + Arrays.asList(executionContext.getArguments()) + ")" + " - " + e, e);
			}
			JDinkSequence sequence = executionContext.getContext().getSequence(sequenceNumber.intValue(), true);
			sequence.clear();
//			if (backgroundColor == ColorConstants.NONE) {
//				backgroundColor = ColorConstants.WHITE;
//			}
			sequence.setBackgroundColor(backgroundColor);
			sequence.setLazyLoader(new JDinkSequenceLazyLoader(
					executionContext.getContext(),
					sequenceNumber.intValue(),
					fileNamePrefix,
					offsetX,
					offsetY,
					hardX1,
					hardY1,
					hardX2,
					hardY2));
			sequence.setAnimation(animation);
			sequence.setLeftAligned(leftAligned);
			if (frameNumber != null) {
				sequence.setDefaultFrameNumber(frameNumber.intValue());
			}
		} else {
			log.debug("file name prefix missing");
		}
		return null;
	}
}
