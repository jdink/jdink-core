package de.siteof.jdink.script;

import de.siteof.jdink.functions.JDinkFunction;

/**
 * <p>Extends {@link JDinkFunction}, for script calls.</p>
 */
public interface JDinkScriptCall extends JDinkFunction {

	void setLineNo(int lineNr);
	
}
