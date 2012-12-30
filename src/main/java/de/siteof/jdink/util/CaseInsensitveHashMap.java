package de.siteof.jdink.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>This hash map is meant to work as if the String.equals method was case in-sensitive.</p>
 * <p>This class is most efficient if the keys do exactly match and are in all lower case.</p>
 *
 * @param <V> the value type of the map
 */
public class CaseInsensitveHashMap<V> extends HashMap<String, V> {

	private static final long serialVersionUID = 1L;

	private final Map<String, String> normalizedKeyMap;

	public CaseInsensitveHashMap() {
		normalizedKeyMap = new HashMap<String, String>();
	}

	private final String getNormalizedKey(String key) {
		return key.toLowerCase();
	}

	@Override
	public V get(Object key) {
		V result = super.get(key);
		if ((result == null) && (key != null) && (String.class.equals(key.getClass()))) {
			String normalizedKey = getNormalizedKey((String) key);
			String actualKey = normalizedKeyMap.get(normalizedKey);
			if (actualKey != null) {
				result = super.get(actualKey);
			} else if (!normalizedKey.equals(key)) {
				result = super.get(normalizedKey);
			}
		}
		return result;
	}

	@Override
	public V put(String key, V value) {
		String normalizedKey = getNormalizedKey(key);
		String actualKey = normalizedKeyMap.get(normalizedKey);
		V result;
		if (actualKey != null) {
			// already defined, just update the value
			result = super.put(actualKey, value);
		} else {
			if (!normalizedKey.equals(key)) {
				normalizedKeyMap.put(normalizedKey, key);
			}
			result = super.put(key, value);
		}
		return result;
	}

	@Override
	public V remove(Object key) {
		V result;
		if ((key != null) && (String.class.equals(key))) {
			String normalizedKey = getNormalizedKey((String) key);
			String actualKey = normalizedKeyMap.remove(normalizedKey);
			if (actualKey != null) {
				// defined as a different value
				result = super.remove(actualKey);
			} else {
				result = super.remove(key);
			}
		} else {
			result = super.remove(key);
		}
		return result;
	}

}
