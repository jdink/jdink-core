package de.siteof.jdink.util;

import java.util.Map;

public interface IntegerMap<V> extends Map<Integer, V> {
	
	V get(int key);
	
	V put(int key, V value);

	V remove(int key);

}
