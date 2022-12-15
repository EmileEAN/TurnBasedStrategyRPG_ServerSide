package eean_games.tbsg._01;

import java.util.Collections;
import java.util.List;

import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.enumerable.eTileType;

public class TileSet 
{
	public TileSet(List<eTileType> _tileTypes)
	{
		tileTypes = ListExtension.CoalesceNullAndReturnCopyOptionally(_tileTypes, eCopyType.Shallow);
	}
	
	//Getters
	public List<eTileType> getTileTypes() { return Collections.unmodifiableList(tileTypes); }
	//End Getters
	
	//Private Read-only Fields
	private final List<eTileType> tileTypes;
	//End Private Read-only Fields
}
