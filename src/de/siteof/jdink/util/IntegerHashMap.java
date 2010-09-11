package de.siteof.jdink.util;

import java.util.HashMap;

public class IntegerHashMap<V> extends HashMap<Integer, V> implements IntegerMap<V> {

	private static final long serialVersionUID = 1L;

	@Override
	public V get(int key) {
		return super.get(Integer.valueOf(key));
	}

	@Override
	public V put(int key, V value) {
		return super.put(Integer.valueOf(key), value);
	}

	@Override
	public V remove(int key) {
		return super.remove(Integer.valueOf(key));
	}

}
