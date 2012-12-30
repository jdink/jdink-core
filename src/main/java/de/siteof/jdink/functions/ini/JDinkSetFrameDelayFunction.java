package de.siteof.jdink.functions.ini;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;

/**
 * <p>Function: set_frame_delay int sequenceNumber int frameNumber int delay</p>
 */
public class JDinkSetFrameDelayFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer sequenceNumber = this.toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = this.toInteger(executionContext.getArgument(1), null);
		Integer delay = this.toInteger(executionContext.getArgument(2), null);
		assertNotNull(sequenceNumber, "sequenceNumber missing");
		assertNotNull(frameNumber, "frameNumber missing");
		assertNotNull(delay, "delay missing");
		
		JDinkSequenceFrameAttributes frameAttributes =
			executionContext.getContext().getFrameAttributes(
					sequenceNumber.intValue(), frameNumber.intValue(), true);
		frameAttributes.setDelay(delay.intValue());
		return null;
	}

}
