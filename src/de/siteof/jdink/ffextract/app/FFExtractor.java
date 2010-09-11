package de.siteof.jdink.ffextract.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.format.ff.FFLoader;

public class FFExtractor {
	
	private static final Log log	= LogFactory.getLog(FFExtractor.class);

	public boolean extract(String[] args) {
		String resourceName	= null;
		if (args.length > 0) {
			resourceName	= args[0];
		}
		if ((resourceName == null) || (resourceName.isEmpty())) {
			log.error("path to dir.ff missing");
			return false;
		}
		String targetDirectory	= null;
		if (args.length > 1) {
			resourceName	= args[1];
		}
		if (targetDirectory == null) {
			targetDirectory = resourceName + "-extracted";
		}
		File targetDirectoryFile = new File(targetDirectory);
		if (!targetDirectoryFile.exists()) {
			if (!targetDirectoryFile.mkdir()) {
				log.error("unable to create director: " + targetDirectory);
				return false;
			}
		}
		byte[] buffer = new byte[4 * 1024];
		FFLoader loader = new FFLoader();
		File file	= new File(resourceName);
		try {
			loader.load(file);
			String[] fileNames = loader.getFileNames();
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = fileNames[i].toLowerCase();

				InputStream in = null;
				OutputStream out = null;
				try {
					log.info("extracting: " + resourceName + "/" + fileName);
					in = loader.getResourceAsStream(fileName);
					if (in == null) {
						log.warn("resource not found: " + fileName);
					} else {
						out = new FileOutputStream(new File(targetDirectory, fileName));
						while (true) {
							int bytesRead = in.read(buffer);
							if (bytesRead < 0) {
								break;
							}
							out.write(buffer, 0, bytesRead);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("unable to load file(" + file + ") - " + e, e);
		} catch (IOException e) {
			log.error("unable to load file(" + file + ") - " + e, e);
		}
		return true;
	}

	public static void main(String[] args) {
		if (!(new FFExtractor()).extract(args)) {
			System.exit(-1);
		}
	}

}
