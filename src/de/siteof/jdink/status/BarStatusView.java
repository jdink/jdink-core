package de.siteof.jdink.status;

import java.util.ArrayList;
import java.util.List;

import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.model.IReadOnlyContextModel;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.view.JDinkImage;

public class BarStatusView implements StatusView {

	private static final long serialVersionUID = 1L;

	private final IReadOnlyContextModel<Integer> valueModel;
	private final IReadOnlyContextModel<Integer> maxValueModel;

	private final int valueSequenceNumber;
	private final int remainderSequenceNumber;

	private final int x;
	private final int y;
	private final int resolution;
	private final int maxWidthPerRow;
	private final int maxWidthTotal;
	private final int rowSpacing;

	private final List<JDinkSprite> sprites = new ArrayList<JDinkSprite>();

	private boolean visible;

	private int currentValue = -1;
	private int currentMaxValue = -1;

	public BarStatusView(
			IReadOnlyContextModel<Integer> valueModel,
			IReadOnlyContextModel<Integer> maxValueModel,
			int valueSequenceNumber,
			int remainderSequenceNumber,
			int x,
			int y,
			int resolution,
			int maxWidthPerRow,
			int maxWidthTotal,
			int rowSpacing) {
		this.valueModel = valueModel;
		this.maxValueModel = maxValueModel;
		this.valueSequenceNumber = valueSequenceNumber;
		this.remainderSequenceNumber = remainderSequenceNumber;
		this.x = x;
		this.y = y;
		this.resolution = resolution;
		this.maxWidthPerRow = maxWidthPerRow;
		this.maxWidthTotal = maxWidthTotal;
		this.rowSpacing = rowSpacing;
	}

	private final int getInt(JDinkContext context, IReadOnlyContextModel<Integer> model) {
		Integer value = model.getObject(context);
		return (value != null ? value.intValue() : 0);
	}

	@Override
	public void show(JDinkContext context) {
		if (!visible) {
			this.updateValue(context);
			this.doShow(context);
		}
	}

	@Override
	public void hide(JDinkContext context) {
		if (visible) {
			for (JDinkSprite sprite: this.sprites) {
				if (sprite != null) {
					sprite.setVisible(false);
					context.getController().notifyChanged(sprite, JDinkController.ALL_CHANGE);
				}
			}
			this.visible = false;
		}
	}

//	private final JDinkRectangle getBounds(JDinkContext context, JDinkSprite sprite) {
//		JDinkRectangle result = null;
//		JDinkShape bounds = sprite.getBounds();
//		if (bounds == null) {
//			JDinkImage image = context.getImage(sprite.getSequence(), sprite.getFrame());
//			if (image != null) {
//				result = JDinkRectangle.between(0, 0, image.getWidth(), image.getHeight());
//			}
//		} else {
//			result = bounds.getBounds();
//		}
//		if (result == null) {
//			result = new JDinkRectangle(0, 0, 0, 0);
//		}
//		return result;
//	}

	private final JDinkRectangle getBounds(JDinkContext context, int sequenceNumber, int frameNumber) {
		JDinkRectangle result = null;
		JDinkSequence sequence = context.getSequence(sequenceNumber, false);
		if (sequence != null) {
			JDinkSequenceFrame frame = sequence.getFrame(frameNumber, false);
			if (frame != null) {
				result = frame.getBounds();
				if (result == null) {
					JDinkImage image = context.getImage(sequence, frame);
					if (image != null) {
						result = new JDinkRectangle(0, 0, image.getWidth(), image.getHeight());
					}
				}
			}
		}
		if (result == null) {
			result = JDinkRectangle.EMPTY;
		}
		return result;
	}

	public void doShow(JDinkContext context) {
		// TODO this is displayed in a different way to the original game
		JDinkController controller = context.getController();
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		int spriteIndex = 0;
		int currentX = x;
		int currentY = y;
		JDinkSprite sprite;
//		JDinkRectangle bounds;

//		int resolution = this.resolution;
		int value = Math.max(0, this.currentValue);
		value = Math.min(this.maxWidthTotal, value);
		int maxValue = Math.max(value, this.currentMaxValue);
		maxValue = Math.min(this.maxWidthTotal, maxValue);

		JDinkRectangle valueLeftBounds = getBounds(context, valueSequenceNumber, 1);
		JDinkRectangle valueMiddleBounds = getBounds(context, valueSequenceNumber, 2);
		JDinkRectangle valueRightBounds = getBounds(context, valueSequenceNumber, 3);

		JDinkRectangle remainderLeftBounds = getBounds(context, remainderSequenceNumber, 1);
		JDinkRectangle remainderMiddleBounds = getBounds(context, remainderSequenceNumber, 2);
		JDinkRectangle remainderRightBounds = getBounds(context, remainderSequenceNumber, 3);

		int valueLeftWidth = Math.min(value, valueLeftBounds.getWidth());
//		if ((resolution > 0) && (valueLeftWidth > resolution)) {
//			valueLeftWidth = resolution;
//		}
		int valueRightWidth = 0;
		if (value == maxValue) {
			valueRightWidth = Math.min(value, valueRightBounds.getWidth());
			if (value < valueLeftWidth + valueRightWidth) {
				valueLeftWidth = Math.min(valueLeftWidth, Math.max(valueRightWidth - valueLeftWidth / 2, valueLeftWidth / 2));
				valueRightWidth = Math.min(value - valueLeftWidth, valueRightWidth);
			}
		}
		int valueMiddleWidth = value - valueLeftWidth - valueRightWidth;


		int remainderLeftWidth = Math.min(value, remainderLeftBounds.getWidth());
//		if ((resolution > 0) && (valueLeftWidth > resolution)) {
//			valueLeftWidth = resolution;
//		}
		int remainderRightWidth = Math.min(maxValue, remainderRightBounds.getWidth());
		if (maxValue < remainderLeftWidth + remainderRightWidth) {
			remainderLeftWidth = Math.min(remainderLeftWidth, Math.max(remainderRightWidth - remainderLeftWidth / 2, remainderLeftWidth / 2));
			remainderRightWidth = Math.min(maxValue - remainderLeftWidth, remainderRightWidth);
		}
		int remainderMiddleWidth = maxValue - remainderLeftWidth - remainderRightWidth;


		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, valueSequenceNumber, 1, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (valueLeftWidth <= 0) {
			sprite.setVisible(false);
		} else {
			sprite.setX(currentX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					0, 0,
					valueLeftWidth, valueLeftBounds.getHeight()));
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += valueLeftWidth;
		spriteIndex++;

		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, valueSequenceNumber, 2, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (valueMiddleWidth <= 0) {
			sprite.setVisible(false);
		} else {
			sprite.setX(currentX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					0, 0,
					valueMiddleWidth, valueMiddleBounds.getHeight()));
			sprite.setFill(true);
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += valueMiddleWidth;
		spriteIndex++;

		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, valueSequenceNumber, 3, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (valueRightWidth <= 0) {
			sprite.setVisible(false);
		} else {
			int offsetX = valueRightWidth - valueRightBounds.getWidth();
			sprite.setX(currentX + offsetX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					-offsetX, 0,
					valueRightWidth, valueRightBounds.getHeight()));
//			sprite.setOffset(JDinkPoint.getInstance(valueRightWidth - valueRightBounds.getWidth(), 0));
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += valueRightWidth;
		spriteIndex++;

		// remainder
		currentX = x;
		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, remainderSequenceNumber, 1, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (remainderLeftWidth <= value) {
			sprite.setVisible(false);
		} else {
			sprite.setX(currentX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					value, 0,
					remainderLeftWidth - value, remainderLeftBounds.getHeight()));
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += remainderLeftWidth;
		spriteIndex++;

		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, remainderSequenceNumber, 2, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (remainderLeftWidth + remainderMiddleWidth <= value) {
			sprite.setVisible(false);
		} else {
			int startX = Math.max(0, value - remainderLeftWidth);
			sprite.setX(currentX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					startX, 0,
					remainderMiddleWidth - startX, remainderMiddleBounds.getHeight()));
			sprite.setFill(true);
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += remainderMiddleWidth;
		spriteIndex++;

		if (spriteIndex >= sprites.size()) {
			sprite = spriteHelper.newSpriteAbsolute(currentX, currentY, remainderSequenceNumber, 3, false);
			sprites.add(sprite);
		} else {
			sprite = sprites.get(spriteIndex);
		}
		if (remainderLeftWidth + remainderMiddleWidth + remainderRightWidth <= value) {
			sprite.setVisible(false);
		} else {
			int offsetX = remainderRightWidth - remainderRightBounds.getWidth();
			int startX = Math.max(0, value - remainderLeftWidth - remainderMiddleWidth);
			sprite.setX(currentX + offsetX);
			sprite.setY(currentY);
			sprite.setClipShape(new JDinkRectangle(
					startX - offsetX, 0,
					remainderRightWidth - startX, remainderRightBounds.getHeight()));
//			sprite.setOffset(JDinkPoint.getInstance(remainderRightWidth - remainderRightBounds.getWidth(), 0));
			sprite.setVisible(true);
		}
		controller.notifyChanged(sprite, JDinkController.ALL_CHANGE);
		currentX += remainderRightWidth;
		spriteIndex++;

		visible = true;
	}

	public boolean updateValue(JDinkContext context) {
		int currentValue = getInt(context, valueModel);
		int currentMaxValue = getInt(context, maxValueModel);
		boolean result;
		if ((currentValue != this.currentValue) || (currentMaxValue != this.currentMaxValue)) {
			this.currentValue = currentValue;
			this.currentMaxValue = currentMaxValue;
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public boolean update(JDinkContext context) {
		boolean result = this.updateValue(context);
		if (result) {
			if (visible) {
				doShow(context);
			}
		}
		return result;
	}


}
