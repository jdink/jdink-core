package de.siteof.jdink.events;

import java.util.EventObject;

public class JDinkKeyEvent extends EventObject {

	private static final long serialVersionUID = 1L;

    /**
     * The first number in the range of ids used for key events.
     */
    public static final int KEY_FIRST = 400;

    /**
     * The last number in the range of ids used for key events.
     */
    public static final int KEY_LAST  = 402;

    /**
     * The "key typed" event.  This event is generated when a character is
     * entered.  In the simplest case, it is produced by a single key press.  
     * Often, however, characters are produced by series of key presses, and 
     * the mapping from key pressed events to key typed events may be 
     * many-to-one or many-to-many.  
     */
    public static final int KEY_TYPED = KEY_FIRST;

    /**
     * The "key pressed" event. This event is generated when a key
     * is pushed down.
     */
    public static final int KEY_PRESSED = 1 + KEY_FIRST; //Event.KEY_PRESS

    /**
     * The "key released" event. This event is generated when a key
     * is let up.
     */
    public static final int KEY_RELEASED = 2 + KEY_FIRST; //Event.KEY_RELEASE

    private final int id;
	private final int keyCode;
	private final char keyChar;

	public JDinkKeyEvent(Object source, int id, int keyCode, char keyChar) {
		super(source);
		this.id = id;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
	}

	@Override
	public String toString() {
		return "JDinkKeyEvent [id=" + id + ", keyChar=" + keyChar
				+ ", keyCode=" + keyCode + ", source=" + source + "]";
	}

	public boolean isKeyTyped() {
		return (id == KEY_TYPED);
	}
	
	public boolean isKeyPressed() {
		return (id == KEY_PRESSED);
	}
	
	public boolean isKeyReleased() {
		return (id == KEY_RELEASED);
	}

	public int getId() {
		return id;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public char getKeyChar() {
		return keyChar;
	}

}
