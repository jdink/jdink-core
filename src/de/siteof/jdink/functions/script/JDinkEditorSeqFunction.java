package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkMapState;

/**
 * <p>Function: int editor_seq(int editorSpriteNumber, int sequenceNumber)</p>
 */
public class JDinkEditorSeqFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkEditorSeqFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer editorSpriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer sequenceNumber = toInteger(executionContext.getArgument(1), null);
		assertNotNull(editorSpriteNumber, "editor_seq: editorSpriteNumber missing");
		assertNotNull(sequenceNumber, "editor_seq: sequenceNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("editor_seq: editorSpriteNumber=" + editorSpriteNumber + " sequenceNumber=" + sequenceNumber);
		}
		int result;
		if ((editorSpriteNumber != null) && (sequenceNumber != null) &&
				(editorSpriteNumber.intValue() >= 1) && (editorSpriteNumber.intValue() < 99)) {
			JDinkMapState mapState = executionContext.getContext().getMapState(true);
			int oldValue = mapState.getEditorSequenceNumber(editorSpriteNumber.intValue());
			if (oldValue != sequenceNumber.intValue()) {
				mapState.setEditorSequenceNumber(editorSpriteNumber.intValue(), sequenceNumber.intValue());
			}
			result = sequenceNumber.intValue();
		} else {
			result = -1;
		}
		return Integer.valueOf(result);
	}

}
