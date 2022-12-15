package eean_games.main.extension_method;

import java.util.List;
import java.util.Map;

import eean_games.main.*;

public class ObjectExtension 
{
	public static <T> T ToT(Object _object, Class<T> _class)
    {
        if (_class.isInstance(_object))
        	return _class.cast(_object);
		
        return null;
    }
	
    ///Returns a deep copy of _object if possible. Otherwise, return null.
    public static Object DeepCopy(Object _object)
    {
    	if (_object == null)
    		return null;
    	
        if (_object instanceof DeepCopyable)
        {
        	DeepCopyable<?> deepCopyableObject = (DeepCopyable<?>)_object;
        	return deepCopyableObject.DeepCopy();
        }
        else if (_object.getClass().isArray()) { return ArrayExtension.DeepCopy((Object[])_object, Object.class); }
        else if (_object instanceof List<?>) { return ListExtension.DeepCopy((List<?>)_object); }
        else if (_object instanceof Map<?, ?>) { return MapExtension.DeepCopy((Map<?, ?>)_object); }

        return null;
    }
}
