package de.siteof.jdink.status;

import de.siteof.jdink.model.JDinkAttachedGlobalIntVariable;
import de.siteof.jdink.model.JDinkContext;

public class NumericStatusView extends AbstractStatusView {

	private static final long serialVersionUID = 1L;

	private final JDinkAttachedGlobalIntVariable variable;

	private int currentValue = -1;

	public NumericStatusView(
			JDinkAttachedGlobalIntVariable variable,
			int x,
			int y,
			int sequenceNumber,
			int digitCount) {
		super(x, y, sequenceNumber, digitCount);
		this.variable = variable;
	}

	@Override
	protected boolean updateCurrentValue(JDinkContext context) {
		int value = variable.getInt(context);
		boolean result;
		if (value != this.currentValue) {
			this.currentValue = value;
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	protected void updateFrameNumbers(JDinkContext context) {
		updateFrameNumbers(this.currentValue);
	}

}
