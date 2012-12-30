package de.siteof.jdink.util.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>PacketDataOutputStream is very similar to {@link DataOutputStream} but
 * implementing {@link PacketDataOutput}. See {@link PacketDataOutput} for further details.</p>
 *
 * <p>This class is also capable of using little endian (with the use of
 * {@link LittleEndianDataInput}</p>
 *
 * @see PacketDataInputStream
 * @see PacketDataOutput
 * @see LittleEndianDataInput
 */
public class PacketDataOutputStream extends FilterOutputStream implements PacketDataOutput {

	private static final Log log = LogFactory.getLog(PacketDataOutputStream.class);

	private final PositionTrackerFilterOutputStream outputStream;
	private final DataOutput dataOutput;
	private final int packSize;
	private final LinkedList<Integer> structureStack = new LinkedList<Integer>();
	private final byte[] alignmentBytes;

	private final boolean skipAutoAlignment = true;
	private int maxMemberSize;
	private int initalMaxMemberSize;


	/**
	 * Constructor
	 * @param in the output stream (the output stream must not be read from directly,
	 *   otherwise the alignment may get out of synch)
	 * @param packSize the packet size (e.g. 4)
	 * @param littleEndian true, if the data is in little endian, false otherwise
	 */
	public PacketDataOutputStream(OutputStream out, int packSize, boolean littleEndian) {
		super(new PositionTrackerFilterOutputStream(out));
		this.outputStream = (PositionTrackerFilterOutputStream) super.out;
		DataOutput dataOutput = new DataOutputStream(this.outputStream);
		if (littleEndian) {
			dataOutput = new LittleEndianDataOutput(dataOutput);
		}
		this.dataOutput = dataOutput;
		this.packSize = packSize;
		this.alignmentBytes = new byte[packSize];
	}

	@Override
	public long getPosition() throws IOException {
		return this.outputStream.getPosition();
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
			long position = this.outputStream.getPosition();
			int remainder = ((int) position) % packSize;
			if (remainder > 0) {
				this.dataOutput.write(this.alignmentBytes, 0,
						packSize - remainder);
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
	public void write(byte[] b, int off, int len) throws IOException {
		if (len > 0) {
			this.beforeMember(1);
			dataOutput.write(b, off, len);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (b.length > 0) {
			this.beforeMember(1);
			dataOutput.write(b);
		}
	}

	@Override
	public void write(int b) throws IOException {
		this.beforeMember(1);
		dataOutput.write(b);
	}

	public void writeBoolean(boolean v) throws IOException {
		this.beforeMember(1);
		dataOutput.writeBoolean(v);
	}

	public void writeByte(int v) throws IOException {
		this.beforeMember(1);
		dataOutput.writeByte(v);
	}

	public void writeBytes(String s) throws IOException {
		this.beforeMember(1);
		dataOutput.writeBytes(s);
	}

	public void writeChar(int v) throws IOException {
		this.beforeMember(2);
		dataOutput.writeChar(v);
	}

	public void writeChars(String s) throws IOException {
		this.beforeMember(2);
		dataOutput.writeChars(s);
	}

	public void writeDouble(double v) throws IOException {
		this.beforeMember(8);
		dataOutput.writeDouble(v);
	}

	public void writeFloat(float v) throws IOException {
		this.beforeMember(4);
		dataOutput.writeFloat(v);
	}

	public void writeInt(int v) throws IOException {
		this.beforeMember(4);
		dataOutput.writeInt(v);
	}

	public void writeLong(long v) throws IOException {
		this.beforeMember(8);
		dataOutput.writeLong(v);
	}

	public void writeShort(int v) throws IOException {
		this.beforeMember(2);
		dataOutput.writeShort(v);
	}

	public void writeUTF(String s) throws IOException {
		this.beforeMember(1);
		dataOutput.writeUTF(s);
	}

}
