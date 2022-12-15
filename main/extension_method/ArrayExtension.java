package eean_games.main.extension_method;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;

public class ArrayExtension {

	@SuppressWarnings("unchecked")
	private static <T> T[] ShallowCopy(T[] _array, Class<T> _class)
	{
		if(_array == null || _class == null)
			return null;
		
		T[] copy = (T[])Array.newInstance(_class, _array.length);
		
		for (int i = 0; i < copy.length; i++)
		{
			copy[i] = _array[i];
		}
		
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] DeepCopy(T[] _array, Class<T> _class)
    {
		if (_array == null || _class == null)
			return null;
		
        T[] copy = (T[])Array.newInstance(_class, 0);

        if (_array.length != 0)
        {
        	copy = (T[])Array.newInstance(_class, _array.length);
            
            for (int i = 0; i < _array.length; i ++)
            {
            	if (_array[i] instanceof DeepCopyable
            		|| _array[i].getClass().isArray()
            		|| _array[i] instanceof List
            		|| _array[i] instanceof Map)
            	{
            		copy[i] = (T)ObjectExtension.DeepCopy(_array[i]);
            	}
            	else /*It is an immutable object*/ { copy[i] = _array[i]; }
            }
        }
        
        return copy;
    }
	
    @SuppressWarnings("unchecked")
	public static <T> T[] CoalesceNullAndReturnCopyOptionally(T[] _array, eCopyType _copyType, Class<T> _class)
    {
        if (_array != null && _copyType != null && _class != null)
        {
            switch (_copyType)
            {
                case Shallow:
                    return ShallowCopy(_array, _class);
                case Deep:
                    return DeepCopy(_array, _class);
                default: //eCopyType.None
                    return _array;
            }
        }
        else
            return (T[])Array.newInstance(_class, 0);
    }
	
    public static byte[] CoalesceNullAndReturnCopyOptionally(byte[] _array, boolean _returnCopy)
    {
    	if (_array != null)
    	{
    		if (_returnCopy)
    			return Arrays.copyOf(_array, _array.length);
    		else
    			return _array;
    	}
    	
    	return new byte[0];
    }
}
