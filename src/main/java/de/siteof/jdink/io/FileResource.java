package de.siteof.jdink.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>File implementation of {@link Resource}</p>
 */
public class FileResource implements Resource {
	
	private File file;
	
	public FileResource(File file) {
		this.file = file;
		if (file == null) {
			throw new IllegalArgumentException("file==null");
		}
	}

	public File getFile() {
		return file;
	}

	@Override
	public boolean isReopenable() {
		return true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(file);
	}

	@Override
	public String getName() {
		return file.toString();
	}

}
