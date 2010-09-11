/*
 * Created on 03.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.loader;

import java.io.Serializable;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface JDinkLazyLoader extends Serializable {

	boolean load(Object source);
	
}
