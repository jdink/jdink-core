package de.siteof.jdink.format.hardness;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.io.PositionTrackerFilterInputStream;

public class JDinkTileHardnessLoader extends AbstractLoader {

	private static final Log log	= LogFactory.getLog(JDinkTileHardnessLoader.class);

	private JDinkTileHardnessEntry[] hardnessEntries;
	private int[] hardnessIndex;

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		// 51x51 + 1 + 4 = 2606
		// * 800 = 2084800
		// + 4 * 8000 = 2116800
		//
		// 2107392
		// - 32000 = 2075392
		// 51->52x51 + 1(4) + 4 = 2660
		// * 800 = 2128000
		//
		// 2118400
		// - 32000 = 2086400
		// / 800 = 2608
		// 51x51 + 1(2) + 4 = 2608
		in = getBufferedInputStream(in);
		in = new PositionTrackerFilterInputStream(in);
		DataInputStream dis = new DataInputStream(in);
		JDinkTileHardnessEntry[] hardnessEntries = new JDinkTileHardnessEntry[800];
		for (int i = 0; i < hardnessEntries.length; i++) {
			JDinkTileHardnessEntry hardnessEntry = loadHardnessEntry(dis);
			hardnessEntries[i] = hardnessEntry;
		}
		if (log.isDebugEnabled()) {
			log.debug("reading index from position: " + ((PositionTrackerFilterInputStream) in).getPosition());
		}
//		int[] hardnessIndex = DataUtil.readIntArrayLittleEndian(dis, 8000);
		int[] hardnessIndex = new int[8000];
		for (int i = 0; i < hardnessIndex.length; i++) {
			try {
				hardnessIndex[i] = DataUtil.readIntLittleEndian(dis);
			} catch (Exception e) {
				log.warn("hardness index incomplete", e);
				break;
			}
		}
		this.hardnessEntries = hardnessEntries;
		this.hardnessIndex = hardnessIndex;
	}

	protected JDinkTileHardnessEntry loadHardnessEntry(DataInput in) throws IOException {
		JDinkTileHardnessEntry hardnessEntry = new JDinkTileHardnessEntry();
		byte[][] hardnessXY = new byte[51][51];
		for (int x = 0; x < hardnessXY.length; x++) {
			in.readFully(hardnessXY[x]);
		}
		byte[][] hardnessYX = new byte[51][51];
		for (int y = 0; y < hardnessYX.length; y++) {
			for (int x = 0; x < hardnessYX[y].length; x++) {
				hardnessYX[y][x] = hardnessXY[x][y];
			}
		}
		hardnessEntry.setHardness(hardnessYX);
		hardnessEntry.setUsed(in.readBoolean());
		in.skipBytes(2); // alignment!
		hardnessEntry.setHold(in.readInt());
		return hardnessEntry;
	}

	/**
	 * @return the hardnessEntries
	 */
	public JDinkTileHardnessEntry[] getHardnessEntries() {
		return hardnessEntries;
	}

	/**
	 * @return the hardnessIndex
	 */
	public int[] getHardnessIndex() {
		return hardnessIndex;
	}

}
