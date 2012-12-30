package de.siteof.jdink.functions;

import java.io.Serializable;

/**
 * <p>
 * Generic script function
 * </p>
 * <p>
 * This is the base interface of anything that is callable.
 * </p>
 */
public interface JDinkFunction extends Serializable {

	Object invoke(JDinkExecutionContext executionContext) throws Throwable;

}
