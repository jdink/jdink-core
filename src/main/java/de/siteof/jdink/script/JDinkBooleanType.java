package de.siteof.jdink.script;

/**
 * <p>Boolean implementation of {@link JDinkType}</p>
 */
public class JDinkBooleanType extends JDinkType {

	private static final long serialVersionUID = 1L;
	
	private static JDinkBooleanType instance = new JDinkBooleanType(); 

	@Override
	public Object convert(Object value) {
		if ((value == null) || (value instanceof Boolean)) {
			return value;
		}
		if (value instanceof String) {
			return new Boolean((String) value);
		}
		throw new IllegalArgumentException("cannot convert to bool: " + value);
	}
	
	public static JDinkBooleanType getBooleanTypeInstance() {
		return instance;
	}
}
