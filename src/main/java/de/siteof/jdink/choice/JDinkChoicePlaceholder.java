package de.siteof.jdink.choice;

import de.siteof.jdink.functions.JDinkExecutionContext;

public interface JDinkChoicePlaceholder {

	String getText(JDinkExecutionContext executionContext, int index);

}
