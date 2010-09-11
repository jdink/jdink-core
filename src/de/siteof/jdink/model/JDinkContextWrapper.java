package de.siteof.jdink.model;

public class JDinkContextWrapper {
	
	private final JDinkContext context;
	
	public JDinkContextWrapper(JDinkContext context) {
		this.context = context;
	}
	
	public boolean isScreenLocked() {
		return false;
	}
	

}
