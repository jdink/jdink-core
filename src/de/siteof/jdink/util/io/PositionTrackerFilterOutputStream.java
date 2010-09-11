package de.siteof.jdink.util.io;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>Wraps around an {@link OutputStream} to track the current position.</p>
 * <p>Note: the {@link DataOutputStream} has a similar functionality built in. See {@link DataOutputStream#size()}</p>
 */
public class PositionTrackerFilterOutputStream extends FilterOutputStream {

	private long position;

	public PositionTrackerFilterOutputStream(OutputStream out) {
		super(out);
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		this.position += len;
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
		this.position += b.length;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
		this.position++;
	}
}