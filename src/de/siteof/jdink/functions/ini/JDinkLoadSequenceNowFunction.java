/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.functions.ini;

import de.siteof.jdink.functions.JDinkExecutionContext;



/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkLoadSequenceNowFunction extends JDinkLoadSequenceFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		//log.debug("JDinkLoadSequenceNowFunction!");
		//return eval(executionContext, true);
		return invoke(executionContext, false);
	}

}
