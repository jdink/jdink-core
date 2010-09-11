package de.siteof.jdink.model;

public interface IReadOnlyContextModel<T> {
	
	T getObject(JDinkContext context);

}
