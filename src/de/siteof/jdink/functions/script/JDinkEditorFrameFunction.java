package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkMapState;

/**
 * <p>Function: int editor_frame(int editorSpriteNumber, int frame)</p>
 */
public class JDinkEditorFrameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkEditorFrameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer editorSpriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer frameNumber = toInteger(executionContext.getArgument(1), null);
		assertNotNull(editorSpriteNumber, "editor_frame: editorSpriteNumber missing");
		assertNotNull(frameNumber, "editor_frame: frameNumber missing");
		if (log.isDebugEnabled()) {
			log.debug("editor_frame: editorSpriteNumber=" + editorSpriteNumber + " frameNumber=" + frameNumber);
		}
		int result;
		if ((editorSpriteNumber != null) && (frameNumber != null) &&
				(editorSpriteNumber.intValue() >= 1) && (editorSpriteNumber.intValue() < 99)) {
			JDinkMapState mapState = executionContext.getContext().getMapState(true);
			int oldValue = mapState.getEditorFrameNumber(editorSpriteNumber.intValue());
			if (oldValue != frameNumber.intValue()) {
				mapState.setEditorFrameNumber(editorSpriteNumber.intValue(), frameNumber.intValue());
			}
			result = frameNumber.intValue();
		} else {
			result = -1;
		}
		return Integer.valueOf(result);
	}

}
