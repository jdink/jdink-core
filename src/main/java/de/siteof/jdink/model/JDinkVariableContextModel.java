package de.siteof.jdink.model;


public class JDinkVariableContextModel<T> implements IReadOnlyContextModel<T> {

	private final JDinkAttachedGlobalVariable<T> variable;

	public JDinkVariableContextModel(JDinkAttachedGlobalVariable<T> variable) {
		this.variable = variable;
	}

	@Override
	public T getObject(JDinkContext context) {
		return variable.getValue(context);
	}

}
