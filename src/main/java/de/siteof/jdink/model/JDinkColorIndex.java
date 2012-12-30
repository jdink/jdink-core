package de.siteof.jdink.model;

public class JDinkColorIndex {

	private static final char[] COLOR_CHARACTERS = {
		'\0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '!', '@', '#', '$', '%'
	};

	private static final char COLOR_PREFIX = '`';

	private final int colorIndex;

	public JDinkColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}

	public static boolean isValid(int colorIndex) {
		return ((colorIndex >= 0) && (colorIndex <= 15));
	}

	public static char getColorCharacter(int colorIndex) {
		return (isValid(colorIndex) ? COLOR_CHARACTERS[colorIndex] : '\0');
	}

	public static String getColorString(int colorIndex) {
		String result;
		if (isValid(colorIndex)) {
			result = new String(new char[] {
					COLOR_PREFIX, COLOR_CHARACTERS[colorIndex]
			});
		} else {
			result = "";
		}
		return result;
	}

	public static int getColorIndex(char colorCharacter) {
		int result = -1;
		for (int i = 1; i < COLOR_CHARACTERS.length; i++) {
			if (COLOR_CHARACTERS[i] == colorCharacter) {
				result = i;
				break;
			}
		}
		return result;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	public char getColorCharacter() {
		return getColorCharacter(colorIndex);
	}

	public String getColorString() {
		return getColorString(colorIndex);
	}

}
