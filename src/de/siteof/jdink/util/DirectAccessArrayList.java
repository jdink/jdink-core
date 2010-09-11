package de.siteof.jdink.util;

import java.util.AbstractList;
import java.util.Arrays;

public final class DirectAccessArrayList<E> extends AbstractList<E> {

	private E[] elementData;
	private int size;
	
	public DirectAccessArrayList(E[] elementData) {
		this(elementData, elementData.length);
	}
	
	public DirectAccessArrayList(E[] elementData, int size) {
		this.elementData = elementData;
		this.size = size;
	}
	
	public final E[] getElementData() {
		return elementData;
	}
	
	public final E[] getArray() {
		E[] result = elementData;
		if (size != result.length) {
			result = ArrayUtil.copyOf(elementData, size);
		}
		return result;
	}
	
	public final void setCapacity(int size) {
		modCount++;
		elementData = ArrayUtil.copyOf(elementData, size);
	}

	private final void ensureCapacityAndIncreaseModCount(int minCapacity) {
		this.modCount++;
		ensureCapacity(minCapacity);
	}

	public final void ensureCapacity(int minCapacity) {
		int oldCapacity = elementData.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			setCapacity(newCapacity);
		}
	}

	public final void setSize(int size) {
		if (size > elementData.length) {
			setCapacity(size);
		}
		this.size = size;
	}

	@Override
	public final E get(int index) {
		return elementData[index];
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public final E set(int index, E element) {
		E result = elementData[index];
		elementData[index] = element;
		return result;
	}

	@Override
	public final Object[] toArray() {
		return getArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] a) {
		T[] result;
        if (a.length < size) {
            result = (T[]) ArrayUtil.copyOf(elementData, size, a.getClass());
        } else {
        	System.arraycopy(elementData, 0, a, 0, size);
        	result = a;
        }
        return result;
	}

	@Override
    public boolean add(E e) {
		ensureCapacityAndIncreaseModCount(size + 1);
		elementData[size++] = e;
		return true;
	}

	@Override
	public void add(int index, E element) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);
		}
		ensureCapacityAndIncreaseModCount(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1,
				 size - index);
		elementData[index] = element;
		size++;
		super.add(index, element);
	}

}
