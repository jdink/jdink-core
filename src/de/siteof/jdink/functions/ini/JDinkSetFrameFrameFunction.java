/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.functions.ini;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkSetFrameFrameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkSetFrameFrameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer seqNumber = this.toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = this.toInteger(executionContext.getArgument(1), null);
		Integer sourceSeqNumber = this.toInteger(executionContext.getArgument(2), new Integer(-1));
		Integer sourceFrameNumber = this.toInteger(executionContext.getArgument(3), null);
		assertNotNull(seqNumber, "seqNumber missing");
		assertNotNull(frameNumber, "frameNumber missing");
		JDinkSequence sequence = executionContext.getContext().getSequence(seqNumber.intValue(), true);
		JDinkSequenceFrame frame = sequence.getFrame(frameNumber.intValue(), true);
		if (sourceSeqNumber.intValue() != -1) {
			assertNotNull(sourceFrameNumber, "sourceFrameNumber missing");
			JDinkSequence sourceSequence = executionContext.getContext().getSequence(sourceSeqNumber.intValue(), false);
			assertNotNull(sourceSequence, "sourceSequence " + sourceSeqNumber + " not found");
			JDinkSequenceFrame sourceFrame = sourceSequence.getFrame(sourceFrameNumber.intValue(), false);
			if (sourceFrame == null) {
				if (log.isDebugEnabled()) {
					log.debug("frame not found: " + sourceSeqNumber + "." + sourceFrameNumber);
				}
			} else {
				//assertNotNull(sourceFrame, "sourceFrame " + sourceFrameNumber + " not found");
				frame.setFileName(sourceFrame.getFileName());
				frame.setImage(sourceFrame.getImage());
			}
		} else {
		}
		return null;
	}

}
