package de.siteof.jdink.parser;

import java.util.Iterator;

import de.siteof.jdink.model.JDinkTextFragment;

public class JDinkTextParser {


	private static class JDinkTextParserIterator implements Iterator<JDinkTextFragment> {

		private final String text;
		private final int[] fontColors;
		private int currentPos;

		public JDinkTextParserIterator(String text, int[] fontColors) {
			this.text = text;
			this.fontColors = fontColors;
		}

		public boolean hasNext() {
			return (currentPos < text.length());
		}

		public JDinkTextFragment next() {
			JDinkTextFragment result = null;
			if (currentPos < text.length()) {
				int color = -1;
				int endPos = text.length();
				int pos = text.indexOf('`', currentPos);
				if ((pos >= 0) && (pos + 1 < text.length())) {
					if (pos > 0) {
						endPos = pos;
					} else {
						int colorIndex = -1;
						switch (text.charAt(pos + 1)) {
						case '1':
							colorIndex = 1;
							break;
						case '2':
							colorIndex = 2;
							break;
						case '3':
							colorIndex = 3;
							break;
						case '4':
							colorIndex = 4;
							break;
						case '5':
							colorIndex = 5;
							break;
						case '6':
							colorIndex = 6;
							break;
						case '7':
							colorIndex = 7;
							break;
						case '8':
							colorIndex = 8;
							break;
						case '9':
							colorIndex = 9;
							break;
						case '0':
							colorIndex = 10;
							break;
						case '!':
							colorIndex = 11;
							break;
						case '@':
							colorIndex = 12;
							break;
						case '#':
							colorIndex = 13;
							break;
						case '$':
							colorIndex = 14;
							break;
						case '%':
							colorIndex = 15;
							break;
						}
						if (colorIndex >= 0) {
							currentPos += 2;
							pos = text.indexOf('`', currentPos);
							if ((pos >= 0) && (pos + 1 < text.length())) {
								endPos = pos;
							}
							if ((this.fontColors != null) && (colorIndex < this.fontColors.length)) {
								color = this.fontColors[colorIndex];
							}
						}
					}
				}
				String s = text.substring(currentPos, endPos);
				result = new JDinkTextFragment(s, color);
				currentPos = endPos;
			}
			return result;
		}

		public void remove() {
			throw new RuntimeException("remove not supported");
		}

	}


	private int[] fontColors;

	public Iterator<JDinkTextFragment> getTextFragmentIterator(String text) {
		return new JDinkTextParserIterator(text, this.fontColors);
	}

	/**
	 * @return the fontColors
	 */
	public int[] getFontColors() {
		return fontColors;
	}

	/**
	 * @param fontColors the fontColors to set
	 */
	public void setFontColors(int[] fontColors) {
		this.fontColors = fontColors;
	}

}
