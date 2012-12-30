package de.siteof.jdink.util.io;

import java.io.DataInput;
import java.io.IOException;

public abstract class AbstractDataInputProxy implements DataInput {
	
	private final DataInput dataInput;
	
	public AbstractDataInputProxy(DataInput dataInput) {
		this.dataInput = dataInput;
	}

	@Override
	public boolean readBoolean() throws IOException {
		return this.dataInput.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return this.dataInput.readByte();
	}

	@Override
	public char readChar() throws IOException {
		return this.dataInput.readChar();
	}

	@Override
	public double readDouble() throws IOException {
		return this.dataInput.readDouble();
	}

	@Override
	public float readFloat() throws IOException {
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
		return this.dataInput.readInt();
	}

	@Override
	public String readLine() throws IOException {
		return this.dataInput.readLine();
	}

	@Override
	public long readLong() throws IOException {
		return this.dataInput.readLong();
	}

	@Override
	public short readShort() throws IOException {
		return this.dataInput.readShort();
	}

	@Override
	public String readUTF() throws IOException {
		return this.dataInput.readUTF();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return this.dataInput.readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return this.dataInput.readUnsignedShort();
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return this.dataInput.skipBytes(n);
	}

}
