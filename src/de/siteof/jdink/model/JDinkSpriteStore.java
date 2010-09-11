package de.siteof.jdink.model;

import java.util.Collections;
import java.util.List;

import de.siteof.jdink.util.DirectAccessArrayList;
import de.siteof.util.filter.IObjectFilter;

public class JDinkSpriteStore {

//	private static final Log log = LogFactory.getLog(JDinkSpriteStore.class);

	private static final JDinkSprite[] EMPTY_SPRITE_ARRAY = new JDinkSprite[0];

	private final DirectAccessArrayList<JDinkSprite> spriteList;
	private int allocatedSpriteCount;

	public JDinkSpriteStore() {
		spriteList = new DirectAccessArrayList<JDinkSprite>(new JDinkSprite[100]);
	}

	public final JDinkSprite[] getSpritesDirect() {
		return spriteList.getElementData();
	}

	public JDinkSprite[] getAllocatedSprites() {
		return getAllocatedSprites(null);
	}

	public JDinkSprite[] getAllocatedSprites(IObjectFilter<JDinkSprite> filter) {
		List<JDinkSprite> list = getAllocatedSpritesList(filter);
		JDinkSprite[] result = null;
		if (!list.isEmpty()) {
			if (list instanceof DirectAccessArrayList<?>) {
				result = ((DirectAccessArrayList<JDinkSprite>) list).getArray();
			} else {
				result = list.toArray(new JDinkSprite[list.size()]);
			}
		} else {
			result = EMPTY_SPRITE_ARRAY;
		}
		return result;
	}

	public List<JDinkSprite> getAllocatedSpritesList() {
		return getAllocatedSpritesList(null);
	}

	public List<JDinkSprite> getAllocatedSpritesList(IObjectFilter<JDinkSprite> filter) {
		List<JDinkSprite> result = null;
		int allocatedSpriteCount = this.allocatedSpriteCount;
		if (allocatedSpriteCount > 0) {
			DirectAccessArrayList<JDinkSprite> list = new DirectAccessArrayList<JDinkSprite>(
					new JDinkSprite[allocatedSpriteCount], 0);
			int foundAllocatedSpriteCount = 0;
			JDinkSprite[] sprites = this.getSpritesDirect();
			for (int i = 0; i < sprites.length; i++) {
				JDinkSprite sprite = sprites[i];
				if (sprite != null) {
					if ((filter == null) || (filter.matches(sprite))) {
						list.add(sprite);
					}
					foundAllocatedSpriteCount++;
					if (foundAllocatedSpriteCount == allocatedSpriteCount) {
						// there can't be any more non-null sprites
						break;
					}
				}
			}
			result = list;
		}
		if (result == null) {
			result = Collections.emptyList();
		}
		return result;
	}

	public JDinkSprite findSprite(IObjectFilter<JDinkSprite> filter) {
		JDinkSprite result = null;
		int allocatedSpriteCount = this.allocatedSpriteCount;
		if (allocatedSpriteCount > 0) {
			int foundAllocatedSpriteCount = 0;
			JDinkSprite[] sprites = this.getSpritesDirect();
			for (int i = 0; i < sprites.length; i++) {
				JDinkSprite sprite = sprites[i];
				if (sprite != null) {
					if ((filter == null) || (filter.matches(sprite))) {
						result = sprite;
						break;
					}
					foundAllocatedSpriteCount++;
					if (foundAllocatedSpriteCount == allocatedSpriteCount) {
						// there can't be any more non-null sprites
						break;
					}
				}
			}
		}
		return result;
	}

	public int getNextFreeSpriteNumber() {
		JDinkSprite[] sprites = this.getSpritesDirect();
		int result = 0;
		for (int index = 1; index < sprites.length; index++) {
			if (sprites[index] == null) {
				result = index;
				break;
			}
		}
		if (result == 0) {
			result = sprites.length;
		}
		return result;
	}

	public JDinkSprite getSprite(int spriteNumber) {
		JDinkSprite[] sprites = this.getSpritesDirect();
		JDinkSprite sprite;
		if ((sprites != null) && (spriteNumber < sprites.length)) {
			sprite = sprites[spriteNumber];
		} else {
			sprite = null;
		}
		return sprite;
	}

	public void setSprite(int spriteNumber, JDinkSprite sprite) {
		if (sprite == null) {
			throw new IllegalArgumentException("sprite must not be null");
		}
		if (spriteNumber >= this.spriteList.size()) {
			spriteList.setSize(spriteNumber + 10);
		}
		JDinkSprite previousSprite = spriteList.set(spriteNumber, sprite);
		if (previousSprite == null) {
			allocatedSpriteCount++;
		}
	}

	public boolean releaseSprite(int spriteNumber) {
		boolean result = false;
		if (spriteNumber < this.spriteList.size()) {
			JDinkSprite previousSprite = spriteList.set(spriteNumber, null);
			if (previousSprite != null) {
				allocatedSpriteCount--;
				result = true;
			}
		}
		return result;
	}

	public boolean releaseSpritesFrom(int startSpriteNumber) {
		boolean result = false;
		JDinkSprite[] sprites = this.getSpritesDirect();
		for (int i = startSpriteNumber; i < sprites.length; i++) {
			if (sprites[i] != null) {
				sprites[i] = null;
				allocatedSpriteCount--;
				result = true;
			}
		}
		return result;
	}

//	public JDinkSprite getSprite(int spriteNumber, boolean create) {
//		if (sprites == null) {
//			sprites = new JDinkSprite[Math.min(10, spriteNumber)];
//		}
//		if (spriteNumber >= sprites.length) {
//			if (create) {
//				JDinkSprite[] a = new JDinkSprite[spriteNumber + 10];
//				System.arraycopy(sprites, 0, a, 0, sprites.length);
//				sprites = a;
//				JDinkSprite sprite = new JDinkSprite();
//				sprite.setSpriteNumber(spriteNumber);
//				sprites[spriteNumber] = sprite;
//				this.setChanged(true);
//				return sprite;
//			} else {
//				return null;
//			}
//		} else {
//			JDinkSprite sprite = sprites[spriteNumber];
//			if ((sprite == null) && (create)) {
//				sprite = new JDinkSprite();
//				sprite.setSpriteNumber(spriteNumber);
//				sprites[spriteNumber] = sprite;
//				this.setChanged(true);
//			}
//			return sprite;
//		}
//	}

//	public JDinkSprite getSpriteByEditorSpriteNumber(int editorSpriteNumber) {
//		JDinkSprite result = null;
//		int allocatedSpriteCount = this.allocatedSpriteCount;
//		if (allocatedSpriteCount > 0) {
//			JDinkSprite[] sprites = this.getSpritesDirect();
//			if (sprites != null) {
//				for (int i = 0; i < sprites.length; i++) {
//					JDinkSprite sprite = sprites[i];
//					if ((sprite != null) && (sprite.getEditorSpriteNumber() == editorSpriteNumber)) {
//						result = sprite;
//						break;
//					}
//				}
//			}
//		}
//		return result;
//	}

//	public JDinkSprite[] getVisibleSprites() {
//		JDinkSprite[] sprites = this.getSpritesDirect();
//		if (sprites.length == 0) {
//			return EMPTY_SPRITE_ARRAY;
//		}
//		List<JDinkSprite> visibleSprites = new ArrayList<JDinkSprite>(allocatedSpriteCount);
//		for (int i = 0; i < sprites.length; i++) {
//			JDinkSprite sprite = sprites[i];
//			if ((sprite != null) && (sprite.isVisible())) {
//				visibleSprites.add(sprite);
//			}
//		}
//		return (JDinkSprite[]) visibleSprites.toArray(EMPTY_SPRITE_ARRAY);
//	}

}
