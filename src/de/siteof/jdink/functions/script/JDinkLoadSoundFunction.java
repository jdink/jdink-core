package de.siteof.jdink.functions.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.model.JDinkSound;

/**
 * <p>Function: void load_sound(int name, int soundNumber)</p>
 */
public class JDinkLoadSoundFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log	= LogFactory.getLog(JDinkLoadSoundFunction.class);

	@Override
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		String name = executionContext.getArgumentAsString(0, null);
		Integer soundNumber = toInteger(executionContext.getArgument(1), null);
		assertNotNull(name, "load sound: name missing");
		assertNotNull(soundNumber, "load sound: soundNumber missing");
		Resource resource = executionContext.getContext().getFileManager().getResource("sound/" + name);
		JDinkSound sound = new JDinkSound();
		sound.setResource(resource);
		executionContext.getContext().addSound(soundNumber.intValue(), sound);
		if (log.isDebugEnabled()) {
			log.debug("load sound: " + name);
		}
		return null;
	}

}
