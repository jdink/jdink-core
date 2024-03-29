package de.siteof.jdink.events;

import java.util.EventObject;

public class JDinkMouseEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	/**
     * The first number in the range of ids used for mouse events.
     */
    public static final int MOUSE_FIRST 	= 500;

    /**
     * The last number in the range of ids used for mouse events.
     */
    public static final int MOUSE_LAST          = 507;

    /**
     * The "mouse clicked" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pressed and released.
     */
    public static final int MOUSE_CLICKED = MOUSE_FIRST;
    /**
     * The "mouse pressed" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pushed down.
     */
    public static final int MOUSE_PRESSED = 1 + MOUSE_FIRST; //Event.MOUSE_DOWN

    /**
     * The "mouse released" event. This <code>MouseEvent</code>
     * occurs when a mouse button is let up.
     */
    public static final int MOUSE_RELEASED = 2 + MOUSE_FIRST; //Event.MOUSE_UP

    /**
     * The "mouse moved" event. This <code>MouseEvent</code>
     * occurs when the mouse position changes.
     */
    public static final int MOUSE_MOVED = 3 + MOUSE_FIRST; //Event.MOUSE_MOVE

    /**
     * The "mouse entered" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor enters the unobscured part of component's
     * geometry. 
     */
    public static final int MOUSE_ENTERED = 4 + MOUSE_FIRST; //Event.MOUSE_ENTER

    /**
     * The "mouse exited" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor exits the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_EXITED = 5 + MOUSE_FIRST; //Event.MOUSE_EXIT

    /**
     * The "mouse dragged" event. This <code>MouseEvent</code>
     * occurs when the mouse position changes while a mouse button is pressed.
     */
    public static final int MOUSE_DRAGGED = 6 + MOUSE_FIRST; //Event.MOUSE_DRAG

    /**
     * The "mouse wheel" event.  This is the only <code>MouseWheelEvent</code>.
     * It occurs when a mouse equipped with a wheel has its wheel rotated.
     * @since 1.4
     */
    public static final int MOUSE_WHEEL = 7 + MOUSE_FIRST;
    
    private static final String[] ID_NAME = {"MOUSE_CLICKED", "MOUSE_PRESSED",
    	"MOUSE_RELEASED", "MOUSE_MOVED", "MOUSE_ENTERED", "MOUSE_EXITED",
    	"MOUSE_DRAGGED"};

	private final int id;
	private final int x;
	private final int y;

	public JDinkMouseEvent(Object source, int id, int x, int y) {
		super(source);
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		String idString;
		int index = id - MOUSE_FIRST;
		if ((index >= 0) && (index < ID_NAME.length)) {
			idString = ID_NAME[index];
		} else {
			idString = Integer.toString(id);
		}
		return "JDinkMouseEvent [id=" + idString + ", x=" + x + ", y=" + y + "]";
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
