package de.siteof.jdink.events;

import java.util.EventObject;

public class JDinkFrameEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
     * The first number in the range of ids used for frame events.
     */
    public static final int FRAME_FIRST = 900;

    /**
     * The last number in the range of ids used for frame events.
     */
    public static final int FRAME_LAST  = 901;

    /**
     * The begin of the frame.
     */
    public static final int BEGIN_FRAME = FRAME_FIRST;

    /**
     * The end of the frame.
     */
    public static final int END_FRAME = 1 + FRAME_FIRST;

    private final int id;

	public JDinkFrameEvent(Object source, int id) {
		super(source);
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
