package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

public abstract class EventLog
{
    public EventLog(BigDecimal _eventTurn)
    {
        EventTurn = _eventTurn;
    }

    //Public Read-only Fields
    public final BigDecimal EventTurn;
    //End Public Read-only Fields
}
