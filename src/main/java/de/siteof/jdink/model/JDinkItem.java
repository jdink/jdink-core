package de.siteof.jdink.model;

import de.siteof.jdink.script.JDinkScope;
import de.siteof.jdink.script.JDinkScriptInstance;

/**
 * <p>Represents a single item</p>
 * <p>An item can for example be a regular item that can be armed or a magic item</p>
 */
public class JDinkItem {
	
	private final int itemNumber;
	
	private int sequenceNumber;
	private JDinkSequence sequence;
	private int frameNumber;
	private JDinkScriptInstance scriptInstance;
	private JDinkScope scope;
	
	public JDinkItem(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public String toString() {
		return "JDinkItem [frameNumber=" + frameNumber + ", itemNumber="
				+ itemNumber + ", scope=" + scope + ", scriptInstance="
				+ scriptInstance + ", sequence=" + sequence
				+ ", sequenceNumber=" + sequenceNumber + "]";
	}

	public JDinkScope requestScope(JDinkContext context) {
		final JDinkItem item = this;
		JDinkScope scope = item.getScope();
		if (scope == null) {
			scope = new JDinkScope(context.getGlobalScope());
			item.setScope(scope);
//			JDinkVariable currentSpriteVariable = scope.getVariable(JDinkScriptConstants.CURRENT_SPRITE_VARNAME);
//			if (currentSpriteVariable == null) {
//				currentSpriteVariable = new JDinkVariable();
//				currentSpriteVariable.setType(JDinkIntegerType.getIntegerTypeInstance());
//				scope.addVariable(JDinkScriptConstants.CURRENT_SPRITE_VARNAME, currentSpriteVariable);
//			}
//			currentSpriteVariable.setValue(new Integer(item.getSpriteNumber()));
		}
		return scope;
	}
	
	public void call(JDinkContext context, String functionName) throws Throwable {
		if (scriptInstance != null) {
			scriptInstance.callFunction(context,
					functionName, new Object[0]);
		}
	}
	
	public void disarm(JDinkContext context) throws Throwable {
		call(context, "disarm");
	}
	
	public void arm(JDinkContext context) throws Throwable {
		call(context, "arm");
	}
	
	public void use(JDinkContext context) throws Throwable {
		call(context, "use");
	}

	public int getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}
	public JDinkScope getScope() {
		return scope;
	}
	public void setScope(JDinkScope scope) {
		this.scope = scope;
	}
	public JDinkScriptInstance getScriptInstance() {
		return scriptInstance;
	}
	public void setScriptInstance(JDinkScriptInstance scriptInstance) {
		this.scriptInstance = scriptInstance;
	}
	public JDinkSequence getSequence() {
		return sequence;
	}
	public void setSequence(JDinkSequence sequence) {
		this.sequence = sequence;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getItemNumber() {
		return itemNumber;
	}
}
