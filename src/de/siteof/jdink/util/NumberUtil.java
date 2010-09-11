package de.siteof.jdink.util;

public final class NumberUtil {
	
	private NumberUtil() {
		// prevent instantiation
	}
	
	public static int compareInts(int a, int b) {
		return (a < b ? -1 : (a == b ? 0 : 1));
	}

}
