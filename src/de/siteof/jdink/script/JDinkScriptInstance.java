package de.siteof.jdink.script;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.brain.JDinkItemMenuBrain;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;

/**
 * <p>Wrapper around a script instance. A script instance consists of the script and the instance scope.</p>
 */
public class JDinkScriptInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkItemMenuBrain.class);

	public static final Object[] EMPTY_ARGUMENTS = new Object[0];

	private boolean initialized;
	private JDinkScriptFile scriptFile;
	private JDinkScope scope;

	public JDinkScriptInstance() {
	}

	public JDinkScriptInstance(JDinkScriptFile scriptFile, JDinkScope scope) {
		this.scriptFile = scriptFile;
		this.scope = scope;
	}

	public JDinkScriptFunction getFunctionByName(String name) {
		return this.getScriptFile().getFunctionByName(name);
	}

	public Object callFunction(JDinkContext context, JDinkScriptFunction function) throws Throwable {
		return callFunction(context, function, EMPTY_ARGUMENTS);
	}

	public Object callFunction(JDinkContext context, JDinkScriptFunction function, Object[] arguments) throws Throwable {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[callFunction] calling function, scriptFile=[" + scriptFile.getFileName() +
						"], function=[" + function.getName() + "]");
			}
			if (arguments == null) {
				arguments = new Object[0];
			}
			JDinkScope scope = getScope();
			if (scope == null) {
				scope = new JDinkScope(context.getGlobalScope());
				this.setScope(scope);
				scope.addInternalVariable(JDinkScriptConstants.RESULT_VARNAME, new JDinkVariable(
						JDinkIntegerType.getInstance(),
						Integer.valueOf(0)));
			}
			scope = new JDinkScope(scope != null ? scope : context.getGlobalScope());
			scope.addInternalVariable(JDinkScriptConstants.THIS_INTERNAL_VARNAME, new JDinkVariable(
					JDinkObjectType.getObjectTypeInstance(JDinkScriptInstance.class),
					this));

			JDinkExecutionContext executionContext = new JDinkExecutionContext();
			executionContext.setContext(context);
			executionContext.setFunctionName(function.getName());
			executionContext.setArguments(arguments);
			executionContext.setScope(scope);
			return function.invoke(executionContext);
		} catch (Exception e) {
			throw new JDinkScriptRuntimeException(this, "failed to invoke " + scriptFile.getFileName() + "." + function.getName(), e);
		}
	}

	public Object callFunction(JDinkContext context, String name, Object[] arguments) throws Throwable {
		JDinkScriptFunction function = getFunctionByName(name);
		if (function != null) {
			return callFunction(context, function, arguments);
		} else {
			return null;
		}
	}

	public void initialize(JDinkContext context, Object[] arguments) throws Throwable {
		this.setInitialized(true);
		callFunction(context, "main", arguments);
	}

	public void initialize(JDinkContext context) throws Throwable {
		initialize(context, null);
	}

	public boolean isInitialized() {
		return initialized;
	}
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	public JDinkScriptFile getScriptFile() {
		return scriptFile;
	}
	public void setScriptFile(JDinkScriptFile scriptFile) {
		this.scriptFile = scriptFile;
	}
	public JDinkScope getScope() {
		return scope;
	}
	public void setScope(JDinkScope scope) {
		this.scope = scope;
	}
}
