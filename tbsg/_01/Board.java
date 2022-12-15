package eean_games.tbsg._01;

import java.util.List;

import eean_games.tbsg._01.enumerable.eTileType;

public class Board
{
    /// <summary>
    /// Ctor
    /// PreCondition: _tileSet.Count > 0;
    /// PostCondition: All eTileType properties of Sockets in the Board will have an eTileType value assigned. The eTileType value for each socket will be randomly selected from those in _tileSet.
    /// </summary>
    /// <param name="_tileSet"></param>
    public Board(List<eTileType> _tileSet)
    {
        Sockets = new Socket[CoreValues.SIZE_OF_A_SIDE_OF_BOARD][CoreValues.SIZE_OF_A_SIDE_OF_BOARD];

        List<eTileType> tileTypes = TileFunctions.GetRandomTileType(CoreValues.SIZE_OF_A_SIDE_OF_BOARD * CoreValues.SIZE_OF_A_SIDE_OF_BOARD, _tileSet);

        for (int x = 1; x <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; x++)
        {
            for(int y = 1; y <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; y++)
            {
            	eTileType tileType = tileTypes.get(CoreValues.SIZE_OF_A_SIDE_OF_BOARD * (x - 1) + (y - 1));
                Sockets[x - 1][y - 1] = new Socket(tileType);
            }
        }
    }

    
    //Public Fields
    public Socket[][] Sockets;
    //End Public Fields
} 
