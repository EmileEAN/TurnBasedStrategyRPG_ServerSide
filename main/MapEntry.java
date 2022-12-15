package eean_games.main;

public class MapEntry<K, V>
{
	public MapEntry(K _key, V _value)
	{
		key = _key;
		value = _value;
	}
	
	//Getters
	public K getKey() { return key; }
	public V getValue() { return value; }
	//End Getters
	
	//Private Fields
	private K key;
	private V value;
	//End Private Fields
}
