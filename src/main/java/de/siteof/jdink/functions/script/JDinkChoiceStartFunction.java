package de.siteof.jdink.functions.script;

import de.siteof.jdink.brain.JDinkChoiceBrain;
import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;

/**
 * <p>Function: int choice_start(Object[] choices)</p>
 */
public class JDinkChoiceStartFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		Object[] choiceArguments = (Object[]) executionContext.getArgument(0);
		assertNotNull(choiceArguments, "choice start: choiceArguments missing");
		int result = JDinkChoiceBrain.getInstance().showChoiceMenuAndWait(executionContext, choiceArguments);

		setResult(executionContext, result);
		return new Integer(result);
	}

}
