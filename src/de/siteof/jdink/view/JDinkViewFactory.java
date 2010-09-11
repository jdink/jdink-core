package de.siteof.jdink.view;

import java.lang.reflect.Constructor;


public class JDinkViewFactory {

	private String viewClassName = "de.siteof.jdink.view.swing.JDinkSwingView";

	private static final JDinkViewFactory instance = new JDinkViewFactory();

	public static JDinkViewFactory getInstance() {
		return instance;
	}

	public JDinkView createView(Object viewInitParameter) {
		try {
			Class<?> c = Class.forName(viewClassName);
			Constructor<?> constructor = null;
			for (Constructor<?> tempConstructor: c.getConstructors()) {
				if (tempConstructor.getParameterTypes().length == 1) {
					constructor = tempConstructor;
				}
			}
			JDinkView view;
			if (constructor != null) {
				view = (JDinkView) constructor.newInstance(viewInitParameter);
			} else {
				view = (JDinkView) c.newInstance();
			}
			return view;
		} catch (Throwable e) {
			throw new RuntimeException("Failed to create view(" + viewClassName + ") - " + e, e);
		}
	}

	/**
	 * @return the viewClassName
	 */
	public String getViewClassName() {
		return viewClassName;
	}

	/**
	 * @param viewClassName the viewClassName to set
	 */
	public void setViewClassName(String viewClassName) {
		this.viewClassName = viewClassName;
	}

}
