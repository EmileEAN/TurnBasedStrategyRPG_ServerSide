package eean_games.main.extension_method;

import java.sql.Blob;
import java.util.Arrays;

public class BlobExtension 
{
	public static Byte[] ToBytes(Blob _blob)
	{
		if (_blob == null)
			return null;
		
		try 
		{
			Byte[] result = new Byte[(int)(_blob.length())];
			
	    	byte[] tmp_imageAsBytes = _blob.getBytes(1l, (int)(_blob.length()));
	    	Arrays.setAll(result, n -> tmp_imageAsBytes[n]);
	    	
	    	return result;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}
}
