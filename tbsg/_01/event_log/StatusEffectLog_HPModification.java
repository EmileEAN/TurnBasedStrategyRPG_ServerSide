package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

public class StatusEffectLog_HPModification extends StatusEffectLog
{
    public StatusEffectLog_HPModification(BigDecimal _eventTurn, int _effectHolderId, String _effectHolderName, String _effectHolderNickname,
        boolean _isPositive, int _value, int _remainingHPAfterModification)
    {
    	super(_eventTurn, _effectHolderId, _effectHolderName, _effectHolderNickname);
    	
        IsPositive = _isPositive;
        Value = _value;
        RemainingHPAfterModification = _remainingHPAfterModification;
    }

    //Public Read-only Fields
    public final boolean IsPositive; // If true, Value represents the amout healed. If false, Value represents the damage dealt. Both healing and damaging value can be zero.
    public final int Value;
    public final int RemainingHPAfterModification;
    //End Public Read-only Fields
}
