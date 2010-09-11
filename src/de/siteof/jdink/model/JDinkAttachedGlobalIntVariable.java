package de.siteof.jdink.model;


public class JDinkAttachedGlobalIntVariable extends JDinkAttachedGlobalVariable<Integer> {

	public JDinkAttachedGlobalIntVariable(String variableName) {
		super(variableName, Integer.class);
	}

	public int getInt(JDinkContext context) {
		int result = 0;
		Integer value = this.getValue(context);
		if (value != null) {
			result = value.intValue();
		}
		return result;
	}

	public void setInt(JDinkContext context, int value) {
		this.setValue(context, Integer.valueOf(value));
	}
}