package de.siteof.jdink.loader;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.siteof.jdink.io.FileResource;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.util.DataUtil;

/**
 * <p>Abstract class for {@link Loader}.
 */
public abstract class AbstractLoader implements Loader {

	private Resource resource;

	public void load(Resource resource) throws IOException {
		this.resource = resource;
		InputStream in = (resource != null ? resource.getInputStream() : null);
		if (in == null) {
			throw new IOException("resource could not be opened");
		}

		try {
			load(in, resource.isReopenable());
		} catch (Throwable e) {
			IOException e2	= new IOException("unable to load resource \"" + resource.getName() + "\" - " + e);
			e2.initCause(e);
			throw e2;
		} finally {
			in.close();
		}
	}

	public void load(File file) throws IOException {
		load(new FileResource(file));
	}

	public final void load(InputStream in) throws IOException {
		load(in, false);
	}

	protected InputStream getBufferedInputStream(InputStream in) {
		return DataUtil.getBufferedInputStream(in);
	}

	public InputStream getNewInputStream() throws IOException {
		if ((resource != null) && (resource.isReopenable())) {
			return resource.getInputStream();
		} else {
			return null;
		}
	}

	protected abstract void load(InputStream in, boolean deferrable) throws IOException;

	public Resource getResource() {
		return resource;
	}
}
