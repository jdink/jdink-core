package de.siteof.jdink.util;

import java.io.File;

public class FileUtil {


	public static File getFileIgnoreCase(File result) {
		if ((result != null) && (!result.exists())) {
			File parent	= result.getParentFile();
			if ((parent != null) && (!parent.exists())) {
				parent	= getFileIgnoreCase(parent);
			}
			if ((parent != null) && (parent.exists())) {
				String name	= result.getName();
				String[] fileNames	= parent.list();
				if (fileNames != null) {
					for (int i = 0; i < fileNames.length; i++) {
						if (fileNames[i].equalsIgnoreCase(name)) {
							result	= new File(parent, fileNames[i]);
							break;
						}
					}
				}
			}
		}
		return result;
	}

}
