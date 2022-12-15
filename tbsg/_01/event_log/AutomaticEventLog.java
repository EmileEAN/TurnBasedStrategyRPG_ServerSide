package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

public abstract class AutomaticEventLog extends EventLog
{
    public AutomaticEventLog(BigDecimal _eventTurn)
    {
    	super(_eventTurn);
    }
}
