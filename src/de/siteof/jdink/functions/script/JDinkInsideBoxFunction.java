package de.siteof.jdink.functions.script;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.geom.JDinkRectangle;

/**
 * <p>Function: void inside_box(int testX, int testY, int boxX1, int boxY1, int boxX2, int boxY2)</p>
 */
public class JDinkInsideBoxFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

//	private static final Log log = LogFactory.getLog(JDinkInsideBoxFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Integer testX = toInteger(executionContext.getArgument(0), null);
		Integer testY = toInteger(executionContext.getArgument(1), null);
		Integer boxX1 = toInteger(executionContext.getArgument(2), null);
		Integer boxY1 = toInteger(executionContext.getArgument(3), null);
		Integer boxX2 = toInteger(executionContext.getArgument(4), null);
		Integer boxY2 = toInteger(executionContext.getArgument(5), null);
		assertNotNull(testX, "inside_box: testX missing");
		assertNotNull(testY, "inside_box: testY missing");
		assertNotNull(boxX1, "inside_box: boxX1 missing");
		assertNotNull(boxY1, "inside_box: boxY1 missing");
		assertNotNull(boxX2, "inside_box: boxX2 missing");
		assertNotNull(boxY2, "inside_box: boxY2 missing");
		JDinkRectangle box = JDinkRectangle.between(
				boxX1.intValue(), boxY1.intValue(),
				boxX2.intValue(), boxY2.intValue());
		int result;
		if (box.contains(testX.intValue(), testY.intValue())) {
			result = 1;
		} else {
			result = 0;
		}
		return Integer.valueOf(result);
	}

}
