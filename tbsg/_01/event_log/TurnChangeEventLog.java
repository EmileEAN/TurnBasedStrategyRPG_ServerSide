package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

public class TurnChangeEventLog extends AutomaticEventLog
{
    public TurnChangeEventLog(BigDecimal _eventTurn,
        int _turnEndingPlayerId, int _turnInitiatingPlayerId)
    {
    	super(_eventTurn);
    	
        TurnEndingPlayerId = _turnEndingPlayerId;
        TurnInitiatingPlayerId = _turnInitiatingPlayerId;
    }

    //Public Read-only Fields
    public final int TurnEndingPlayerId;
    public final int TurnInitiatingPlayerId;
    //End Public Read-only Fields
}
