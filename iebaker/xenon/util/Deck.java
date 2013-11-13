package iebaker.xenon.util;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Deck is a class which maintains multiple sets of instances of T, grouped by their subclass
 */
public class Deck<T> {
	private java.util.Map<Class<?>, java.util.ArrayList<T>> _MAP = new HashMap<Class<?>, java.util.ArrayList<T>>();

	private java.util.ArrayList<T> GET(Class<?> c) {
		return _MAP.get(c);
	}

	private boolean HAS_CLASS(Class<?> c) {
		return _MAP.containsKey(c);
	}

	private boolean HAS_CLASS_OF(T t) {
		return _MAP.containsKey(t.getClass());
	}

	private void ADD_CLASS_OF(T t) {
		_MAP.put(t.getClass(), new ArrayList<T>());
	}

	private boolean ADD(T t) {
		return _MAP.get(t.getClass()).add(t);
	}

	private java.util.ArrayList<T> LIKE(T t) {
		return _MAP.get(t.getClass());
	}

	private boolean REMOVE(T t) {
		return _MAP.get(t.getClass()).remove(t);
	}

	private java.util.Set<Class<?>> CLASSES() {
		return _MAP.keySet();
	}

	private int size() {
		int acc = 0;
		for(Class<?> c : CLASSES()) {
			acc += GET(c).size();
		}
		return acc;
	}

	public boolean isEmpty() {
		for(Class<?> c : CLASSES()) {
			if(!GET(c).isEmpty()) return false;
		}
		return true;
	}

	public boolean add(T t) {
		if(!HAS_CLASS_OF(t)) {
			ADD_CLASS_OF(t);
		}
		return ADD(t);
	}

	public boolean remove(T t) {
		if(!HAS_CLASS_OF(t)) {
			return false;
		}
		return REMOVE(t);
	}

	public boolean contains(T t) {
		if(!HAS_CLASS_OF(t)) {
			return false;
		}
		return LIKE(t).contains(t);
	}

	public java.util.ArrayList<T> all() {
		java.util.ArrayList<T> ret = new ArrayList<T>();
		for(Class<?> c : CLASSES()) {
			for(T t : GET(c)) {
				ret.add(t);
			}
		}
		return ret;
	}

	public java.util.ArrayList<T> ofClass(Class<?> c) {
		return GET(c);
	}

	public boolean hasClass(Class<?> c) {
		return HAS_CLASS(c);
	}

	public Object[] toArray() {
		Object[] arr = new Object[this.size()];
		int index = 0;
		for(Class<?> c : CLASSES()) {
			for(Object o : GET(c)) {
				arr[index] = o;
				++index;
			}
		}
		return arr;
	}
}