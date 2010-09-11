/*
 * Created on 29.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.format.map;


import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.StringUtil;
import de.siteof.jdink.util.io.PositionTrackerFilterInputStream;

/**
 * @author user
 *
 * sizeof tile: 4x4 + 1 + 1 + 15x4 = 98
 * sizeof sprite_placement: 6x4 + 1/4? + 3x4 + 13x1 + 13x1 + 13x1 + 13x1 + 7x4 + 4 + 4x4(rect) + 5x4 + 10x4 + 5x4 =
 * sizeof small_map: 20x1 + 97x98(tile) + 40x4 + 80x1 + 101x?(sprite) + 13x1 + 13x1 + 13x1 + 1000
 *
 * total size: 1063534
 * sizeof small_map: 31280
 * sizeof tile: 80
 * sizeof sprite_placement: 220
 *
 * -sizeof small_map: 31782
 * -sizeof tile: 78
 * -sizeof sprite_placement: 217
 */
public class JDinkMapLoader extends AbstractLoader {

	private int mapEntrySize;

	private static final Log log = LogFactory.getLog(JDinkMapLoader.class);

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		in = getBufferedInputStream(in);
		PositionTrackerFilterInputStream in2 = new PositionTrackerFilterInputStream(in);
		DataInputStream dis = new DataInputStream(in2);
		JDinkMapEntry mapEntry = loadMapEntry(dis);
		if (log.isDebugEnabled()) {
			log.debug("map entry size: " + in2.getPosition());
		}
		in2.setPosition(0);
		loadMapTile(dis);
		if (log.isDebugEnabled()) {
			log.debug("tile size: " + in2.getPosition());
		}
		in2.setPosition(0);
		loadMapSpritePlacement(dis);
		if (log.isDebugEnabled()) {
			log.debug("sprite placement size: " + in2.getPosition());
			log.debug("mapEntry: " + StringUtil.toString(mapEntry, true));
		}
		mapEntrySize = 31280;
	}

	public JDinkMapEntry getMapEntry(int mapEntryNumber) throws IOException {
		if (mapEntryNumber <= 0) {
			return null;
		}
		InputStream in = getNewInputStream();
		try {
			DataInputStream dis;
			if (in instanceof DataInputStream) {
				dis = (DataInputStream) in;
			} else {
				dis = new DataInputStream(in);
			}
			long ofs = (mapEntryNumber-1) * mapEntrySize;
			in.skip(ofs);
			return loadMapEntry(dis);
		} finally {
			in.close();
		}
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

	protected JDinkMapTile[] readTileArray(DataInput in, int length) throws IOException {
		JDinkMapTile[] a = new JDinkMapTile[length];
		for (int i = 0; i < length; i++) {
			a[i] = loadMapTile(in);
		}
		return a;
	}

	protected JDinkMapSpritePlacement[] readSpritePlacementArray(DataInput in, int length) throws IOException {
		JDinkMapSpritePlacement[] a = new JDinkMapSpritePlacement[length];
		for (int i = 0; i < length; i++) {
			a[i] = loadMapSpritePlacement(in);
			if (log.isDebugEnabled()) {
				log.debug("[readSpritePlacementArray] spritePlacement[" + i + "]=" + a[i]);
			}
		}
		return a;
	}

	protected JDinkMapEntry loadMapEntry(DataInput in) throws IOException {
		JDinkMapEntry mapEntry = new JDinkMapEntry();
		mapEntry.setName(readString(in, 20));
		mapEntry.setTiles(readTileArray(in, 97));
		mapEntry.setV(readIntArray(in, 40));
		mapEntry.setS(readString(in, 80));
		mapEntry.setSpritePlacements(readSpritePlacementArray(in, 101));
		mapEntry.setScriptName(readString(in, 13));
		mapEntry.setRandom(readString(in, 13));
		mapEntry.setLoad(readString(in, 13));
		mapEntry.setBuffer(readString(in, 1000));
		in.skipBytes(1); // alignment!
		return mapEntry;
	}

	protected JDinkMapTile loadMapTile(DataInput in) throws IOException {
		JDinkMapTile mapTile = new JDinkMapTile();
		mapTile.setNum(readInt(in));
		mapTile.setProperty(readInt(in));
		mapTile.setAlthard(readInt(in));
		mapTile.setMore2(readInt(in));
		mapTile.setMore3(in.readByte());
		mapTile.setMore4(in.readByte());
		in.skipBytes(2); // alignment!
		mapTile.setBuffer(readIntArray(in, 15));
		return mapTile;
	}

	protected JDinkMapSpritePlacement loadMapSpritePlacement(DataInput in) throws IOException {
		JDinkMapSpritePlacement spritePlacement = new JDinkMapSpritePlacement();
		spritePlacement.setX(readInt(in));
		spritePlacement.setY(readInt(in));
		spritePlacement.setSequenceNumber(readInt(in));
		spritePlacement.setFrameNumber(readInt(in));
		spritePlacement.setType(readInt(in));
		spritePlacement.setSize(readInt(in));
		spritePlacement.setActive(in.readBoolean());
		in.skipBytes(3); // alignment!
		spritePlacement.setRotation(readInt(in));
		spritePlacement.setSpecial(readInt(in));
		spritePlacement.setBrainNumber(readInt(in));
		spritePlacement.setScriptName(readString(in, 13));
		spritePlacement.setHit(readString(in, 13));
		spritePlacement.setDie(readString(in, 13));
		spritePlacement.setTalk(readString(in, 13));
		spritePlacement.setSpeed(readInt(in));
		spritePlacement.setBaseWalk(readInt(in));
		spritePlacement.setBaseIdle(readInt(in));
		spritePlacement.setBaseAttack(readInt(in));
		spritePlacement.setBaseHit(readInt(in));
		spritePlacement.setTimer(readInt(in));
		spritePlacement.setQue(readInt(in));
		spritePlacement.setHard(readInt(in));
		spritePlacement.setAltX1(readInt(in));
		spritePlacement.setAltY1(readInt(in));
		spritePlacement.setAltX2(readInt(in));
		spritePlacement.setAltY2(readInt(in));
		spritePlacement.setProp(readInt(in));
		spritePlacement.setWarpMap(readInt(in));
		spritePlacement.setWarpX(readInt(in));
		spritePlacement.setWarpY(readInt(in));
		spritePlacement.setParamSeq(readInt(in));
		spritePlacement.setBaseDie(readInt(in));
		spritePlacement.setGold(readInt(in));
		spritePlacement.setHitPoints(readInt(in));
		spritePlacement.setStrength(readInt(in));
		spritePlacement.setDefense(readInt(in));
		spritePlacement.setExp(readInt(in));
		spritePlacement.setSound(readInt(in));
		spritePlacement.setVision(readInt(in));
		spritePlacement.setNohit(readInt(in));
		spritePlacement.setTouchDamage(readInt(in));
		spritePlacement.setBuffer(readIntArray(in, 5));
		return spritePlacement;
	}
}
