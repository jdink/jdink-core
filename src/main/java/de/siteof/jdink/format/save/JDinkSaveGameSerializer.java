package de.siteof.jdink.format.save;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.model.JDinkItem;
import de.siteof.jdink.model.JDinkMapState;
import de.siteof.jdink.model.JDinkSaveGame;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.script.JDinkVariable;
import de.siteof.jdink.serializer.AbstractStreamSerializer;
import de.siteof.jdink.util.ArrayUtil;
import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.io.PacketDataOutput;
import de.siteof.jdink.util.io.PacketDataOutputStream;

public class JDinkSaveGameSerializer extends AbstractStreamSerializer<JDinkSaveGame> {

	private static final Log log = LogFactory.getLog(JDinkSaveGameSerializer.class);

	@Override
	public void serialize(OutputStream out, JDinkSaveGame object)
			throws IOException {
		this.serializeSaveGame(new PacketDataOutputStream(out, 4, true),
				object);
	}

	protected void serializeSaveGame(PacketDataOutput out, JDinkSaveGame saveGame) throws IOException {
		out.beginStructure(4);
		out.writeInt(saveGame.getVersion());
		writeString(out, saveGame.getGameInfo(), 196);
		out.writeInt(saveGame.getMinutes());
		out.writeInt(saveGame.getX());
		out.writeInt(saveGame.getY());
		out.writeInt(saveGame.getDie());
		out.writeInt(saveGame.getSize());
		out.writeInt(saveGame.getDefense());
		out.writeInt(saveGame.getDir());
		out.writeInt(saveGame.getPframe());
		out.writeInt(saveGame.getPseq());
		out.writeInt(saveGame.getSeq());
		out.writeInt(saveGame.getFrame());
		out.writeInt(saveGame.getStrength());
		out.writeInt(saveGame.getBaseWalk());
		out.writeInt(saveGame.getBaseIdle());
		out.writeInt(saveGame.getBaseHit());
		out.writeInt(saveGame.getQue());

		writeItemMap(out, saveGame.getMagicItems(), 9);

		writeItemMap(out, saveGame.getItems(), 17);

		out.writeInt(saveGame.getCuritem());

		// play.unused
		out.writeInt(0);

		out.writeInt(saveGame.getCounter());
		out.writeBoolean(saveGame.isIdle());

		this.writeMapStateMap(out, saveGame.getMapStateMap(), 769);

		int[] buttons = saveGame.getButtons();
		if (buttons == null) {
			buttons = new int[10];
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = i;
			}
		} else if (buttons.length != 0) {
			buttons = ArrayUtil.copyOf(buttons, 10);
		}
		for (int button: buttons) {
			out.writeInt(button);
		}

		writeVariablesMap(out, saveGame.getVariableScopeMap(), 250);

		out.writeBoolean(saveGame.isPushActive());
		out.writeInt(saveGame.getPushDir());
		out.writeInt((int) saveGame.getPushTimer());
		out.writeInt(saveGame.getLastTalk());
		out.writeInt(saveGame.getMouse());
		out.writeBoolean(saveGame.isItemMagic());
		out.writeInt(saveGame.getLastMap());

		// play.crap
		out.writeInt(0);
		// play.buff[95] + play.dbuff[20] + lbuff[10]
		writePadding(out, (95 + 20 + 10) * 4);

		writeString(out, saveGame.getMapdat(), 50);
		writeString(out, saveGame.getDinkdat(), 50);
		writeString(out, saveGame.getPalette(), 50);

		writeTileMap(out, saveGame.getTileMap(), 42);

		writeGlobalFunctionMap(out, saveGame.getGlobalFunctionMap(), 100);

		writePadding(out, 750);

		out.endStructure();

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
	}

	private void writeItem(PacketDataOutput out, boolean active, String name, int seq, int frame) throws IOException {
		out.beginStructure(4);
		out.writeBoolean(active);
		writeString(out, name, 10);
		out.writeInt(seq);
		out.writeInt(frame);
		out.endStructure();
	}

	private void writeItem(PacketDataOutput out, JDinkItem item) throws IOException {
		if (item != null) {
			JDinkScriptInstance scriptInstance = item.getScriptInstance();
			JDinkScriptFile scriptFile = (scriptInstance != null ? scriptInstance.getScriptFile() : null);
			String name = (scriptFile != null ? scriptFile.getFileName() : null);
			if (name != null) {
				int pos = name.lastIndexOf('/');
				if (pos >= 0) {
					name = name.substring(pos + 1);
				}
				pos = name.indexOf('.');
				if (pos >= 0) {
					name = name.substring(0, pos);
				}
			}
			writeItem(out, true, name, item.getSequenceNumber(), item.getFrameNumber());
		} else {
			writeItem(out, false, "", 0, 0);
		}
	}

	private void writeItemMap(PacketDataOutput out, Map<Integer, JDinkItem> items, int count) throws IOException {
		JDinkItem[] itemArray = new JDinkItem[count];
		if (items != null) {
			for (JDinkItem item: items.values()) {
				int index = item.getItemNumber();
				if ((index >= 0) && (index < itemArray.length)) {
					itemArray[index] = item;
				}
			}
		}
		for (JDinkItem item: itemArray) {
			writeItem(out, item);
		}
	}

	private void writeMapState(PacketDataOutput out,
			byte[] editorSpriteStates,
			short[] editorSequenceNumbers,
			byte[] editorFrameNumbers,
			int lastTime) throws IOException {
		out.beginStructure(4);
		out.write(editorSpriteStates);
		for (int i = 0; i < editorSequenceNumbers.length; i++) {
			out.writeShort(editorSequenceNumbers[i]);
		}
		out.write(editorFrameNumbers);
		out.writeInt(lastTime);
		out.endStructure();
	}

	private void writeMapState(PacketDataOutput out, JDinkMapState mapState) throws IOException {
		byte[] editorSpriteStates = new byte[100];
		short[] editorSequenceNumbers = new short[100];
		byte[] editorFrameNumbers = new byte[100];
		int lastTime = 0;
		if (mapState != null) {
			for (Integer key: mapState.getEditorSpriteStateKeys()) {
				int index = key.intValue();
				if ((index >= 0) && (index <= editorSpriteStates.length)) {
					editorSpriteStates[index] = (byte) mapState.getEditorSpriteState(index);
				} else {
					log.error("[writeMapStateMap] editor sprite index out of range: " + index);
					throw new IOException("editor sprite index out of range: " + index);
				}
			}
			for (Integer key: mapState.getEditorSequenceNumberKeys()) {
				int index = key.intValue();
				if ((index >= 0) && (index <= editorSequenceNumbers.length)) {
					editorSequenceNumbers[index] = (short) mapState.getEditorSequenceNumber(index);
				} else {
					log.error("[writeMapStateMap] editor sprite out of range: " + index);
					throw new IOException("editor sprite index out of range: " + index);
				}
			}
			for (Integer key: mapState.getEditorFrameNumberKeys()) {
				int index = key.intValue();
				if ((index >= 0) && (index <= editorFrameNumbers.length)) {
					editorFrameNumbers[index] = (byte) mapState.getEditorFrameNumber(index);
				} else {
					log.error("[writeMapStateMap] editor sprite index out of range: " + index);
					throw new IOException("editor sprite index out of range: " + index);
				}
			}
			// TODO translate time
			lastTime = (int) mapState.getLastTime();
		}
		this.writeMapState(out, editorSpriteStates, editorSequenceNumbers, editorFrameNumbers, lastTime);
	}

	private void writeMapStateMap(PacketDataOutput out, Map<Integer, JDinkMapState> mapStateMap, int count) throws IOException {
		if (mapStateMap == null) {
			mapStateMap = Collections.emptyMap();
		}
		for (int index = 0; index < count; index++) {
			this.writeMapState(out, mapStateMap.get(Integer.valueOf(index)));
		}
	}

	private void writeVariable(PacketDataOutput out, int value, String name, int scope, boolean active) throws IOException {
		out.beginStructure(4);
		out.writeInt(value);
		writeString(out, name, 20);
		out.writeInt(scope);
		out.writeBoolean(active);
		out.endStructure();
	}

	private void writeVariablesMap(PacketDataOutput out, Map<Integer, Map<String, JDinkVariable>> variableScopeMap, int count) throws IOException {
		int index = 0;
		if (variableScopeMap != null) {
			List<Integer> scopes = new ArrayList<Integer>(variableScopeMap.keySet());
			Collections.sort(scopes);
			for (Integer scope: scopes) {
				Map<String, JDinkVariable> variableMap = variableScopeMap.get(scope);
				List<String> variableNames = new ArrayList<String>(variableMap.keySet());
				Collections.sort(variableNames);
				for (String variableName: variableNames) {
					JDinkVariable variable = variableMap.get(variableName);
					if (index < count) {
						Object value = variable.getValue();
						int intValue = 0;
						if (value instanceof Integer) {
							intValue = ((Integer) value).intValue();
						} else {
							log.warn("[writeVariablesMap] not an integer: " + variableName + ", value=" + value);
							throw new IOException("not an integer: " + variableName + ", value=" + value);
						}
						writeVariable(out, intValue, variableName, scope.intValue(), true);
						index++;
					} else {
						log.error("[writeVariablesMap] no space left for variable: " + variableName);
						throw new IOException("no space left for variable: " + variableName);
					}
				}
			}
		}
		// write remaining slots
		while (index < count) {
			writeVariable(out, 0, "", 0, false);
			index++;
		}
	}

	private void writeTileMap(PacketDataOutput out, Map<Integer, String> tileMap, int count) throws IOException {
		String[] tileStrings = new String[count];
		if (tileMap != null) {
			for (Map.Entry<Integer, String> entry: tileMap.entrySet()) {
				int index = entry.getKey().intValue();
				if ((index >= 0) && (index < tileStrings.length)) {
					tileStrings[index] = entry.getValue();
				} else {
					log.error("[writeTileMap] invalid tile map index: " + index);
					throw new IOException("invalid tile map index: " + index);
				}
			}
		}
		for (int i = 0; i < tileStrings.length; i++) {
			out.beginStructure(1);
			writeString(out, tileStrings[i], 50);
			out.endStructure();
		}
	}

	private void writeGlobalFunction(PacketDataOutput out, String file, String functionName) throws IOException {
		out.beginStructure(1);
		writeString(out, file, 10);
		writeString(out, functionName, 20);
		out.endStructure();
	}

	private void writeGlobalFunctionMap(PacketDataOutput out, Map<String, Collection<String>> globalFunctionMap, int count) throws IOException {
		int index = 0;
		if (globalFunctionMap != null) {
			List<String> files = new ArrayList<String>(globalFunctionMap.keySet());
			Collections.sort(files);
			for (String file: files) {
				for (String functionName: globalFunctionMap.get(file)) {
					if (index < count) {
						writeGlobalFunction(out, file, functionName);
						index++;
					} else {
						log.error("[writeVariablesMap] no space left for global function: " + file + ", " + functionName);
						throw new IOException("no space left for global function: " + file + ", " + functionName);
					}
				}
			}
		}
		// write remaining slots
		while (index < count) {
			writeGlobalFunction(out, "", "");
			index++;
		}
	}

	private void writeString(DataOutput out, String s, int length) throws IOException {
		if (s == null) {
			s = "";
		}
		if (s.length() > length - 1) {
			log.error("[writeString] string to long, maximum length=" + (length - 1) + ", s=[" + s + "]");
			throw new IOException("string to long, maximum length=" + (length - 1) + ", s=[" + s + "]");
		}
		DataUtil.writeString(out, s, length);
	}

	private void writePadding(DataOutput out, int length) throws IOException {
		byte[] buffer = new byte[length];
		out.write(buffer);
	}

}
