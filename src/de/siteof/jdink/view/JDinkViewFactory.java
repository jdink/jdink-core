package de.siteof.jdink.view;


public class JDinkViewFactory {

	private String viewClassName = "de.siteof.jdink.view.swing.JDinkSwingView";
	
	private static final JDinkViewFactory instance = new JDinkViewFactory();

	public static JDinkViewFactory getInstance() {
		return instance;
	}

	public JDinkView createView() {
		try {
			return (JDinkView) Class.forName(viewClassName).newInstance();
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
