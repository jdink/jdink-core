package de.siteof.jdink.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.siteof.jdink.util.IntegerHashMap;
import de.siteof.jdink.util.IntegerMap;

/**
 * <p>Represents the persistent state on a single map.</p>
 * <p>(mydata)</p>
 */
public class JDinkMapState {

	public static final int DEFAULT_STATE = 0;
	public static final int KILL_PERMANENTLY_STATE = 1;
	public static final int FUTURE_DRAW_NORMAL_WITHOUT_HARDNESS_STATE = 2;
	public static final int FUTURE_DRAW_BACKGROUND_WITHOUT_HARDNESS_STATE = 3;
	public static final int FUTURE_DRAW_NORMAL_WITH_HARDNESS_STATE = 4;
	public static final int FUTURE_DRAW_BACKGROUND_WITH_HARDNESS_STATE = 5;
	public static final int RETURN_IN_5_MINUTES_STATE = 6;
	public static final int RETURN_IN_3_MINUTES_STATE = 7;
	public static final int RETURN_IN_1_MINUTE_STATE = 8;
	/**
	 * 0 – Do nothing, draw the sprite as normal.
	 * 1 – Kill sprite permanently, make sure it doesn’t return.
2 – In the future, draw the sprite’s editor_seq / editor_frame as a normal sprite without hardness. (This can be used if you 
want a body to be there if you return to the screen)
3 ­ In the future, draw the sprite’s editor_seq / editor_frame as a background sprite without hardness.
4 ­ In the future, draw the sprite’s editor_seq / editor_frame as a normal sprite with hardness
5 ­ In the future, draw the sprite’s editor_seq / editor_frame as a background sprite with hardness.
6 – Kill the sprite, but let it come back after 5 minutes.
7 – Kill the sprite, but let it come back after 3 minutes.
8 – Kill the sprite, but let it come back after 1 minute.
	 */

	public static final JDinkMapState EMPTY_MAP_STATE = new JDinkMapState() {
		@Override
		public void setEditorFrameNumber(int editorSpriteNumber, int frameNumber) {
			throw new UnsupportedOperationException("read-only map state");
		}

		@Override
		public void setEditorSequenceNumber(int editorSpriteNumber,
				int sequenceNumber) {
			throw new UnsupportedOperationException("read-only map state");
		}

		@Override
		public void setEditorSpriteState(int editorSpriteNumber, int type) {
			throw new UnsupportedOperationException("read-only map state");
		}

		@Override
		public void setLastTime(long lastTime) {
			// TODO Auto-generated method stub
			super.setLastTime(lastTime);
		}

	};


	private long lastTime;
	private byte[] editorSpriteState;
	private IntegerMap<Integer> editorSequenceNumberMap;
	private IntegerMap<Integer> editorFrameNumberMap;

	public JDinkMapState() {
		this.lastTime = System.currentTimeMillis();
	}

	public Collection<Integer> getEditorSpriteStateKeys() {
		Collection<Integer> result;
		if (editorSpriteState != null) {
			result = new ArrayList<Integer>(editorSpriteState.length);
			for (int i = 0; i < editorSpriteState.length; i++) {
				if (editorSpriteState[i] != 0) {
					result.add(Integer.valueOf(i));
				}
			}
		} else {
			result = Collections.emptyList();
		}
		return result;
	}

	public int getEditorSpriteState(int editorSpriteNumber) {
		int result;
		if ((editorSpriteState != null) && (editorSpriteNumber < editorSpriteState.length)) {
			result = editorSpriteState[editorSpriteNumber];
		} else {
			result = 0;
		}
		return result;
	}

	public void setEditorSpriteState(int editorSpriteNumber, int type) {
		if (editorSpriteState == null) {
			editorSpriteState = new byte[100];
		}
		if (editorSpriteNumber < editorSpriteState.length) {
			editorSpriteState[editorSpriteNumber] = (byte) type;
		}
	}

	public Collection<Integer> getEditorSequenceNumberKeys() {
		Collection<Integer> keys;
		if (editorSequenceNumberMap != null) {
			keys = editorSequenceNumberMap.keySet();
		} else {
			keys = Collections.emptyList();
		}
		return keys;
	}

	public int getEditorSequenceNumber(int editorSpriteNumber) {
		int result = 0;
		if (editorSequenceNumberMap != null) {
			Integer value = editorSequenceNumberMap.get(editorSpriteNumber);
			if (value != null) {
				result = value.intValue();
			}
		}
		return result;
	}

	public void setEditorSequenceNumber(int editorSpriteNumber, int sequenceNumber) {
		if (sequenceNumber != 0) {
			if (editorSequenceNumberMap == null) {
				editorSequenceNumberMap = new IntegerHashMap<Integer>();
			}
			editorSequenceNumberMap.put(editorSpriteNumber, Integer.valueOf(sequenceNumber));
		} else {
			editorSequenceNumberMap.remove(editorSpriteNumber);
		}
	}

	public Collection<Integer> getEditorFrameNumberKeys() {
		Collection<Integer> keys;
		if (editorFrameNumberMap != null) {
			keys = editorFrameNumberMap.keySet();
		} else {
			keys = Collections.emptyList();
		}
		return keys;
	}

	public int getEditorFrameNumber(int editorSpriteNumber) {
		int result = 0;
		if (editorFrameNumberMap != null) {
			Integer value = editorFrameNumberMap.get(editorSpriteNumber);
			if (value != null) {
				result = value.intValue();
			}
		}
		return result;
	}

	public void setEditorFrameNumber(int editorSpriteNumber, int frameNumber) {
		if (frameNumber != 0) {
			if (editorFrameNumberMap == null) {
				editorFrameNumberMap = new IntegerHashMap<Integer>();
			}
			editorFrameNumberMap.put(editorSpriteNumber, Integer.valueOf(frameNumber));
		} else {
			editorFrameNumberMap.remove(editorSpriteNumber);
		}
	}

	private static final boolean isEmpty(Map<?, ?> m) {
		return (m == null) || (m.isEmpty());
	}

	private static final boolean isEmpty(byte[] a) {
		boolean result = true;
		if (a != null) {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != 0) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	public boolean isEmpty() {
		// ignore lastTime as it doesn't have any effect if all of the other values are blank
		return (isEmpty(editorSequenceNumberMap)) &&
				(isEmpty(editorFrameNumberMap)) &&
				(isEmpty(editorSpriteState));
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

}
