package eean_games.main.extension_method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;

public class MapExtension 
{
	@SuppressWarnings({ "unchecked" })
	public static <T, U> Map<T, U> DeepCopy(Map<T, U> _map)
    {
		if (_map == null)
			return null;
		
        Map<T, U> copy = new HashMap<T, U>();
        
        if (_map.size() != 0)
        {
        	for (Map.Entry<T, U> entry : _map.entrySet())
        	{
                T key = entry.getKey();
                U value = entry.getValue();
        		
        		if (key instanceof DeepCopyable
            			|| key.getClass().isArray()
            			|| key instanceof List
            			|| key instanceof Map)
        		{
        			key = ((T)ObjectExtension.DeepCopy(key));
        		}
        		/*else == It is an immutable object and, hence, let key have the raw data*/
        	
        		if (value instanceof DeepCopyable
            			|| value.getClass().isArray()
            			|| value instanceof List
            			|| value instanceof Map)
        		{
    				value = ((U)ObjectExtension.DeepCopy(value));
        		}
            	/*else == It is an immutable object and, hence, let value have the raw data*/
        	}
        }
        
        return copy;
    }
	
	public static <T, U> Map<T, U> CoalesceNullAndReturnCopyOptionally(Map<T, U> _map, eCopyType _copyType)
    {
        if (_map != null && _copyType != null)
        {
            switch (_copyType)
            {
                case Shallow:
                    return new HashMap<T, U>(_map);
                case Deep:
                    return DeepCopy(_map);
                default:
                    return _map;
            }
        }
        else
            return new HashMap<T, U>();
    }
}
