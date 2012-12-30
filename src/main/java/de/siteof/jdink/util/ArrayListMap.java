package de.siteof.jdink.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayListMap<V> extends AbstractMap<Integer, V> implements IntegerMap<V> {
	
	private class Entry implements java.util.Map.Entry<Integer, V> {
		
		public final int key;
		
		public Entry(int key) {
			this.key = key;
		}
		
		public int getIntKey() {
			return key;
		}
		
		@Override
		public Integer getKey() {
			return Integer.valueOf(key);
		}

		@Override
		public V getValue() {
			return get(key);
		}

		@Override
		public V setValue(V value) {
			return put(key, value);
		}
		
	}
	
	private class EntryIterator implements Iterator<java.util.Map.Entry<Integer, V>> {
		
		private int currentIndex;
		private Entry current;
		private boolean validNext;
		
		public EntryIterator() {			
		}
		
		private void updateNext() {
			if (!validNext) {
				// find next index
				while (currentIndex < size()) {
					V value = get(currentIndex);
					if (value != null) {
						validNext = true;
						break;
					}
					currentIndex++;
				}
			}
		}

		@Override
		public boolean hasNext() {
			updateNext();
			return validNext;
		}

		@Override
		public java.util.Map.Entry<Integer, V> next() {
			updateNext();
			if (!validNext) {
				current = null;
				throw new NoSuchElementException();
			}
			validNext = false;
			current = new Entry(currentIndex++);
			return current;
		}

		@Override
		public void remove() {
			if (current == null) {
				throw new IllegalStateException();
			}
			ArrayListMap.this.remove(current.getIntKey());
		}
		
	}
	
	private final class EntrySet extends AbstractSet<java.util.Map.Entry<Integer, V>> {
		
		public EntrySet() {
		}

		@Override
		public Iterator<java.util.Map.Entry<Integer, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return ArrayListMap.this.size;
		}
		
	}
	
	private final DirectAccessArrayList<V> directAccessList;
	
	private int size;
	
	// view
	private transient EntrySet entrySet;
	
	
	public ArrayListMap(V[] elementData) {
		directAccessList = new DirectAccessArrayList<V>(elementData);
	}

	@Override
	public Set<java.util.Map.Entry<Integer, V>> entrySet() {
		EntrySet result = this.entrySet;
		if (result == null) {
			result = new EntrySet();
			this.entrySet = result;
		}
		return result;
	}
	
	@Override
	public V get(int key) {
		V result;
		if ((key >= 0) && (key < directAccessList.size())) {
			result = directAccessList.get(key);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public V get(Object key) {
		return this.get(((Integer) key).intValue());
	}

	@Override
	public V put(Integer key, V value) {
		return this.put(((Integer) key).intValue(), value);
	}

	@Override
	public V put(int key, V value) {
		if (key < 0) {
			throw new IllegalArgumentException("negative keys not supported");
		}
		if (key >= directAccessList.size()) {
			directAccessList.setSize(key + 10);
		}
		V result = directAccessList.set(key, value);
		if (value != null) {
			if (result == null) {
				size++;
			}
		} else if (result != null) {
			size--;
		}
		return result;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public V remove(Object key) {
		return this.remove(((Integer) key).intValue());
	}

	@Override
	public V remove(int key) {
		return this.put(key, null);
	}

}
