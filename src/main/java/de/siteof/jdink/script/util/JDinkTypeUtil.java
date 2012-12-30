package de.siteof.jdink.script.util;

import de.siteof.jdink.script.JDinkBooleanType;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkObjectType;
import de.siteof.jdink.script.JDinkType;

public final class JDinkTypeUtil {
	
	private JDinkTypeUtil() {
		// prevent instantiation
	}
	
	public static JDinkType getType(Object value) {
		JDinkType result;
		if (value == null) {
			result = JDinkObjectType.getObjectTypeInstance(Object.class);
		} else if (value instanceof Integer) {
			result = JDinkIntegerType.getInstance();
		} else if (value instanceof Boolean) {
			result = JDinkBooleanType.getBooleanTypeInstance();
		} else {
			result = JDinkObjectType.getObjectTypeInstance(Object.class);
		}
		return result;
	}

}
