package de.siteof.jdink.functions.script;

import de.siteof.jdink.app.JDinkGameManager;
import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;

/**
 * <p>Function: void restart_game()</p>
 */
public class JDinkRestartGameFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

//	private static final Log log = LogFactory.getLog(JDinkLoadGameFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Throwable {
		JDinkContext context = executionContext.getContext();
		context.getController().waitForView(context);
		JDinkGameManager gameManager = JDinkGameManager.getInstance(context);
		gameManager.start(context);
		return null;
	}

}
