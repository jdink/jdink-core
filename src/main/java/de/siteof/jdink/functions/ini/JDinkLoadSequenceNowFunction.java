package de.siteof.jdink.functions.ini;

import de.siteof.jdink.functions.JDinkExecutionContext;



/**
 * <p>dink.ini method. Loads a sequence.</p>
 *
 * <p>Signature: load_sequence_now imagePrefix sequenceNumber frameNumber offsetX offsetY hardX1 hardY1 hardX2 hardY2</p>
 */
public class JDinkLoadSequenceNowFunction extends JDinkLoadSequenceFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		//log.debug("JDinkLoadSequenceNowFunction!");
		//return eval(executionContext, true);
		return invoke(executionContext, false);
	}

}
