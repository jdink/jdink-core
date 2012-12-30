package de.siteof.jdink.events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import de.siteof.jdink.model.JDinkContext;

public abstract class AbstractJDinkEventListenerList<ListenerT, EventT> implements EventListener {

	private final List<ListenerT> listenerList = new ArrayList<ListenerT>();

	protected abstract void callListener(ListenerT listener, JDinkContext context, EventT event);

	public boolean isEmpty() {
		return listenerList.isEmpty();
	}

	public void fireEvent(JDinkContext context, EventT event) {
		for (ListenerT listener: listenerList) {
			callListener(listener, context, event);
		}
	}

	public void addListener(ListenerT listener) {
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}

	public void removeListener(ListenerT listener) {
		listenerList.add(listener);
	}

}
