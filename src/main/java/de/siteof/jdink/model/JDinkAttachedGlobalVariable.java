package de.siteof.jdink.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.script.JDinkVariable;
import de.siteof.jdink.script.util.JDinkTypeUtil;

public class JDinkAttachedGlobalVariable<T> {

	private static final Log log = LogFactory.getLog(JDinkAttachedGlobalVariable.class);

	private final String variableName;
	private final Class<?> type;
	private transient JDinkVariable variable;

	public JDinkAttachedGlobalVariable(String variableName, Class<?> type) {
		this.variableName = variableName;
		this.type = type;
	}

	public void detach() {
		this.variable = null;
	}

	public String getVariableName() {
		return variableName;
	}

	private JDinkVariable getVariable(JDinkContext context) {
		JDinkVariable variable = this.variable;
		if (variable == null) {
			variable = context.getGlobalScope().getVariable(this.getVariableName());
			if (variable == null) {
				log.warn("[getVariable] variable not found, variableName=[" +
						this.getVariableName() + "]");
			}
		}
		return variable;
	}

	@SuppressWarnings("unchecked")
	public T getValue(JDinkContext context) {
		T result = null;
		JDinkVariable variable = this.getVariable(context);
		if (variable != null) {
			Object value = variable.getValue();
			if (value != null) {
				if (type.isInstance(value)) {
					result = (T) value;
				} else {
					log.warn("[getVariable] variable of unexpected type, variableName=[" +
							this.getVariableName() +
							"], expectedType=[" + this.type + "], actualType=[" + value.getClass() + "]");
				}
			} else {
				log.warn("[getVariable] variable declared but value is null, variableName=[" +
						this.getVariableName() + "]");
			}
		}
		return result;
	}

	public void setValue(JDinkContext context, T value) {
		JDinkVariable variable = this.getVariable(context);
		if (variable != null) {
			variable.setValue(value);
		} else {
			// create one
			variable = new JDinkVariable(JDinkTypeUtil.getType(value),
					value);
			context.getGlobalScope().addVariable(this.getVariableName(), variable);
		}
	}
}