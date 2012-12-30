/*
 * Created on 30.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.util;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectUtil {
	
	public static Boolean toBoolean(Object o, Boolean defaultValue) {
		if (o != null) {
			if (o instanceof Boolean) {
				return (Boolean) o;
			} if (o instanceof Integer) {
				return (((Integer) o).intValue() != 0 ? Boolean.TRUE : Boolean.FALSE);
			} else {
				return new Boolean(o.toString());
			}
		} else {
			return defaultValue;
		}
	}

	public static Integer toInteger(Object o, Integer defaultValue) {
		if (o != null) {
			if (o instanceof Integer) {
				return (Integer) o;
			} else {
				return new Integer(o.toString());
			}
		} else {
			return defaultValue;
		}
	}

}
