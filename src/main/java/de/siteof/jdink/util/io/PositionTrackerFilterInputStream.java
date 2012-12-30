/**
 *
 */
package de.siteof.jdink.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PositionTrackerFilterInputStream extends FilterInputStream {

	private long position;

	public PositionTrackerFilterInputStream(InputStream in) {
		super(in);
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	@Override
	public int read() throws IOException {
		int result = super.read();
		position++;
		return result;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = super.read(b, off, len);
		position += result;
		return result;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int result = super.read(b);
		position += result;
		return result;
	}

	@Override
	public long skip(long n) throws IOException {
		long result = super.skip(n);
		position += result;
		return result;
	}
}