package de.siteof.jdink.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptConstants;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.util.ObjectUtil;


/**
 * <p>Abstract class for all {@link JDinkFunction} implementations.</p>
 */
public abstract class AbstractJDinkFunction implements JDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(AbstractJDinkFunction.class);
	
	protected static final Object VOID = null;


	@Override
	public String toString() {
		return "JDinkFunction [className=" + this.getClass().getSimpleName() + "]";
	}

	protected void assertNotNull(Object o, String text) {
		if (o == null) {
			throw new RuntimeException(text);
		}
	}

	protected Integer toInteger(Object o, Integer defaultValue) {
		return ObjectUtil.toInteger(o, defaultValue);
	}
	protected Boolean toBoolean(Object o, Boolean defaultValue) {
		return ObjectUtil.toBoolean(o, defaultValue);
	}

	protected JDinkScope requestSpriteScope(JDinkSprite sprite, JDinkContext context) {
		return sprite.requestScope(context);
	}

	protected void setResult(JDinkExecutionContext executionContext, int result) {
		JDinkScriptInstance thisValue = (JDinkScriptInstance) executionContext.getScope().getInternalVariableValue("this");
		if (thisValue == null) {
			throw new RuntimeException("this == null?!");
		}
		thisValue.getScope().setVariableValue(JDinkScriptConstants.RESULT_VARNAME, new Integer(result));
	}

	protected Object returnFunctionCall(JDinkExecutionContext executionContext, Object returnValue) {
		if (log.isDebugEnabled()) {
			log.debug("return function call: returnValue=[" + returnValue + "]");
		}
		executionContext.setResult(returnValue);
		executionContext.setState(JDinkExecutionContext.STATE_RETURN);
		return returnValue;
	}

	protected Object returnFunctionCall(JDinkExecutionContext executionContext) {
		return this.returnFunctionCall(executionContext, null);
	}

	protected void updateFrame(JDinkExecutionContext executionContext) {
		executionContext.getContext().getController().nextFrame(executionContext.getContext());
		executionContext.getContext().getView().updateView();
	}

	protected void sleep(JDinkExecutionContext executionContext) {
		this.sleep(executionContext, 200);	// was 500
	}

	protected void sleep(JDinkExecutionContext executionContext, long length) {
		executionContext.getContext().getController().sleep(length);
	}

}
