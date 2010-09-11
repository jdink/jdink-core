/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.util;

import de.siteof.util.bean.BeanClassWrapper;
import de.siteof.util.bean.IBeanClassPropertyWrapper;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringUtil {

	private static int depth;

	public static String toString(Object o) {
		return toString(o, false);
	}

	public static String toString(Object o, boolean recursive) {
		if (o == null) {
			return "null";
		}
		depth++;
		try {
			if (o instanceof String) {
				return (String) o;
			}
			if ((o instanceof Number) || (o instanceof Character)) {
				return o.toString();
			}
			if (depth > 1000) {
				return "?";
			}
			try {
				BeanClassWrapper classWrapper = BeanClassWrapper.getInstance(o.getClass());
				IBeanClassPropertyWrapper[] propertyWrappers	= classWrapper.getBeanClassPropertyWrappers();
				StringBuffer sb = new StringBuffer();
				sb.append(o.getClass());
				sb.append(" (");
				sb.append(o.hashCode());
				sb.append(")\n");
				for (int i = 0; i < propertyWrappers.length; i++) {
					if ((propertyWrappers[i].isStatic()) || (!propertyWrappers[i].isEditable())) {
						continue;
					}
					Object value	= propertyWrappers[i].getValue(o);
					sb.append(propertyWrappers[i].getName());
					sb.append('=');
					if (value == null) {
						sb.append("null");
					} else {
						if (recursive) {
							sb.append(toString(value, true));
						} else {
							sb.append(value.toString());
						}
						sb.append('\n');
					}
				}
				return sb.toString();
			} catch (Exception e) {
			}
			return o.toString();
		} finally {
			depth--;
		}
	}
}
