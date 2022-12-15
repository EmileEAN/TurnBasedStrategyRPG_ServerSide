package eean_games.tbsg._01.extension_method;

public class StringExtension 
{
    @SuppressWarnings("rawtypes")
	public static Class ToCorrespondingEnumType(String _string)
    {
    	if (_string == null || _string == "")
    		return null;
    	
        try
        {
            String nameSpace = "eean_games.tbsg._01.enumerable;";

            Class result = Class.forName(nameSpace + "." + _string);
            
            if (result.isEnum())
            	return result;
            
            return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object ToCorrespondingEnumValue(String _string, String _enumClassString)
    {
    	if (_string == null || _string == ""
    		|| _enumClassString == null || _enumClassString == "")
    	{
    		return null;
    	}
    	
        try
        {
            Class enumClass = ToCorrespondingEnumType(_enumClassString);
            
            return Enum.valueOf(enumClass, _string);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
