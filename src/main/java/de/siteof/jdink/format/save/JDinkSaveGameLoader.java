package de.siteof.jdink.format.save;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkMapState;
import de.siteof.jdink.model.JDinkSaveGame;
import de.siteof.jdink.script.JDinkIntegerType;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.script.JDinkVariable;
import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.io.PacketDataInput;
import de.siteof.jdink.util.io.PacketDataInputStream;

public class JDinkSaveGameLoader extends AbstractLoader {

	private static final Log log = LogFactory.getLog(JDinkSaveGameLoader.class);

	private JDinkSaveGame saveGame;

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		this.saveGame = loadSaveGame(in, deferrable);
	}

	protected JDinkSaveGame loadSaveGame(InputStream in, boolean deferrable) throws IOException {
		in = getBufferedInputStream(in);
		PacketDataInput dataInput = new PacketDataInputStream(in, 4, true);
		JDinkSaveGame result = loadSaveGame(dataInput, null);
		if (in.available() > 0) {
			log.warn("[loadSaveGame] finished reading but still bytes left");
		}
		return result;
	}

	protected JDinkSaveGame loadSaveGame(PacketDataInput in, JDinkSaveGame saveGame) throws IOException {
		JDinkSaveGame result = saveGame;
		if (result == null) {
			result = new JDinkSaveGame();
		}
		in.beginStructure(4);
		result.setVersion(readInt(in));
		result.setGameInfo(readString(in, 196));
		result.setMinutes(readInt(in));
		result.setX(readInt(in));
		result.setY(readInt(in));
		result.setDie(readInt(in));
		result.setSize(readInt(in));
		result.setDefense(readInt(in));
		result.setDir(readInt(in));
		result.setPframe(readInt(in));
		result.setPseq(readInt(in));
		result.setSeq(readInt(in));
		result.setFrame(readInt(in));
		result.setStrength(readInt(in));
		result.setBaseWalk(readInt(in));
		result.setBaseIdle(readInt(in));
		result.setBaseHit(readInt(in));
		result.setQue(readInt(in));

		result.setMagicItems(readItemMap(in, 9));

		result.setItems(readItemMap(in, 17));

		result.setCuritem(readInt(in));

		// play.unused
		readInt(in);

		result.setCounter(readInt(in));

		result.setIdle(readBoolean(in));

		result.setMapStateMap(readMapStateMap(in, 769));

		int[] buttons = new int[10];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = readInt(in);
		}
		result.setButtons(buttons);

		result.setVariableScopeMap(readVariablesMap(in, 250));

		result.setPushActive(readBoolean(in));
		result.setPushDir(readInt(in));
		result.setPushTimer(readInt(in) & 0xFFFFFFFFl);
		result.setLastTalk(readInt(in));
		result.setMouse(readInt(in));
		result.setItemMagic(readBoolean(in));
		result.setLastMap(readInt(in));

		// play.crap
		readInt(in);
		// play.buff[95] + play.dbuff[20] + lbuff[10]
		in.skipBytes((95 + 20 + 10) * 4);

		result.setMapdat(readString(in, 50));
		result.setDinkdat(readString(in, 50));
		result.setPalette(readString(in, 50));
		result.setTileMap(readTileMap(in, 42));
		result.setGlobalFunctionMap(readGlobalFunctionMap(in, 100));

		in.skipBytes(750);

		in.endStructure();

		/*
    int version;
    char gameinfo[196];
    int minutes;
    int x,y,die, size, defense, dir, pframe, pseq, seq, frame, strength, base_walk, base_idle,


        base_hit,que;

    item_struct mitem[9]; //added one to these, because I don't like referring to a 0 item
    item_struct item[17];

    int curitem, unused;
    int counter;
    bool idle;
    mydata spmap[769];
    int button[10];
    varman var[max_vars];


    bool push_active;
    int push_dir;
    DWORD push_timer;
    int last_talk;
    int mouse;
    bool item_magic;
    int last_map;
    int crap;
    int buff[95];
    DWORD dbuff[20];

    long lbuff[10];

    //redink1... use wasted space for storing file location of map.dat, dink.dat, palette, and tiles
    char mapdat[50];
    char dinkdat[50];
    char palette[50];
    player_info_tile tile[42];
    global_function func[100];

    char  cbuff[750];
*/
		return result;
	}

	private Map<Integer, JDinkItem> readItemMap(PacketDataInput in, int count) throws IOException {
		Map<Integer, JDinkItem> result = new HashMap<Integer, JDinkItem>(count);
		for (int index = 0; index < count; index++) {
			in.beginStructure(4);
			boolean active = readBoolean(in);
			String name = readString(in, 10);
			int seq = readInt(in);
			int frame = readInt(in);
			in.endStructure();
			if (active) {
				int itemNumber = index;
				JDinkItem item = new JDinkItem(itemNumber);
				item.setSequenceNumber(seq);
				item.setFrameNumber(frame);
				if (name.length() > 0) {
					JDinkScriptFile scriptFile = new JDinkScriptFile();
					scriptFile.setFileName(name);
					JDinkScriptInstance scriptInstance = new JDinkScriptInstance();
					scriptInstance.setScriptFile(scriptFile);
					item.setScriptInstance(scriptInstance);
				}
				result.put(Integer.valueOf(itemNumber), item);
			}
		}
		return result;
	}

	private Map<Integer, JDinkMapState> readMapStateMap(PacketDataInput in, int count) throws IOException {
		Map<Integer, JDinkMapState> result = new HashMap<Integer, JDinkMapState>(count);
		for (int index = 0; index < count; index++) {
			in.beginStructure(4);
			JDinkMapState mapState = new JDinkMapState();
			for (int i = 0; i < 100; i++) {
				int type = in.readUnsignedByte();
				if (type != 0) {
					mapState.setEditorSpriteState(i, type);
				}
			}
			for (int i = 0; i < 100; i++) {
				int sequenceNumber = in.readUnsignedShort();
				if (sequenceNumber != 0) {
					mapState.setEditorSequenceNumber(i, sequenceNumber);
				}
			}
			for (int i = 0; i < 100; i++) {
				int frameNumber = in.readUnsignedByte();
				if (frameNumber != 0) {
					mapState.setEditorFrameNumber(i, frameNumber);
				}
			}
			// TODO might need to convert time
			mapState.setLastTime(readInt(in));
			in.endStructure();

			if (!mapState.isEmpty()) {
				int mapNumber = index;
				result.put(Integer.valueOf(mapNumber), mapState);
			}
		}
		return result;
	}

	private Map<Integer, Map<String, JDinkVariable>> readVariablesMap(PacketDataInput in, int count) throws IOException {
		Map<Integer, Map<String, JDinkVariable>> result = new HashMap<Integer, Map<String, JDinkVariable>>(count);
		for (int index = 0; index < count; index++) {
			in.beginStructure(4);
			int value = readInt(in);
			String name = readString(in, 20);
			int scope = readInt(in);
			boolean active = readBoolean(in);
			in.endStructure();
			if (active) {
				JDinkVariable variable = new JDinkVariable(JDinkIntegerType.getInstance(),
						Integer.valueOf(value));
				Integer scopeKey = Integer.valueOf(scope);
				Map<String, JDinkVariable> variablesMap = result.get(scopeKey);
				if (variablesMap == null) {
					variablesMap = new HashMap<String, JDinkVariable>();
					result.put(scopeKey, variablesMap);
				}
				variablesMap.put(name, variable);
			}
		}
		return result;
	}

	private Map<Integer, String> readTileMap(PacketDataInput in, int count) throws IOException {
		Map<Integer, String> tileMap = new HashMap<Integer, String>();
		for (int i = 0; i < count; i++) {
			in.beginStructure(1);
			String s = readString(in, 50);
			in.endStructure();
			if (s.length() > 0) {
				tileMap.put(Integer.valueOf(i), s);
			}
		}
		return tileMap;
	}

	private Map<String, Collection<String>> readGlobalFunctionMap(PacketDataInput in, int count) throws IOException {
		Map<String, Collection<String>> globalFunctionMap = new HashMap<String, Collection<String>>();
		for (int i = 0; i < count; i++) {
			in.beginStructure(1);
			String file = readString(in, 10);
			String functionName = readString(in, 20);
			if ((file.length() > 0) || (functionName.length() > 0)) {
				Collection<String> functionNames = globalFunctionMap.get(file);
				if (functionNames == null) {
					functionNames = new LinkedList<String>();
					globalFunctionMap.put(file, functionNames);
				}
				functionNames.add(functionName);
			}
			in.endStructure();
		}
		return globalFunctionMap;
	}

	protected boolean readBoolean(DataInput in) throws IOException {
		return in.readBoolean();
	}

	protected int readInt(DataInput in) throws IOException {
		return in.readInt();
	}

	protected String readString(DataInput in, int length) throws IOException {
		return DataUtil.readString(in, length);
	}

	public JDinkSaveGame getSaveGame() {
		return saveGame;
	}

}
