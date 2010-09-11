package de.siteof.jdink.format.mapinfo;


import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.util.DataUtil;

/**
 * <p>Loads {@link JDinkMapInfo}.</p>
 */
public class JDinkMapInfoLoader extends AbstractLoader {

	private JDinkMapInfo mapInfo;

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		in = getBufferedInputStream(in);
		DataInput dataInput = DataUtil.getDataInput(in);
		JDinkMapInfo mapInfo = new JDinkMapInfo();
		mapInfo.setName(readString(dataInput, 20));
		mapInfo.setLoc(readIntArray(dataInput, 769));
		mapInfo.setMusic(readIntArray(dataInput, 769));
		mapInfo.setIndoor(readIntArray(dataInput, 769));
		mapInfo.setV(readIntArray(dataInput, 40));
		mapInfo.setS(readString(dataInput, 80));
		mapInfo.setBuffer(readString(dataInput, 2000));
		this.mapInfo = mapInfo;
	}

	protected int[] readIntArray(DataInput in, int length) throws IOException {
		return DataUtil.readIntArrayLittleEndian(in, length);
	}

	protected int readInt(DataInput in) throws IOException {
		return DataUtil.readIntLittleEndian(in);
	}

	protected String readString(DataInput in, int length) throws IOException {
		return DataUtil.readString(in, length);
	}

	public JDinkMapInfo getMapInfo() {
		return mapInfo;
	}
	public void setMapInfo(JDinkMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}
}
