/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.script;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.format.dinkc.JDinkCLoader;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkScriptBlock extends AbstractJDinkScriptCall {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkCLoader.class);

	private final Collection<JDinkScriptCall> calls = new ArrayList<JDinkScriptCall>();

	public void addCall(JDinkScriptCall call) {
		calls.add(call);
	}
	public JDinkScriptCall[] getCalls() {
		return (JDinkScriptCall[]) calls.toArray(new JDinkScriptCall[0]);
	}

	private int findDefineLabel(JDinkScriptCall[] calls, String labelName) {
		int result = -1;
		for (int iCall = 0; iCall < calls.length; iCall++) {
			JDinkScriptCall call = calls[iCall];
			if (call instanceof JDinkDefineLabelFunctionCall) {
				if (((JDinkDefineLabelFunctionCall) call).getName().equals(labelName)) {
					result = iCall;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		JDinkScriptCall[] calls = getCalls();
		int iCall = 0;
		while (iCall < calls.length) {
			JDinkScriptCall call = calls[iCall++];
			//log.debug("function:" + call.getFunctionName());
			call.invoke(executionContext);

			switch (executionContext.getState()) {
			case JDinkExecutionContext.STATE_RETURN:
			case JDinkExecutionContext.STATE_EXIT:
				iCall = calls.length;
				break;
			case JDinkExecutionContext.STATE_GOTO:
				String label = (String) executionContext.getResult();
				int labelIndex = findDefineLabel(calls, label);
				if (labelIndex >= 0) {
					if (log.isDebugEnabled()) {
						log.debug("label found, label=[" + label + "], index=[" + labelIndex + "]");
					}
					iCall = labelIndex; // we could also point to the index after
					executionContext.setState(JDinkExecutionContext.STATE_NONE);
					executionContext.setResult(null);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("label not found - parent blockt might have, label=[" + label + "]");
					}
					iCall = calls.length;
				}
				break;
			}
		}
		return executionContext.getResult();
	}
}
