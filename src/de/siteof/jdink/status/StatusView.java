package de.siteof.jdink.status;

import de.siteof.jdink.model.JDinkContext;

public interface StatusView {
	
	boolean update(JDinkContext context);
	
	void show(JDinkContext context);

}
