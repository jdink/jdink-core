package de.siteof.jdink.format.save;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.model.JDinkSaveGameDescriptor;
import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.io.PacketDataInput;
import de.siteof.jdink.util.io.PacketDataInputStream;

public class JDinkSaveGameDescriptorLoader extends AbstractLoader {

	private JDinkSaveGameDescriptor saveGameDescriptor;

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		this.saveGameDescriptor = loadSaveGameDescriptor(in, deferrable);
	}

	protected JDinkSaveGameDescriptor loadSaveGameDescriptor(InputStream in, boolean deferrable) throws IOException {
		PacketDataInput dataInput = new PacketDataInputStream(in, 4, true);
		JDinkSaveGameDescriptor result = loadSaveGameDescriptor(dataInput, null);
		return result;
	}

	protected JDinkSaveGameDescriptor loadSaveGameDescriptor(PacketDataInput in, JDinkSaveGameDescriptor saveGame) throws IOException {
		JDinkSaveGameDescriptor result = saveGame;
		if (result == null) {
			result = new JDinkSaveGameDescriptor();
		}
		in.beginStructure(4);
		result.setVersion(readInt(in));
		result.setGameInfo(readString(in, 196));
		result.setMinutes(readInt(in));
		in.endStructure();
		return result;
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

	public JDinkSaveGameDescriptor getSaveGameDescriptor() {
		return saveGameDescriptor;
	}

}
