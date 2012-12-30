package de.siteof.jdink.status;

import de.siteof.jdink.model.IReadOnlyContextModel;
import de.siteof.jdink.model.JDinkContext;

public class TextStatusView extends AbstractStatusView {

	private static final long serialVersionUID = 1L;
	
	private final IReadOnlyContextModel<String> textModel;
	
	private String currentValue = "";
	
	public TextStatusView(
			IReadOnlyContextModel<String> textModel,
			int x,
			int y,
			int sequenceNumber,
			int digitCount) {
		super(x, y, sequenceNumber, digitCount);
		this.textModel = textModel;
	}

	@Override
	protected boolean updateCurrentValue(JDinkContext context) {
		String value = textModel.getObject(context);
		if (value == null) {
			value = "";
		}
		boolean result;
		if (!value.equals(this.currentValue)) {
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
