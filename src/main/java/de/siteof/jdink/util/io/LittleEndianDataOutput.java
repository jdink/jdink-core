package de.siteof.jdink.util.io;

import java.io.DataOutput;
import java.io.IOException;

public class LittleEndianDataOutput extends AbstractDataOutputProxy {

	public LittleEndianDataOutput(DataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		this.writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeFloat(float v) throws IOException {
		this.writeFloat(Float.floatToIntBits(v));
	}

	@Override
	public void writeInt(int v) throws IOException {
		super.writeInt(Integer.reverseBytes(v));
	}

	@Override
	public void writeLong(long v) throws IOException {
		super.writeLong(Long.reverseBytes(v));
	}

	@Override
	public void writeShort(int v) throws IOException {
		super.writeShort(Short.reverseBytes((short) v));
	}

}
