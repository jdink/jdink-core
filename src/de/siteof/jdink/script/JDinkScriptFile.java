package de.siteof.jdink.script;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Represents a whole script file. The script files functions are exposed and may be called.</p>
 */
public class JDinkScriptFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private Collection<JDinkScriptFunction> functions = new ArrayList<JDinkScriptFunction>();
	private String fileName;
	
	@Override
	public String toString() {
		return "JDinkScriptFile [fileName=" + fileName + "]";
	}

	public void addFunction(JDinkScriptFunction function) {
		functions.add(function);
	}
	public JDinkScriptFunction[] getFunctions() {
		return (JDinkScriptFunction[]) functions.toArray(new JDinkScriptFunction[0]);
	}
	
	public JDinkScriptFunction getFunctionByName(String name) {
		for (JDinkScriptFunction function: functions) {
			if (function.getName().equals(name)) {
				return function;
			}
		}
		return null;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
