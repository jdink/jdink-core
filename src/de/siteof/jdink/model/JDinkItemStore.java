package de.siteof.jdink.model;

import java.util.HashMap;
import java.util.Map;

import de.siteof.jdink.util.ArrayListMap;
import de.siteof.jdink.util.IntegerMap;

public class JDinkItemStore {

	private final IntegerMap<JDinkItem> items = new ArrayListMap<JDinkItem>(new JDinkItem[10]);

	public void clear() {
		items.clear();
	}

	public JDinkItem allocateItem() {
		int itemNumber = 1;
		while (items.get(itemNumber) != null) {
			itemNumber++;
		}

		JDinkItem item = new JDinkItem(itemNumber);
		items.put(itemNumber, item);
		return item;
	}

//	public JDinkItem[] getItems() {
//		return items.toArray(new JDinkItem[items.size()]);
//	}

	public JDinkItem getItem(int itemNumber) {
		return items.get(itemNumber);
	}

	public Map<Integer, JDinkItem> getItems() {
		return new HashMap<Integer, JDinkItem>(items);
	}

	public JDinkItem setItem(int itemNumber, JDinkItem item) {
		return items.put(itemNumber, item);
	}

}
