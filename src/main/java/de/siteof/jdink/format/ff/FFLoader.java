package de.siteof.jdink.format.ff;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.util.DataUtil;
import de.siteof.jdink.util.io.PositionTrackerFilterInputStream;

/**
 * <p>Loads files in the ".ff" format - uncompressed package (similar to zip)</p>
 * <p>This is called "fastfile", created by Microsoft.</p>
 * <p>The format is defined in fastfile.c and ffent.h</p>
 * <p>A DWORD denoting the number of entries, following a list of file entries:</p>
 * <pre>typedef struct {
    long	offset;
    char	name[13];
} FILEENTRY, *LPFILEENTRY;</pre>
 */
public class FFLoader {

	/**
	 * A single file entry
	 */
	private class FileEntry {
		private int address;
		private String name;
		private byte[] bytes;

		public int getAddress() {
			return address;
		}

		public void setAddress(int address) {
			this.address = address;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private static final Log log = LogFactory.getLog(FFLoader.class);

	private Map<String, FileEntry> fileEntryByNameIgnoreCaseHash;
	private Collection<String> fileNames;
	private FileEntry[] fileEntries;
	private File inputFile;
	private boolean dataLoaded;

	public FFLoader() {

	}

	public void load(File file) throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			if (log.isDebugEnabled()) {
				log.debug("FF-File: " + file);
			}
			load(in, false);
			inputFile = file;
		} finally {
			in.close();
		}
	}

	public void load(InputStream in) throws IOException {
		load(in, true);
	}

	public void load(InputStream in, boolean loadData) throws IOException {
		PositionTrackerFilterInputStream posIn = new PositionTrackerFilterInputStream(in);
		DataInput dataInput = DataUtil.getBufferedDataInput(in);
		int count = readInt(dataInput);
		if (count > 10000) {
			throw new IOException("too many files");
		}
		if (count <= 0) {
			throw new IOException("no files found");
		}
		Map<String, FileEntry> fileEntryByNameIgnoreCaseHash = new HashMap<String, FileEntry>();
		FileEntry[] fileEntries = new FileEntry[count];
		Collection<String> fileNames = new ArrayList<String>();
		for (int index = 0; index < count; index++) {
			FileEntry fileEntry = new FileEntry();
			fileEntry.setAddress(readInt(dataInput));
			fileEntry.setName(DataUtil.readString(dataInput, 13));
			fileEntries[index] = fileEntry;
			if (fileEntry.getName().length() > 0) {
				fileEntryByNameIgnoreCaseHash.put(fileEntry.getName().toUpperCase(), fileEntry);
				fileNames.add(fileEntry.getName());
			}
			//log.debug("name: " + fileEntry.getName());
		}
		this.fileEntryByNameIgnoreCaseHash	= fileEntryByNameIgnoreCaseHash;
		this.fileNames = fileNames;
		this.fileEntries = fileEntries;
		if (loadData) {
			dataLoaded = true;
			loadDataNow(in, (int) posIn.getPosition());
		}
	}

	protected void loadDataNow(InputStream in, int offset) throws IOException {
		int count = fileEntries.length;
		int index = 0;
		while ((index+1 < count) && (fileEntries[index].getAddress() == 0)) {
			index++;
		}
		if (index+1 < count) {
			if (offset < fileEntries[index].getAddress()) {
				in.skip(fileEntries[index].getAddress() - offset);
			} else {
				if (offset != fileEntries[index].getAddress()) {
					throw new IOException("offset not found: 0x" + Integer.toHexString(fileEntries[0].getAddress()).toUpperCase() +
							" (name:" + fileEntries[index].getName() + ")");
				}
			}
		}
		for (; index+1 < count; index++) {
			int size = fileEntries[index+1].getAddress() - fileEntries[index].getAddress();
			if ((size < 0) || (size > 1024 * 1024 * 1024)) {
				throw new IOException("invalid size: " + size +  " (name:" + fileEntries[index].getName() + ")" +
						"  (0x" + Integer.toHexString(fileEntries[index+1].getAddress()).toUpperCase() + " - " +
						" 0x" + Integer.toHexString(fileEntries[index].getAddress()).toUpperCase() + ")");
			}
			byte[] bytes = new byte[size];
			in.read(bytes);
			fileEntries[index].setBytes(bytes);
		}
	}

	protected void loadDataNow() {
		dataLoaded = true;
		try {
			FileInputStream in = new FileInputStream(inputFile);
			try {
				loadDataNow(in, 0);
			} finally {
				in.close();
			}
		} catch (FileNotFoundException e) {
			log.error("error reading file (" + inputFile + ")" + " - " + e, e);
		} catch (IOException e) {
			log.error("error reading file (" + inputFile + ")" + " - " + e, e);
		}
	}

	protected int readInt(DataInput in) throws IOException {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			int b = in.readUnsignedByte();
			result += (b << (i*8));
		}
		return result;
	}

	protected String readString(DataInput in) throws IOException {
		StringBuffer sb = new StringBuffer();
		while(true) {
			char c = (char) in.readByte();
			if (c == 0) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public String[] getFileNames() {
		return (String[]) fileNames.toArray(new String[0]);
	}

	protected FileEntry getFileEntryByNameIgnoreCase(String name) {
		if (name != null) {
			return (FileEntry) fileEntryByNameIgnoreCaseHash.get(name.toUpperCase());
		} else {
			return null;
		}
	}

	public byte[] getResourceBytes(String name) {
		FileEntry fileEntry = getFileEntryByNameIgnoreCase(name);
		if (fileEntry != null) {
			if (!dataLoaded) {
				loadDataNow();
			}
			return fileEntry.getBytes();
		} else {
			return null;
		}
	}

	public boolean hasResource(String name) {
		FileEntry fileEntry = getFileEntryByNameIgnoreCase(name);
		return (fileEntry != null);
	}

	public InputStream getResourceAsStream(String name) {
		FileEntry fileEntry = getFileEntryByNameIgnoreCase(name);
		if (fileEntry != null) {
			if ((!dataLoaded) && (fileEntry.getBytes() == null)) {
				loadDataNow();
			}
			if (fileEntry.getBytes() != null) {
				return new ByteArrayInputStream(fileEntry.getBytes());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
