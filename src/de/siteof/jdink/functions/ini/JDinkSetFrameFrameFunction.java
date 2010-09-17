package de.siteof.jdink.functions.ini;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;

/**
 * <p>Function: set_frame_frame int sequenceNumber int frameNumber int sourceSequenceNumber int sourceFrameNumber</p>
 */
public class JDinkSetFrameFrameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSetFrameFrameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer sequenceNumber = this.toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = this.toInteger(executionContext.getArgument(1), null);
		Integer sourceSequenceNumber = this.toInteger(executionContext.getArgument(2), new Integer(-1));
		Integer sourceFrameNumber = this.toInteger(executionContext.getArgument(3), null);
		assertNotNull(sequenceNumber, "sequenceNumber missing");
		assertNotNull(frameNumber, "frameNumber missing");

		// set the frame attributes
		JDinkSequenceFrameAttributes frameAttributes =
			executionContext.getContext().getFrameAttributes(
					sequenceNumber.intValue(), frameNumber.intValue(), true);
		if (sourceSequenceNumber.intValue() >= 0) {
			frameAttributes.setSourceSequenceNumber(sourceSequenceNumber.intValue());
			frameAttributes.setSourceFrameNumber(sourceFrameNumber.intValue());
		} else {
			frameAttributes.setSourceSequenceNumber(-1);
			frameAttributes.setSourceFrameNumber(0);
		}

		// now the sequence if it has been loaded already
		JDinkSequence sequence = executionContext.getContext().getSequence(
				sequenceNumber.intValue(), false);
		if (sequence != null) {
			if (sourceSequenceNumber.intValue() != -1) {
				assertNotNull(sourceFrameNumber, "sourceFrameNumber missing");
				JDinkSequence sourceSequence = executionContext.getContext().getSequence(sourceSequenceNumber.intValue(), false);
				assertNotNull(sourceSequence, "sourceSequence " + sourceSequenceNumber + " not found");
				JDinkSequenceFrame sourceFrame = sourceSequence.getFrame(sourceFrameNumber.intValue(), false);
				if (sourceFrame == null) {
					if (log.isDebugEnabled()) {
						log.debug("frame not found: " + sourceSequenceNumber + "." + sourceFrameNumber);
					}
				} else {
					//assertNotNull(sourceFrame, "sourceFrame " + sourceFrameNumber + " not found");
					JDinkSequenceFrame frame = sequence.getFrame(frameNumber.intValue(), true);
					frame.setFileName(sourceFrame.getFileName());
					frame.setImage(sourceFrame.getImage());
				}
			} else {
				// not implemented - would remove current override
			}
		}

//		JDinkSequence sequence = executionContext.getContext().getSequence(sequenceNumber.intValue(), true);
//		JDinkSequenceFrame frame = sequence.getFrame(frameNumber.intValue(), true);
//		if (sourceSequenceNumber.intValue() != -1) {
//			assertNotNull(sourceFrameNumber, "sourceFrameNumber missing");
//			JDinkSequence sourceSequence = executionContext.getContext().getSequence(sourceSequenceNumber.intValue(), false);
//			assertNotNull(sourceSequence, "sourceSequence " + sourceSequenceNumber + " not found");
//			JDinkSequenceFrame sourceFrame = sourceSequence.getFrame(sourceFrameNumber.intValue(), false);
//			if (sourceFrame == null) {
//				if (log.isDebugEnabled()) {
//					log.debug("frame not found: " + sourceSequenceNumber + "." + sourceFrameNumber);
//				}
//			} else {
//				//assertNotNull(sourceFrame, "sourceFrame " + sourceFrameNumber + " not found");
//				frame.setFileName(sourceFrame.getFileName());
//				frame.setImage(sourceFrame.getImage());
//			}
//		} else {
//		}
		return null;
	}

}
