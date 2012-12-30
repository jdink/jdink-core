package de.siteof.jdink.functions.ini;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;

/**
 * <p>Function: set_frame_special int sequenceNumber int frameNumber int special</p>
 */
public class JDinkSetFrameSpecialFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer sequenceNumber = this.toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = this.toInteger(executionContext.getArgument(1), null);
		Integer special = this.toInteger(executionContext.getArgument(2), null);
		assertNotNull(sequenceNumber, "sequenceNumber missing");
		assertNotNull(frameNumber, "frameNumber missing");
		assertNotNull(special, "special missing");
		
		JDinkSequenceFrameAttributes frameAttributes =
			executionContext.getContext().getFrameAttributes(
					sequenceNumber.intValue(), frameNumber.intValue(), true);
		frameAttributes.setSpecial(special.intValue() != 0);
		return null;
	}

}
