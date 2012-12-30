package de.siteof.jdink.script;

public class JDinkScriptRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final JDinkScriptInstance scriptInstance;
	
	public JDinkScriptRuntimeException(JDinkScriptInstance scriptInstance, String message, Throwable cause) {
		super(message, cause);
		this.scriptInstance = scriptInstance;
	}

	public JDinkScriptInstance getScriptInstance() {
		return scriptInstance;
	}

}
