package de.siteof.jdink.util;

import java.util.AbstractList;

public class ArrayListInfiniteList<E> extends AbstractList<E> {
	
	private final DirectAccessArrayList<E> directAccessList;
	
	public ArrayListInfiniteList(E[] elementData) {
		directAccessList = new DirectAccessArrayList<E>(elementData);
	}

	@Override
	public E get(int index) {
		E result;
		if ((index >= 0) && (index < directAccessList.size())) {
			result = directAccessList.get(index);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public int size() {
		return directAccessList.size();
	}

	@Override
	public boolean add(E e) {
		return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		if (element == null) {
			throw new IllegalArgumentException("element must not be null");
		}
		if (index >= this.directAccessList.size()) {
			directAccessList.setSize(index + 10);
		}
		directAccessList.set(index, element);
		super.add(index, element);
	}

}
