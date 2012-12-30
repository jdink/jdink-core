package de.siteof.jdink.script;

import java.io.Serializable;

/**
 * <p>
 * Represents a single script variable instance.
 * </p>
 */
public class JDinkVariable implements Serializable {

	private static final long serialVersionUID = 1L;

	private JDinkType type;
	private Object value;
	
	public JDinkVariable() {
		
	}
	
	public JDinkVariable(JDinkType type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return "JDinkVariable [type=" + type + ", value=" + value + "]";
	}

	public JDinkType getType() {
		return type;
	}

	public void setType(JDinkType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = type.convert(value);
	}
}
