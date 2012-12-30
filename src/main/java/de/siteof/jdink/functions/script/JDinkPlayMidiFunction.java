package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: void playmidi(String name)</p>
 */
public class JDinkPlayMidiFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkPlayMidiFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String name = executionContext.getArgumentAsString(0, null);
		assertNotNull(name, "play midi: name missing");
		if (log.isDebugEnabled()) {
			log.debug("play midi: " + name);
		}
		log.warn("playmidi not implemented");
		return null;
	}

}
