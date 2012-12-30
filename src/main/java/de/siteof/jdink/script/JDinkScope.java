package de.siteof.jdink.script;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.script.util.JDinkTypeUtil;

/**
 * <p>Defines the scope for an execution context. Currently that only affects variables.</p>
 * <p>Scopes may be nested - i.e. if a variable wasn't found in this scope, it may exist in the parent scope.</p>
 */
public class JDinkScope implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkScope.class);

	private final JDinkScope parent;
	private final Map<String, JDinkVariable> variableMap = new HashMap<String, JDinkVariable>();

	public JDinkScope(JDinkScope parent) {
		this.parent = parent;
	}

	public JDinkScope() {
		this(null);
	}

	public void clear() {
		this.variableMap.clear();
	}

	public void addVariable(String name, JDinkVariable variable) {
		JDinkVariable oldVariable = getLocalVariable(name);
		if (oldVariable != null) {
//			throw new RuntimeException("variable already set: " + name);
			log.warn("variable already set: " + name);
		}
		variableMap.put(name, variable);
	}

	public void removeVariable(String name, JDinkVariable variable) {
		JDinkVariable oldVariable = getLocalVariable(name);
		if (oldVariable == null) {
			throw new RuntimeException("variable not defined: " + name);
		}
		if (oldVariable != variable) {
			throw new RuntimeException("variable does not match: " + name);
		}
		variableMap.remove(name);
	}

	public void removeVariable(String name) {
		JDinkVariable oldVariable = (JDinkVariable) variableMap.remove(name);
		if (oldVariable == null) {
			throw new RuntimeException("variable not defined: " + name);
		}
	}

	public void setVariableValue(String name, Object value) {
		JDinkVariable variable = getVariable(name);
		if (variable != null) {
			variable.setValue(value);
		} else {
			JDinkType type = JDinkTypeUtil.getType(value);
			variable = new JDinkVariable(type, value);
			this.addVariable(name, variable);
//			throw new RuntimeException("variable not defined: " + name);
		}
//
//		if ((variableMap.get(name) != null) || (parent == null) || (!parent.hasVariable(name))) {
//			if (value == null) {
//				variableMap.remove(name);
//			} else {
//				variableMap.put(name, value);
//			}
//		} else {
//			parent.setVariable(name, value);
//		}
	}

	public boolean hasVariable(String name) {
		return getVariable(name) != null;
	}

	public Collection<String> getLocalVariableNames() {
		return variableMap.keySet();
	}

	public JDinkVariable getLocalVariable(String name) {
		return (JDinkVariable) variableMap.get(name);
	}

	public JDinkVariable getVariable(String name) {
		JDinkVariable variable = (JDinkVariable) variableMap.get(name);
		if ((variable == null) && (parent != null)) {
			variable = parent.getVariable(name);
		}
		return variable;
	}

	public Object getVariableValue(String name, Object defaultValue) {
		JDinkVariable variable = getVariable(name);
		if (variable != null) {
			return variable.getValue();
		} else {
			return defaultValue;
		}
	}

	public Object getVariableValue(String name) {
		JDinkVariable variable = getVariable(name);
		if (variable != null) {
			return variable.getValue();
		} else {
			throw new RuntimeException("variable not defined: " + name);
		}
	}

	protected String getInternalVariableName(String name) {
		return "!" + name;
	}

	public boolean hasInternalVariable(String name) {
		return hasVariable(getInternalVariableName(name));
	}

	public void addInternalVariable(String name, JDinkVariable variable) {
		addVariable(getInternalVariableName(name), variable);
	}

	public void removeInternalVariable(String name, JDinkVariable variable) {
		removeVariable(getInternalVariableName(name), variable);
	}

	public JDinkVariable getInternalVariable(String name) {
		return getVariable(getInternalVariableName(name));
	}

	public void setInternalVariableValue(String name, Object value) {
		setVariableValue(getInternalVariableName(name), value);
	}

	public Object getInternalVariableValue(String name) {
		return getVariableValue(getInternalVariableName(name));
	}

	public JDinkScope getParent() {
		return parent;
	}
}
