package de.siteof.jdink.util.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>PacketDataInputStream is very similar to {@link DataInputStream} but
 * implementing {@link PacketDataInput}. See {@link PacketDataInput} for further details.</p>
 *
 * <p>This class is also capable of using little endian (with the use of
 * {@link LittleEndianDataInput}</p>
 *
 * @see PacketDataInput
 * @see LittleEndianDataInput
 */
public class PacketDataInputStream extends FilterInputStream implements PacketDataInput {

	private static final Log log = LogFactory.getLog(PacketDataInputStream.class);

	private final PositionTrackerFilterInputStream inputStream;
	private final DataInput dataInput;
	private final int packSize;
	private final LinkedList<Integer> structureStack = new LinkedList<Integer>();

	private final boolean skipAutoAlignment = true;
	private int maxMemberSize;
	private int initalMaxMemberSize;


	/**
	 * Constructor
	 * @param in the input stream (the input stream must not be read from directly,
	 *   otherwise the alignment may get out of synch)
	 * @param packSize the packet size (e.g. 4)
	 * @param littleEndian true, if the data is in little endian, false otherwise
	 */
	public PacketDataInputStream(InputStream in, int packSize, boolean littleEndian) {
		super(new PositionTrackerFilterInputStream(in));
		this.inputStream = (PositionTrackerFilterInputStream) super.in;
		DataInput dataInput = new DataInputStream(this.inputStream);
		if (littleEndian) {
			dataInput = new LittleEndianDataInput(dataInput);
		}
		this.dataInput = dataInput;
		this.packSize = packSize;
	}

	@Override
	public long getPosition() throws IOException {
		return this.inputStream.getPosition();
	}

	@Override
	public void skipAlignment() throws IOException {
		this.skipAlignment(this.packSize);
	}

	@Override
	public void skipAlignmentFor(int size) throws IOException {
		skipAlignment(Math.min(this.packSize, size));
	}

	private void beforeMember(int size) throws IOException {
		if (skipAutoAlignment) {
			if (size > this.maxMemberSize) {
				this.maxMemberSize = size;
			}
			skipAlignmentFor(size);
		}
	}

	private void skipAlignment(int packSize) throws IOException {
		if (packSize > 1) {
			long position = this.inputStream.getPosition();
			int remainder = ((int) position) % packSize;
			if (remainder > 0) {
				this.dataInput.skipBytes(packSize - remainder);
			}
		}
	}

	@Override
	public void beginStructure(int maxMemberSize) throws IOException {
		// the structure need to be aligned at the size of the smallest member (before and after)
		this.skipAlignmentFor(maxMemberSize);
		structureStack.add(Integer.valueOf(maxMemberSize));
		this.initalMaxMemberSize = maxMemberSize;
		this.maxMemberSize = 0;
	}

	@Override
	public void endStructure() throws IOException {
		if (initalMaxMemberSize != maxMemberSize) {
			log.warn("[endStructure] maxMemberSize mismatches, initial=" + initalMaxMemberSize +
					", actual=" + maxMemberSize);
			throw new IOException("maxMemberSize mismatches, initial=" + initalMaxMemberSize +
					", actual=" + maxMemberSize);
		}
		// the structure need to be aligned at the size of the smallest member (before and after)
		if (skipAutoAlignment) {
			skipAlignmentFor(this.maxMemberSize);
		}
		maxMemberSize = structureStack.removeLast().intValue();
	}

	@Override
	public boolean readBoolean() throws IOException {
		this.beforeMember(1);
		return this.dataInput.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		this.beforeMember(1);
		return this.dataInput.readByte();
	}

	@Override
	public char readChar() throws IOException {
		this.beforeMember(2);
		return this.dataInput.readChar();
	}

	@Override
	public double readDouble() throws IOException {
		this.beforeMember(8);
		return this.dataInput.readDouble();
	}

	@Override
	public float readFloat() throws IOException {
		this.beforeMember(4);
		return this.dataInput.readFloat();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		this.dataInput.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		this.dataInput.readFully(b, off, len);
	}

	@Override
	public int readInt() throws IOException {
		this.beforeMember(4);
		return this.dataInput.readInt();
	}

	@Override
	public String readLine() throws IOException {
		return this.dataInput.readLine();
	}

	@Override
	public long readLong() throws IOException {
		this.beforeMember(8);
		return this.dataInput.readLong();
	}

	@Override
	public short readShort() throws IOException {
		this.beforeMember(2);
		return this.dataInput.readShort();
	}

	@Override
	public String readUTF() throws IOException {
		return this.dataInput.readUTF();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		this.beforeMember(1);
		return this.dataInput.readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		this.beforeMember(2);
		return this.dataInput.readUnsignedShort();
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return this.dataInput.skipBytes(n);
	}

}
