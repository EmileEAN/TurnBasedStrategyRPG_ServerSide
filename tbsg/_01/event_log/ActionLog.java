package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main.extension_method.StringExtension;

public abstract class ActionLog extends EventLog
{
    public ActionLog(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname)
    {
    	super(_actionTurn);
    	
        ActorId = _actorId;
        ActorName = StringExtension.CoalesceNull(_actorName);
        ActorNickname = StringExtension.CoalesceNull(_actorNickname);
    }

    //Public Read-only Fields
    public final int ActorId;
    public final String ActorName;
    public final String ActorNickname;
    //End Public Read-only Fields
}
