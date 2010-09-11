package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int editor_type(int editorSpriteNumber, int type)</p>
 */
public class JDinkEditorTypeFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkEditorTypeFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer editorSpriteNumber = toInteger(executionContext.getArgument(0), null);
		Integer type = toInteger(executionContext.getArgument(1), null);
		assertNotNull(editorSpriteNumber, "editor_type: editorSpriteNumber missing");
		assertNotNull(type, "editor_type: type missing");
		if (log.isDebugEnabled()) {
			log.debug("editor_type: editorSpriteNumber=" + editorSpriteNumber + " type=" + type);
		}
		int result;
		if ((editorSpriteNumber != null) && (type != null) &&
				(editorSpriteNumber.intValue() >= 1) && (editorSpriteNumber.intValue() < 99)) {
			int oldState = executionContext.getContext().getAndSetEditorSpriteState(
					editorSpriteNumber.intValue(), type.intValue());
			if (oldState != type.intValue()) {
//				executionContext.getContext().getController().setChanged(true);
			}
			result = type.intValue();
		} else {
			result = -1;
		}
		return Integer.valueOf(result);
	}

}
