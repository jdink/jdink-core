package de.siteof.jdink.functions.script;

import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.loader.JDinkIniLoader;

/**
 * <p>Function: void init(String initString)</p>
 */
public class JDinkInitFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkInitFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String initString = executionContext.getArgumentAsString(0, null);
		assertNotNull(initString, "init: initString missing");
		if (log.isDebugEnabled()) {
			log.debug("init: initString=[" + initString + "]");
		}
		JDinkIniLoader iniLoader = new JDinkIniLoader();
		iniLoader.setContext(executionContext.getContext());
		iniLoader.load(new ByteArrayInputStream(initString.getBytes()));
		return null;
	}

}
