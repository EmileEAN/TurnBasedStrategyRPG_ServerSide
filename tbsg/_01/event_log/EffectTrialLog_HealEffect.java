package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.tbsg._01.animationInfo.AnimationInfo;

public class EffectTrialLog_HealEffect extends EffectTrialLog_UnitTargetingEffect
{
    public EffectTrialLog_HealEffect(BigDecimal _eventTurn, AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed, int _targetId, String _targetName, String _targetNickname, int _targetLocationTileIndex,
        boolean _wasCritical, int _value, int _remainingHPAfterModification)
    {
    	super(_eventTurn, _animationInfo, _isDiffused,_didActivate, _didSucceed, _targetId, _targetName, _targetNickname, _targetLocationTileIndex);
    	
        WasCritical = _wasCritical;

        Value = _value;
        RemainingHPAfterModification = _remainingHPAfterModification;
    }

    //Public Read-only Fields
    public boolean WasCritical;

    public int Value;
    public int RemainingHPAfterModification;
    //End Public Read-only Fields
}