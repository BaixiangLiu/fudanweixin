package edu.fudan.eservice.common.utils;

import java.io.Serializable;

public class Pair<K,V> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7555161890952853994L;
	private K key;
	private V value;
	
	public  Pair (K key,V value)
	{
		this.key = key;
		this.value=value;
	}
	
	public Pair ()
	{
		
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Pair: "+key+":"+value;
	}
}
