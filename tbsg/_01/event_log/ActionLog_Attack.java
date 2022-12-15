package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

public class ActionLog_Attack extends ActionLog
{
    public ActionLog_Attack(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname,
        int _actorLocationTileIndex, int _targetId, int _targetLocationTileIndex)
    {
    	super(_actionTurn, _actorId, _actorName, _actorNickname);
    	
        ActorLocationTileIndex = _actorLocationTileIndex;
        TargetId = _targetId;
        TargetLocationTileIndex = _targetLocationTileIndex;
    }

    //Public Read-only Fields
    public final int ActorLocationTileIndex;
    public final int TargetId;
    public final int TargetLocationTileIndex;
    //End Public Read-only Fields
}