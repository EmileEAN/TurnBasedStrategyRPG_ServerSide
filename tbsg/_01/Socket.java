package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.List;

import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.unit.UnitInstance;

public class Socket
{
    public Socket(eTileType _tileType)
    {
        TileType = _tileType;
        Unit = null;
        TrapEffects = new ArrayList<Effect>();
    }

    //Public Fields
    public eTileType TileType;
    public UnitInstance Unit;
    //End Public Fields
    
    //Public Read-only Fields
    public final List<Effect> TrapEffects; //List elements are modifiable
    //End Public Read-only Fields
}
