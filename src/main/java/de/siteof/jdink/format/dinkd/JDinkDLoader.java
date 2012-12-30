package de.siteof.jdink.format.dinkd;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.format.dinkc.JDinkCLoader;
import de.siteof.jdink.io.FileResource;
import de.siteof.jdink.util.DataUtil;

/**
 * <p>Loads files in the ".d" format - "encrypted" ".c" files</p>
 */
public class JDinkDLoader extends JDinkCLoader {

	private static final Log log = LogFactory.getLog(JDinkDLoader.class);

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// Note: we're still accessing in. Which means we must access the buffered input stream.
		in = DataUtil.getBufferedInputStream(in);
		DataInput dataInput = DataUtil.getDataInput(in);

		int c = dataInput.readUnsignedByte();
		int[][] pairs = null;
		if (c > 127) {
			int count = c - 128;
			pairs = new int[count][2];
			for (int i = 0; i < count; i++) {
				pairs[i][0] = dataInput.readUnsignedByte();
				pairs[i][1] = dataInput.readUnsignedByte();
			}
		} else {
			if (c == '\r') {
				c = '\n';
			}
			if (c == 9) {
				c = ' ';
			}
			bos.write(c);
		}
		int top = 0;
		int[] stack = new int[16];
		while(true) {
			if (top > 0) {
				top--;
				c = stack[top];
			} else {
				c = in.read();
				if (c == -1) {
					break;
				}
			}
			if (c > 127) {
				stack[top++] = pairs[c-128][1];
				stack[top++] = pairs[c-128][0];
			} else {
				if (c == '\r') {
					c = '\n';
				}
				if (c == 9) {
					c = ' ';
				}
				bos.write(c);
			}
		}

		bos.flush();

		byte[] bytes = bos.toByteArray();

		if ((this.getResource() instanceof FileResource) && (bytes.length > 0)) {
			String fileName	= this.getResource().getName() + ".c";
			try {
				FileOutputStream out = new FileOutputStream(fileName);
				out.write(bytes);
				out.close();
			} catch (FileNotFoundException e) {
				log.error("error writing to file (" + fileName + ")" + " - " + e, e);
			} catch (IOException e) {
				log.error("error writing to file (" + fileName + ")" + " - " + e, e);
			}
		}
		super.load(new ByteArrayInputStream(bytes), deferrable);
	}
}
