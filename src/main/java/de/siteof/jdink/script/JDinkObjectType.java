/*
 * Created on 30.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.script;

/**
 * <p>Generic object type.</p>
 * <p>Does not offer conversion other than casting to super class/interface.</p>
 */
public class JDinkObjectType extends JDinkType {

	private static final long serialVersionUID = 1L;

	private Class<?> c;

	protected JDinkObjectType(Class<?> c) {
		this.c = c;
	}

	@Override
	public Object convert(Object value) {
		if ((value == null) || (c.isInstance(value))) {
			return value;
		}
		throw new IllegalArgumentException("cannot convert to " + c.getName() + ": " + value);
	}
	
	public static JDinkObjectType getObjectTypeInstance(Class<?> c) {
		return new JDinkObjectType(c);
	}
}
