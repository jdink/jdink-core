package de.siteof.jdink.status;

import de.siteof.jdink.model.IReadOnlyContextModel;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkItem;

public class ItemStatusView extends AbstractStatusView {

	private static final long serialVersionUID = 1L;
	
	private final IReadOnlyContextModel<JDinkItem> itemModel;
	
	private int frameNumber = -1;
	
	public ItemStatusView(
			IReadOnlyContextModel<JDinkItem> itemModel,
			int x,
			int y) {
		super(x, y, -1, 1);
		this.itemModel = itemModel;
	}

	@Override
	protected boolean updateCurrentValue(JDinkContext context) {
		JDinkItem item = itemModel.getObject(context);
		int sequenceNumber = (item != null ? item.getSequenceNumber() : -1);
		int frameNumber = (item != null ? item.getFrameNumber() : -1);
		boolean result;
		if ((sequenceNumber != this.getSequenceNumber()) ||
				(frameNumber != this.frameNumber)) {
			this.setSequenceNumber(sequenceNumber);
			this.frameNumber = frameNumber;
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	@Override
	protected void updateFrameNumbers(JDinkContext context) {
		setFrameNumber(0, this.frameNumber);
	}
	
}
