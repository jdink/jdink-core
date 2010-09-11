package de.siteof.jdink.loader;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.format.ff.FFLoader;
import de.siteof.jdink.io.FileResource;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.util.FileUtil;

/**
 * <p>Manages access to all kind of resources</p>
 * <p>Resources may not represent physical files but could be embedded in another package for example</p>
 * TODO reuse IResource from de.siteof.resource
 */
public class JDinkFileManager {

	private static class PrefixFileFilter implements FileFilter {

		private final String prefix;

		public PrefixFileFilter(String prefix) {
			prefix	= prefix.replace('\\', '/');
			this.prefix = prefix.toLowerCase();
		}

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().replace('\\', '/').toLowerCase().startsWith(prefix);
		}

	}

	private static class FFPackage {
		private final Resource packageResource;

		private transient Reference<FFLoader> ffLoader;

		public FFPackage(Resource packageResource) {
			this.packageResource = packageResource;
		}

		public FFLoader getFFLoader() throws IOException {
			FFLoader result = (ffLoader != null ? ffLoader.get() : null);
			if (result == null) {
				result = new FFLoader();
				if (packageResource instanceof FileResource) {
					result.load(((FileResource) packageResource).getFile());
				} else {
					result.load(packageResource.getInputStream());
				}
				ffLoader = new WeakReference<FFLoader>(result);
			}
			return result;
		}

		public String[] getFileNames() throws IOException {
			return getFFLoader().getFileNames();
		}

		public boolean hasResource(String name) throws IOException {
			return getFFLoader().hasResource(name);
		}

		public InputStream getResourceAsStream(String name) throws IOException {
			return getFFLoader().getResourceAsStream(name);
		}
	}

	private static class FFLoaderFileResouce implements Resource {

		private final FFPackage ffPackage;
		private final String name;

		public FFLoaderFileResouce(FFPackage ffPackage, String name) {
			this.ffPackage = ffPackage;
			this.name = name;
		}

		@Override
		public boolean isReopenable() {
			return true;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return ffPackage.getResourceAsStream(name);
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("embedded files are readonly");
		}

		@Override
		public String getName() {
			return name;
		}

	}

	private File[] pathParents;
//	private Map<String, FFLoader> ffLoaderMap = new HashMap<String, FFLoader>();
	private final Map<String, FFPackage> ffPackageMap = new HashMap<String, FFPackage>();
	private final Map<String, Resource> resourceMap = new HashMap<String, Resource>();
	private boolean caseSensitive;

	private static final Log log	= LogFactory.getLog(JDinkFileManager.class);

	public File[] getPathParents() {
		return pathParents;
	}

	public void setPathParents(File[] pathParents) {
		this.pathParents = pathParents;
	}

	public String[] getResourceNamesByPrefix(String prefix) throws IOException {
		return getResourceNamesByPrefix(prefix, true);
	}

	public String[] getResourceNamesByPrefix(String prefix, boolean allowDefaults) throws IOException {
		Collection<String> fileNames = new ArrayList<String>();
		File[] pathParents;
		if (allowDefaults) {
			pathParents = this.pathParents;
		} else {
			pathParents = new File[] { this.pathParents[0] };
		}
		prefix	= prefix.replace('\\', '/');
		for (int i = 0; i < pathParents.length; i++) {
			File pathParent = pathParents[i];
			File file;
			file = new File(pathParent, prefix);
			String prefixBaseName = file.getName().toLowerCase();
			File dir = file.getParentFile();
			String parentDirectoryName;
			if (!dir.exists()) {
				dir = FileUtil.getFileIgnoreCase(dir);
				if (!dir.exists()) {
					// still not found
					continue;
				}
				String s = dir.toString();
				parentDirectoryName = new File(prefix).getParent();
				if (s.length() >= parentDirectoryName.length()) {
					parentDirectoryName = s.substring(s.length() - parentDirectoryName.length());
				}
			} else {
				parentDirectoryName = new File(prefix).getParent();
			}
			File[] files = dir.listFiles(new PrefixFileFilter(prefixBaseName));
			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					File file2 = files[j];
					if ((file2.exists()) && (file2.canRead()) &&
							(!file2.getName().toLowerCase().equals("dir.ff"))) {
						//fileNames.add(file2.getAbsolutePath());
						fileNames.add(new File(parentDirectoryName, file2.getName()).toString());
					}
				}
			}
			file = new File(dir, "dir.ff");
			FFPackage ffPackage = ffPackageMap.get(file.getAbsolutePath());
			if (ffPackage == null) {
				ffPackage = ffPackageMap.get(file.getAbsolutePath().toLowerCase());
			}
			if (ffPackage == null) {
				file	= FileUtil.getFileIgnoreCase(file);
			}
			if ((ffPackage == null) && (file.exists()) && (file.canRead())) {
				ffPackage = new FFPackage(new FileResource(file));
				ffPackageMap.put(file.getAbsolutePath(), ffPackage);
				ffPackageMap.put(file.getAbsolutePath().toLowerCase(), ffPackage);
			}
			if (ffPackage != null) {
				String[] fileNames2 = ffPackage.getFileNames();
				for (int j = 0; j < fileNames2.length; j++) {
					if ((fileNames2[j].toLowerCase().startsWith(prefixBaseName)) &&
							(!fileNames2[j].toLowerCase().equals("dir.ff"))) {
						fileNames.add(new File(parentDirectoryName, fileNames2[j]).toString());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("no dir.ff: " + file);
				}
			}
		}
		return (String[]) fileNames.toArray(new String[0]);
	}

	public Resource getResource(String name) throws IOException {
		return getResource(name, true);
	}

	public Resource getResource(String name, boolean allowDefaults) throws IOException {
		Resource resource = (Resource) resourceMap.get(name);
		if (resource != null) {
			return resource;
		}
		resource = (Resource) resourceMap.get(name.toLowerCase());
		if (resource != null) {
			return resource;
		}
		File[] pathParents;
		if (allowDefaults) {
			pathParents = this.pathParents;
		} else {
			pathParents = new File[] { this.pathParents[0] };
		}
		for (int i = 0; i < pathParents.length; i++) {
			File pathParent = pathParents[i];
			File file;
			file	= new File(pathParent, name);
			file	= FileUtil.getFileIgnoreCase(file);
			if ((file.exists()) && (file.canRead())) {
				resource = new FileResource(file);
				resourceMap.put(name, resource);
				resourceMap.put(name.toLowerCase(), resource);
				return resource;
			}
			String baseName = file.getName();
			file	= new File(file.getParentFile(), "dir.ff");

			file = new File(file.getParentFile(), "dir.ff");
			FFPackage ffPackage = ffPackageMap.get(file.getAbsolutePath());
			if (ffPackage == null) {
				ffPackage = ffPackageMap.get(file.getAbsolutePath().toLowerCase());
			}
			if (ffPackage == null) {
				file	= FileUtil.getFileIgnoreCase(file);
			}
			if ((ffPackage == null) && (file.exists()) && (file.canRead())) {
				ffPackage = new FFPackage(new FileResource(file));
				ffPackageMap.put(file.getAbsolutePath(), ffPackage);
				ffPackageMap.put(file.getAbsolutePath().toLowerCase(), ffPackage);
			}
			if (ffPackage != null) {
				if (ffPackage.hasResource(baseName)) {
					resource = new FFLoaderFileResouce(ffPackage, baseName);
					resourceMap.put(name, resource);
					resourceMap.put(name.toLowerCase(), resource);
					return resource;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("no dir.ff: " + file);
				}
			}
		}
		return null;
	}

	private boolean mkdirs(File file) {
		boolean result;
		if (file == null) {
			result = false;
		} else if (!file.exists()) {
			result = mkdirs(file.getParentFile());
		} else {
			result = true;
		}
		return result;
	}

	public Resource getOutputResource(String name) throws IOException {
		File file = new File(this.pathParents[0], name);
		File parentFile = file.getParentFile();
		mkdirs(parentFile);
		return new FileResource(file);
	}

	public InputStream getResourceAsStream(String name) throws IOException {
		Resource resource = getResource(name);
		if (resource != null) {
			return resource.getInputStream();
		} else {
			return null;
		}
//		for (int i = 0; i < pathParents.length; i++) {
//			File pathParent = pathParents[i];
//			File file;
//			file = new File(pathParent, name);
//			if ((file.exists()) && (file.canRead())) {
//				return new FileInputStream(file);
//			}
//			String baseName = file.getName();
//			file = new File(file.getParentFile(), "dir.ff");
//			FFLoader loader = (FFLoader) ffLoaderMap.get(file.getAbsolutePath());
//			if ((loader == null) && (file.exists()) && (file.canRead())) {
//				loader = new FFLoader();
//				loader.load(file);
//				ffLoaderMap.put(file.getAbsolutePath(), loader);
//			}
//			if (loader != null) {
//				InputStream in = loader.getResourceAsStream(baseName);
//				if (in != null) {
//					return in;
//				}
//			}
//		}
//		return null;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
