package de.siteof.jdink.util.io;

import java.io.DataInput;
import java.io.IOException;

public class LittleEndianDataInput extends AbstractDataInputProxy {
	
	public LittleEndianDataInput(DataInput dataInput) {
		super(dataInput);
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public int readInt() throws IOException {
		int result = super.readInt();
//		result = ((result >> 24) & 0xFF) +
//				((result & 0xFF0000) >> 8) +
//				((result & 0xFF00) << 8) +
//				((result & 0xFF) << 24);
		result = Integer.reverseBytes(result);
		return result;
	}

	@Override
	public long readLong() throws IOException {
		long result = super.readLong();
		result = Long.reverseBytes(result);
		return result;
	}

	@Override
	public short readShort() throws IOException {
		short result = super.readShort();
		result = Short.reverseBytes(result);
		return result;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return this.readShort() & 0xFFFF;
	}

}
