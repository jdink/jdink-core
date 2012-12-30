package de.siteof.jdink.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>File implementation of {@link Resource}</p>
 */
public class ClassPathResource implements Resource {
	
	private final Class<?> parent;
	private final String resourceName;
	
	public ClassPathResource(Class<?> parent, String resourceName) {
		this.parent = parent;
		this.resourceName = resourceName;
		if (resourceName == null) {
			throw new IllegalArgumentException("resourceName==null");
		}
	}
	
	public ClassPathResource(String resourceName) {
		this(null, resourceName);
	}

	@Override
	public boolean isReopenable() {
		return true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream in;
		if (parent != null) {
			in = parent.getResourceAsStream(resourceName);
		} else {
			in = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		}
		return in;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return resourceName;
	}

}
