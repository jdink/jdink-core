package de.siteof.jdink.serializer;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamSerializer<T> {

	public void serialize(OutputStream out, T object) throws IOException;

}
