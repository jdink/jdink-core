package de.siteof.jdink.script;

/**
 * <p>Represents an integer type.</p>
 * @see JDinkType
 */
public class JDinkIntegerType extends JDinkType {

	private static final long serialVersionUID = 1L;

	private static JDinkIntegerType instance = new JDinkIntegerType(); 

	@Override
	public String toString() {
		return "Integer";
	}

	@Override
	public Object convert(Object value) {
		if ((value == null) || (value instanceof Integer)) {
			return value;
		}
		if (value instanceof String) {
			return new Integer((String) value);
		}
		throw new IllegalArgumentException("cannot convert to int: " + value);
	}
	
	public static JDinkIntegerType getInstance() {
		return instance;
	}
}
