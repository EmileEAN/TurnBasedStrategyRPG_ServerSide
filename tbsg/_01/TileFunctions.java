package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.List;

import eean_games.main.MTRandom;
import eean_games.tbsg._01.enumerable.eTileType;

public class TileFunctions 
{
	public static List<eTileType> GetRandomTileType(int _numberOfTiles, List<eTileType> _tileSet)
    {
        try
        {
            if (_numberOfTiles < 1)
            {
                System.out.println("The number of tiles to be created must be greater than 0.");
                return null;
            }

            if(_tileSet.size() < 1)
            {
                System.out.println("_tileSet must have at least one element.");
                _tileSet.add(eTileType.Normal);
            }

            List<eTileType> tmp = new ArrayList<eTileType>();

            MTRandom.randInit();

            for (int i = 1; i <= _numberOfTiles; i++)
            {
                int index = MTRandom.getRand(0, _tileSet.size() - 1);
                tmp.add(_tileSet.get(index));
            }

            return tmp;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
