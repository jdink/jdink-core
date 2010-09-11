package de.siteof.jdink.collision;

import java.util.Map;

public class JDinkConfiguration {

	private Map<String, String> map;

	public JDinkConfiguration(Map<String, String> map) {
		this.map	= map;
	}

	public String getString(String key) {
		return map.get(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value	= this.getString(key);
		if ((value == null) || (value.length() == 0)) {
			return defaultValue;
		} else {
			return Boolean.getBoolean(key);
		}
	}

	public boolean isSoundEnabled() {
		return this.getBoolean("source.enabled", true);
	}

}
