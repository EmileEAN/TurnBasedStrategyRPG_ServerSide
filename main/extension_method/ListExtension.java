package eean_games.main.extension_method;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;

public class ListExtension 
{	
    @SuppressWarnings("unchecked")
	public static <T> List<T> DeepCopy(List<T> _list)
    {
    	if (_list == null)
    		return null;
    	
        List<T> copy = new ArrayList<T>();

        if (_list.size() != 0)
        {
        	for (T item : _list)
        	{
        		if (item instanceof DeepCopyable
        			|| item.getClass().isArray()
        			|| item instanceof List
        			|| item instanceof Map)
        		{
        			copy.add((T)ObjectExtension.DeepCopy(item));
        		}
        		else /*It is an immutable object*/ { copy.add(item); }
        	}
        }

        return copy;
    }
    
    public static <T> List<T> CoalesceNullAndReturnCopyOptionally(List<T> _list, eCopyType _copyType)
    {
        if (_list != null && _copyType != null)
        {
            switch (_copyType)
            {
                case Shallow:
                    return new ArrayList<T>(_list);
                case Deep:
                    return DeepCopy(_list);
                default: //eCopyType.None
                    return _list;
            }
        }
        else
            return new ArrayList<T>();
    }
}
