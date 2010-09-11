package de.siteof.jdink.functions.script;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int random(int num, int offset)</p>
 */
public class JDinkRandomFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkRandomFunction.class);
	
	private final Random random = new Random();

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer num = toInteger(executionContext.getArgument(0), null);
		Integer offset = toInteger(executionContext.getArgument(1), null);
		assertNotNull(num, "editor_type: num missing");
		assertNotNull(offset, "editor_type: offset missing");
		if (log.isDebugEnabled()) {
			log.debug("editor_type: num=" + num + " offset=" + offset);
		}
		int result;
		if ((num != null) && (offset != null) && (num.intValue() >= 1)) {
			result = offset.intValue() + random.nextInt(num.intValue());
		} else {
			result = -1;
		}
		return Integer.valueOf(result);
	}

}
