package de.siteof.jdink.view;

import java.io.IOException;

import de.siteof.jdink.io.Resource;

public interface JDinkImageFactory {

	JDinkImage getImage(Resource resource) throws IOException;

	JDinkImage getMaskedImage(JDinkImage image, int backgroundColor) throws IOException;

}
