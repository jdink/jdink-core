package de.siteof.jdink.model;


import java.util.ArrayList;
import java.util.Collection;

import de.siteof.jdink.geom.JDinkShape;
import de.siteof.jdink.loader.JDinkLazyLoader;
import de.siteof.jdink.util.ArrayUtil;

/**
 * <p>A sequence of frames (or a single frame)</p>
 * <p>A sequence may be used as an animation or to group a set of pictures (e.g. numbers)</p>
 */
public class JDinkSequence {

	private JDinkSequenceFrame[] frames;

	private int backgroundColor;
	private int offsetX;
	private int offsetY;
	private int frameCount;
	private boolean loading;
	private boolean loaded;
	private JDinkLazyLoader lazyLoader;
	private JDinkShape collisionShape;
	private int defaultFrameNumber;
	private boolean animation;
	private boolean leftAligned;

	public void requireLoaded() {
		if ((!loaded) && (!loading) && (lazyLoader != null)) {
			loading = true;
			try {
				loaded = lazyLoader.load(this);
			} finally {
				loading = false;
			}
		}
	}

	public void clear() {
		frames	= null;
		this.frameCount = 0;
		loading = false;
		loaded  = false;
	}

	public int getFrameCount() {
		requireLoaded();
		return frameCount;
	}
	
	public final int getFirstFrameNumber() {
		return 1;
	}
	
	public final int getLastFrameNumber() {
		return getFrameCount();
	}
	
	public final int getNextFrameNumber(int frameNumber) {
		return frameNumber + 1;
	}
	
	public final int getPreviousFrameNumber(int frameNumber) {
		return frameNumber - 1;
	}
	
	private final int getFrameIndex(int frameNumber) {
		return Math.max(0, frameNumber - 1);
	}

	public JDinkSequenceFrame getFrame(int frameNumber, boolean create) {
		requireLoaded();
		int frameIndex = getFrameIndex(frameNumber);
		JDinkSequenceFrame frame;
		if ((frames != null) && (frameIndex < frames.length)) {
			frame = frames[frameIndex];
		} else {
			frame = null;
		}
		if ((frame == null) && (create)) {
			if (frames == null) {
				frames = new JDinkSequenceFrame[Math.max(10, frameIndex + 1)];
			} else if (frameIndex >= frames.length) {
				frames = ArrayUtil.copyOf(frames, frameIndex + 10);
			}
			frame = new JDinkSequenceFrame();
			frames[frameIndex] = frame;
			if (frameIndex >= frameCount) {
				frameCount = frameIndex + 1;
			}
		}
		return frame;
//		if ((create) && (frameIndex > frameCount)) {
//			frameCount = frameIndex + 1;
//		}
//		if (frameIndex >= frames.length) {
//			if (create) {
//				JDinkSequenceFrame[] a = new JDinkSequenceFrame[frameIndex + 10];
//				System.arraycopy(frames, 0, a, 0, frames.length);
//				frames = a;
//				JDinkSequenceFrame frame = new JDinkSequenceFrame();
//				frames[frameIndex] = frame;
//				return frame;
//			} else {
//				return null;
//			}
//		} else {
//			JDinkSequenceFrame frame = frames[frameIndex];
//			if ((frame == null) && (create)) {
//				frame = new JDinkSequenceFrame();
//				frames[frameNumber] = frame;
//			}
//			return frame;
//		}
	}

	public JDinkSequenceFrame[] getFrames() {
		requireLoaded();
		JDinkSequenceFrame[] result;
		if ((this.frames != null) && (this.frames.length > 0)) {
			Collection<JDinkSequenceFrame> frames = new ArrayList<JDinkSequenceFrame>(this.frames.length);
			for (int i = 0; i < this.frames.length; i++) {
				if (this.frames[i] != null) {
					frames.add(this.frames[i]);
				}
			}
			result = (JDinkSequenceFrame[]) frames.toArray(new JDinkSequenceFrame[frames.size()]);
		} else {
			result = new JDinkSequenceFrame[0];
		}
		return result;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getOffsetX() {
		return offsetX;
	}
	
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public JDinkLazyLoader getLazyLoader() {
		return lazyLoader;
	}
	
	public void setLazyLoader(JDinkLazyLoader lazyLoader) {
		this.lazyLoader = lazyLoader;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * @return the collisionShape
	 */
	public JDinkShape getCollisionShape() {
		return collisionShape;
	}

	/**
	 * @param collisionShape the collisionShape to set
	 */
	public void setCollisionShape(JDinkShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public int getDefaultFrameNumber() {
		return defaultFrameNumber;
	}

	public void setDefaultFrameNumber(int defaultFrameNUmber) {
		this.defaultFrameNumber = defaultFrameNUmber;
	}

	public boolean isAnimation() {
		return animation;
	}

	public void setAnimation(boolean animation) {
		this.animation = animation;
	}

	public boolean isLeftAligned() {
		return leftAligned;
	}

	public void setLeftAligned(boolean leftAligned) {
		this.leftAligned = leftAligned;
	}
}
