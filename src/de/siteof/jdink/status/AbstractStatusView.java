package de.siteof.jdink.status;

import java.io.Serializable;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkSpriteHelper;
import de.siteof.jdink.util.ArrayUtil;
import de.siteof.jdink.view.JDinkImage;

public abstract class AbstractStatusView implements StatusView, Serializable {

	private static final long serialVersionUID = 1L;
	
	private int x;
	private final int y;
	private int sequenceNumber;
	private JDinkSprite[] sprites;
	private int[] frameNumbers;
	private boolean visible;
	
	public AbstractStatusView(
			int x,
			int y,
			int sequenceNumber,
			int digitCount) {
		this.x = x;
		this.y = y;
		this.sequenceNumber = sequenceNumber;
		this.sprites = new JDinkSprite[digitCount];
		this.frameNumbers = new int[digitCount];
	}
	
	public void setCharacterLength(JDinkContext context, int length) {
		if (length != sprites.length) {
			for (int i = 0; i < this.sprites.length - length; i++) {
				if (this.sprites[i] != null) {
					context.getController().releaseSprite(this.sprites[i].getSpriteNumber());
				}
			}
			this.sprites = ArrayUtil.copyOf(this.sprites, length);
			this.frameNumbers = ArrayUtil.copyOf(this.frameNumbers, length);
			if (this.visible) {
				this.updateFrameNumbers(context);
				doShow(context);
			}
		}
	}
	
	public int getCharacterLength() {
		return this.sprites.length;
	}

	protected void setFrameNumber(int index, int frameNumber) {
		frameNumbers[index] = frameNumber;
	}

	protected void updateFrameNumbers(int value) {
		for (int i = 0; i < frameNumbers.length; i++) {
			int digit = value % 10;
			int frameNumber = (digit == 0 ? 10 : digit);
			frameNumbers[frameNumbers.length - 1 - i] = frameNumber;
			value = value / 10;
		}
	}
	
	protected void updateFrameNumbers(String value) {
		int offset = value.length() - frameNumbers.length;
		for (int i = 0; i < frameNumbers.length; i++) {
			int index = i + offset;
			char ch;
			if ((index >= 0) && (index < value.length())) {
				ch = value.charAt(index);
			} else {
				ch = '0';
			}
			int frameNumber;
			switch (ch) {
			case '0':
				frameNumber = 10;
				break;
			case '1':
				frameNumber = 1;
				break;
			case '2':
				frameNumber = 2;
				break;
			case '3':
				frameNumber = 3;
				break;
			case '4':
				frameNumber = 4;
				break;
			case '5':
				frameNumber = 5;
				break;
			case '6':
				frameNumber = 6;
				break;
			case '7':
				frameNumber = 7;
				break;
			case '8':
				frameNumber = 8;
				break;
			case '9':
				frameNumber = 9;
				break;
			case '/':
				frameNumber = 11;
				break;
			default:
				frameNumber = 10;
			}
			frameNumbers[i] = frameNumber;
		}
	}
	
	protected abstract boolean updateCurrentValue(JDinkContext context);

	protected abstract void updateFrameNumbers(JDinkContext context);
	
	private boolean updateCurrentValueAndFrameNumbers(JDinkContext context) {
		boolean result = this.updateCurrentValue(context);
		if (result) {
			this.updateFrameNumbers(context);
		}
		return result;
	}

	@Override
	public final boolean update(JDinkContext context) {
		boolean result = updateCurrentValueAndFrameNumbers(context);
		if (result) {
			for (int i = 0; i < sprites.length; i++) {
				JDinkSprite sprite = sprites[i];
				if (sprite != null) {
					sprite.setFrameNumber(frameNumbers[i]);
				}
			}
		}
		return result;
	}

	@Override
	public final void show(JDinkContext context) {
		if (!visible) {
			update(context);
			doShow(context);
		}
	}

	private final void doShow(JDinkContext context) {
		JDinkSpriteHelper spriteHelper = context.getSpriteHelper();
		int currentX = this.x;
		for (int i = 0; i < sprites.length; i++) {
			JDinkSprite sprite = sprites[i];
			if (sprite == null) {
				sprite = spriteHelper.newSpriteAbsolute(
						currentX, y, this.sequenceNumber, frameNumbers[i], true);
				sprites[i] = sprite;
			} else {
				sprite.setX(currentX);
				sprite.setY(y);
				spriteHelper.setSpriteSequence(sprite, this.sequenceNumber);
			}
			sprite.setVisible(sprite.getSequence() != null);
			JDinkShape bounds = sprite.getBounds();
			if (bounds == null) {
				JDinkImage image = context.getImage(sprite.getSequence(), sprite.getFrame());
				if (image != null) {
					currentX += image.getWidth();
				}
			} else {
				currentX += bounds.getBounds().getWidth();
			}
		}
		this.visible = true;
	}

	public JDinkSprite[] getSprites() {
		return sprites;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

}
