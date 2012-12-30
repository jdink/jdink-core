/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Implementation of {@link JDinkScriptCall} forming an if statement</p>
 * 
 * <p>if ( argument(0) ) { argument(1) } else { argument(2) }</p>
 */
public class JDinkScriptIfFunctionCall extends JDinkScriptFunctionCall {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkScriptIfFunctionCall.class);

	public JDinkScriptIfFunctionCall() {
		super("!IF");
	}

	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object value  = getArgument(0);
		Object thenStatement  = getArgument(1);
		Object elseStatement  = getArgument(2);
		if (log.isDebugEnabled()) {
			log.debug("if " + value + " then " + thenStatement + " else " + elseStatement);
		}
		if (value instanceof JDinkScriptFunctionCall) {
			value = ((JDinkScriptFunctionCall) value).invoke(executionContext);
		}
		if (thenStatement == null) {
			throw new RuntimeException("then-statement missing");
		}
		boolean b;
		if (value instanceof Boolean) {
			b = ((Boolean) value).booleanValue();
		} else if (value instanceof String) {
			b = new Boolean((String) value).booleanValue();
		} else if (value instanceof Integer) {
			b = ((Integer) value).intValue() != 0;
		} else {
			throw new IllegalArgumentException("if: boolean value expected");
		}
		if (log.isDebugEnabled()) {
			log.debug("b=" + b);
		}
		if (b) {
			return asValue(executionContext, thenStatement);
		} else {
			return asValue(executionContext, elseStatement);
		}
	}
}
