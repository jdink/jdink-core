package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.model.JDinkContext;

/**
 * <p>Function: void arm_weapon(...)</p>
 */
public class JDinkArmWeaponFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkArmWeaponFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		JDinkContext context = executionContext.getContext();
		if (log.isDebugEnabled()) {
			log.debug("arm weapon: &cur_weapon=" + context.getGlobalVariables().currentWeapon.getInt(context));
		}
		log.warn("arm_weapon not implemented");
		return null;
	}

}
