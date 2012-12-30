package de.siteof.jdink.util;

import de.siteof.cache.IObjectCache;
import de.siteof.cache.WeakReferenceObjectCacheProxy;

public final class ObjectCacheFactory {
	
	private static final ObjectCacheFactory instance = new ObjectCacheFactory(); 
	
	private ObjectCacheFactory() {
	}
	
	public static final ObjectCacheFactory getInstance() {
		return instance;
	}
	
	public <K, V> IObjectCache<K, V> createCache(String name) {
		return WeakReferenceObjectCacheProxy.getInstance(new de.siteof.cache.ObjectCache<K, V>());
	}

}
