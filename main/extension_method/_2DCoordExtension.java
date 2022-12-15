package eean_games.main.extension_method;

import eean_games.main._2DCoord;
import eean_games.tbsg._01.CoreValues;

public class _2DCoordExtension 
{
	public static int ToIndex(_2DCoord _coord) { return CoreValues.SIZE_OF_A_SIDE_OF_BOARD * _coord.Y + _coord.X; }

	public static _2DCoord CoalesceNullAndReturnCopyOptionally(_2DCoord _coord, boolean _returnCopyInstead)
    {
        if (_coord != null)
        {
            if (_returnCopyInstead)
                return _coord.DeepCopy();
            else
                return _coord;
        }
        else
            return new _2DCoord(0, 0);
    }
}
