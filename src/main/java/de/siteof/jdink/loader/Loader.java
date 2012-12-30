package de.siteof.jdink.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.siteof.jdink.io.Resource;

public interface Loader {

	public void load(Resource resource) throws IOException;

	public void load(File file) throws IOException;

	public void load(InputStream in) throws IOException;

}
