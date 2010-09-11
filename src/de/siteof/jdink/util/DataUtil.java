package de.siteof.jdink.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Data utility methods</p>
 */
public class DataUtil {

	private DataUtil() {
		// prevent instantiation
	}

	public static boolean readBoolean(DataInput in) throws IOException {
		return in.readByte() != 0;
	}

	public static int[] readIntArrayLittleEndian(DataInput in, int length) throws IOException {
		int[] a = new int[length];
		for (int i = 0; i < length; i++) {
			a[i] = readIntLittleEndian(in);
		}
		return a;
	}

	public static int readIntLittleEndian(DataInput in) throws IOException {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			value += (in.readUnsignedByte() << (i*8));
		}
		return value;
	}

	public static String readString(DataInput in, int length) throws IOException {
		StringBuffer sb = new StringBuffer();
		while(length > 0) {
			length--;
			char c = (char) in.readByte();
			if (c == 0) {
				break;
			}
			sb.append(c);
		}
		if (length > 0) {
			in.skipBytes(length);
		}
		return sb.toString();
	}

	public static void writeString(DataOutput out, String s, int length) throws IOException {
		byte[] bytes = new byte[length];
		// reserve one byte for a leasing zero
		int actualLength = Math.min(length - 1, s.length());
		for (int i = 0; i < actualLength; i++) {
			bytes[i] = (byte) s.charAt(i);
		}
		out.write(bytes);
	}

	public static InputStream getBufferedInputStream(InputStream in) {
		InputStream result;
		if ((in instanceof ByteArrayInputStream) || (in instanceof BufferedInputStream)) {
			// already buffered
			result = in;
		} else {
			result = new BufferedInputStream(in, 4096);
		}
		return result;
	}

	public static DataInput getDataInput(InputStream in) {
		DataInput result;
		if (in instanceof DataInput) {
			// already buffered
			result = (DataInput) in;
		} else {
			result = new DataInputStream(in);
		}
		return result;
	}

	public static DataInput getBufferedDataInput(InputStream in) {
		DataInput result = getDataInput(getBufferedInputStream(in));
		return result;
	}

}
