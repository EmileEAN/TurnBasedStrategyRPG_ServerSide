package eean_games.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import eean_games.main.extension_method.ObjectExtension;

public class Linq 
{
	//Linq for List<T>
	public static <T, U> List<U> ofType(List<T> _list, Class<U> _class) { return (_list != null && _class != null) ? _list.stream().filter(x -> _class.isInstance(x)).map(x -> ObjectExtension.ToT(x, _class)).collect(Collectors.toList()) : new ArrayList<U>(); }
	public static <T, U> List<U> cast(List<T> _list, Class<U> _class) { return (_list != null && _class != null) ? _list.stream().map(x -> ObjectExtension.ToT(x, _class)).collect(Collectors.toList()) : null; }
	public static <T> boolean containsAny(List<T> _list, List<T> _targetList) { return !Collections.disjoint(_list, _targetList); }
	public static <T> boolean any(List<T> _list, Predicate<? super T> _predicate) { return (_list != null && _predicate != null) ? _list.stream().anyMatch(_predicate) : false; }
	public static <T> List<T> where(List<T> _list, Predicate<? super T> _predicate) { return (_list != null && _predicate != null) ? _list.stream().filter(_predicate).collect(Collectors.toList()) : _list; }
	public static <T, U extends Comparable<? super U>> List<T> orderBy(List<T> _list, Function<? super T,? extends U> _keyExtractor) { return (_list != null && _keyExtractor != null) ? _list.stream().sorted(Comparator.comparing(_keyExtractor)).collect(Collectors.toList()) : _list; }
	public static <T, U extends Comparable<? super U>> List<T> orderByDescending(List<T> _list, Function<? super T,? extends U> _keyExtractor) 
	{ 
		if (_list != null && _keyExtractor != null)
			return _list;
		
		List<T> result = orderBy(_list, _keyExtractor);
		Collections.reverse(result);
		return result;
	}
	@SuppressWarnings("unchecked")
	public static <T> List<T> orderByMultipleConditions(List<T> _list, List<KeyExtractorAndSortType<T>> _conditions)
	{
		if (_list == null || _list.size() < 1
			||_conditions == null || _conditions.size() < 1)
		{
			return _list;	
		}
		
		/*
		 * listsPerKeyValues will store the sublists of _list that will be created.
		 * Each sublist will contain the same value for the given key each time a sorting process is applied.
		 * The sublists will be subdivided again and again until all sorting conditions have been applied.
		 * The eventual sublists will be merged altogether, in the order, to form a single list with sorted values.
		 */
        List<List<T>> listsPerKeyValue = new ArrayList<List<T>>();
        listsPerKeyValue.add(new ArrayList<T>(_list));

        for (int i = 0; i < _conditions.size(); i++)
        {
            List<List<T>> tmp_lists = new ArrayList<List<T>>();
            for (List<T> subList : listsPerKeyValue)
            {
                List<T> orederedList = orderByCondition(subList, _conditions.get(i));
                tmp_lists.addAll(subdivideBy(orederedList, _conditions.get(i).keyExtractor));
            }
            
            listsPerKeyValue = tmp_lists; //Update listsPerKeyValue
        }
        
        List<T> result = new ArrayList<T>();
        for (List<T> subList : listsPerKeyValue) //Merge the sub-lists
        {
            result.addAll(subList);
        }
        
        return result;
    }
    @SuppressWarnings("unchecked")
	public static <T> List<T> orderByCondition(List<T> _list, KeyExtractorAndSortType<T> _condition)
    {
    	if (_list == null
    		||_condition == null 
    		|| _condition.keyExtractor == null 
    		|| _condition.sortType == null)
    	{
    		return _list;
    	}
    	
        if (_condition.sortType == eSortType.Ascending)
            return orderBy(_list, _condition.keyExtractor);
        else
            return orderByDescending(_list, _condition.keyExtractor);
    }
    public static <T, U extends Comparable<? super U>> List<List<T>> subdivideBy(List<T> _list, Function<? super T,? extends U> _keyExtractor) 
	{ 
    	if (_list == null)
    		return null;
    	
	    List<T> list = new ArrayList<T>(_list);
	    
	    List<List<T>> listsPerKeyValue = new ArrayList<List<T>>();
    	if (_keyExtractor == null)
    	{
    		listsPerKeyValue.add(list);
    		return listsPerKeyValue;
    	}
	    
	    boolean firstLoop = true;
	    
	    U previousKeyValue = _keyExtractor.apply(firstOrDefault(list)); //Initialize with the key value for the first item in the list
	    for (T item : list)
	    {
	        U currentKeyValue = _keyExtractor.apply(item);
	        
	        if (firstLoop || !currentKeyValue.equals(previousKeyValue))
	        {
	            listsPerKeyValue.add(new ArrayList<T>());
	            
	            if (firstLoop)
	                firstLoop = false;
	        }
	        
	        last(listsPerKeyValue).add(item);
	        
	        previousKeyValue = currentKeyValue;
	    }
	    
	    return listsPerKeyValue;
	}
	public static <T> List<T> distinct(List<T> _list) { return (_list != null) ? _list.stream().distinct().collect(Collectors.toList()) : null; }
	public static <T> T first(List<T> _list) { return _list.get(0); }
	public static <T> T first(List<T> _list, Predicate<? super T> _predicate) { return _list.stream().filter(_predicate).findFirst().get(); }
	public static <T> T firstOrDefault(List<T> _list) { return (_list != null && _list.size() > 0) ? _list.get(0) : null; }
    public static <T> T firstOrDefault(List<T> _list, Predicate<? super T> _predicate) { return (_list != null && _list.size() > 0) ? _list.stream().filter(_predicate).findFirst().orElse(null) : null;}
    public static <T> T last(List<T> _list) { return _list.get(_list.size() - 1); }
	public static <T> T last(List<T> _list, Predicate<? super T> _predicate) 
	{ 
		List<T> filteredList = _list.stream().filter(_predicate).collect(Collectors.toList());
		return last(filteredList);
	}
	public static <T> T lastOrDefault(List<T> _list) { return (_list != null && _list.size() > 0) ? _list.get(_list.size() - 1) : null; }
    public static <T> T lastOrDefault(List<T> _list, Predicate<? super T> _predicate) 
    {
    	if (_list == null || _list.size() < 1)
    		return null;
    	
    	List<T> filteredList = _list.stream().filter(_predicate).collect(Collectors.toList());
    	return lastOrDefault(filteredList);
    }

    //Linq for Map<T, U>
    public static <T, U, K, V> Map<K, V> cast(Map<T, U> _map, Class<K> _keyClass, Class<V> _valueClass) 
    {
    	if (_map != null && _keyClass != null && _valueClass != null)
    		return null;
    	
    	Map<K, V> result = new HashMap<K, V>();
    	
    	try 
    	{
        	for (Map.Entry<T, U> entry : _map.entrySet())
        	{
        		K key = _keyClass.cast(entry.getKey());
        		V value = _valueClass.cast(entry.getValue());
        		
        		result.put(key, value);
        	}	
        	
        	return result;
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex);
    		return null;
    	}
    }
}
