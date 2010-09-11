package de.siteof.jdink.util.io;

import java.io.DataOutput;
import java.io.IOException;

public abstract class AbstractDataOutputProxy implements DataOutput {

	private final DataOutput dataOutput;

	public AbstractDataOutputProxy(DataOutput dataOutput) {
		this.dataOutput = dataOutput;
	}

	public void write(byte[] b, int off, int len) throws IOException {
		dataOutput.write(b, off, len);
	}

	public void write(byte[] b) throws IOException {
		dataOutput.write(b);
	}

	public void write(int b) throws IOException {
		dataOutput.write(b);
	}

	public void writeBoolean(boolean v) throws IOException {
		dataOutput.writeBoolean(v);
	}

	public void writeByte(int v) throws IOException {
		dataOutput.writeByte(v);
	}

	public void writeBytes(String s) throws IOException {
		dataOutput.writeBytes(s);
	}

	public void writeChar(int v) throws IOException {
		dataOutput.writeChar(v);
	}

	public void writeChars(String s) throws IOException {
		dataOutput.writeChars(s);
	}

	public void writeDouble(double v) throws IOException {
		dataOutput.writeDouble(v);
	}

	public void writeFloat(float v) throws IOException {
		dataOutput.writeFloat(v);
	}

	public void writeInt(int v) throws IOException {
		dataOutput.writeInt(v);
	}

	public void writeLong(long v) throws IOException {
		dataOutput.writeLong(v);
	}

	public void writeShort(int v) throws IOException {
		dataOutput.writeShort(v);
	}

	public void writeUTF(String s) throws IOException {
		dataOutput.writeUTF(s);
	}

}
