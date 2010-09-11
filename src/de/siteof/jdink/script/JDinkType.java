package de.siteof.jdink.script;

import java.io.Serializable;

/**
 * <p>Base class for all JDink types.</p>
 */
public abstract class JDinkType implements Serializable {

	private static final long serialVersionUID = 1L;

	public Object convert(Object value) {
		return value;
	}
}
