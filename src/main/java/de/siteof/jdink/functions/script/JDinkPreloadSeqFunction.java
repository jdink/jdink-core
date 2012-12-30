/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.functions.script;

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
public class JDinkPreloadSeqFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkPreloadSeqFunction.class);

	/* (non-Javadoc)
	 * @see de.siteof.jdink.functions.JDinkFunction#invoke(de.siteof.jdink.functions.JDinkExecutionContext)
	 */
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		log.debug("JDinkPreloadSeqFunction!");
		Integer seqNumber = toInteger(executionContext.getArgument(0), null);
		assertNotNull(seqNumber, "seqNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("seqNumber=" + seqNumber);
		}
		JDinkSequence sequence = executionContext.getContext().getSequence(seqNumber.intValue(), false);
		assertNotNull(sequence, "sequence " + seqNumber + " not found");
		JDinkSequenceFrame[] frames = sequence.getFrames();
		for (int i = 0; i < frames.length; i++) {
			JDinkSequenceFrame frame = frames[i];
			String fileName = frame.getFileName();
			if (frame.getImage() == null) {
				executionContext.getContext().getImage(sequence, frame);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("image already loaded: " + fileName);
				}
			}
		}
		if (frames.length == 0) {
			log.debug("no frames in sequence?");
		}
		return null;
	}

}
