package de.siteof.jdink.model;

public class JDinkTextFragment {

	private final String text;
	private final int color;

	public JDinkTextFragment(String text, int color) {
		this.text = text;
		this.color = color;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

}
