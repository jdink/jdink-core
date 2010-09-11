package de.siteof.jdink.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Generic resource</p>
 */
public interface Resource {
	
	boolean isReopenable();
	
	String getName();

	InputStream getInputStream() throws IOException;
	
	OutputStream getOutputStream() throws IOException;

}
