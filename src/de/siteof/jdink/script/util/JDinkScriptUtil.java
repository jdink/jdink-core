package de.siteof.jdink.script.util;

import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptFunction;
import de.siteof.jdink.script.JDinkScriptInstance;

public final class JDinkScriptUtil {

	public static final Object SCRIPT_NOT_FOUND = "SCRIPT_NOT_FOUND";

	private JDinkScriptUtil() {
		// prevent instantiation
	}

	public static boolean callStatelessScript(
			JDinkContext context, String scriptName) throws Throwable {
		return callStatelessScript(context, scriptName, null, new Object[0]);
	}

	public static boolean callStatelessScript(
			JDinkContext context, String scriptName, String functionName, Object[] arguments) throws Throwable {
		boolean result = false;
		JDinkScriptFile scriptFile = context.getScript(scriptName, true);
		if (scriptFile != null) {
			if (functionName == null) {
				functionName = "main";
			}
			JDinkScriptFunction function = scriptFile.getFunctionByName(functionName);
			if (function != null) {
				JDinkScriptInstance scriptInstance = new JDinkScriptInstance(
						scriptFile, new JDinkScope(context.getGlobalScope()));
				scriptInstance.callFunction(context, function, arguments);
				result = true;
			}
		}
		return result;
	}

}
