package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main._2DCoord;
import eean_games.main.extension_method._2DCoordExtension;

public class ActionLog_Move extends ActionLog
{
    public ActionLog_Move(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname,
        _2DCoord _initialCoord, _2DCoord _eventualCoord)
    {
    	super(_actionTurn, _actorId, _actorName, _actorNickname);
    	
        InitialCoord = _2DCoordExtension.CoalesceNullAndReturnCopyOptionally(_initialCoord, true);
        EventualCoord = _2DCoordExtension.CoalesceNullAndReturnCopyOptionally(_eventualCoord, true);
    }

    //Public Read-only Fields
    public final _2DCoord InitialCoord;
    public final _2DCoord EventualCoord;
    //End Public Read-only Fields
}
