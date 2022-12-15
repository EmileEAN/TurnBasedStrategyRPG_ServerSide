package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.enumerable.eEffectiveness;

public class EffectTrialLog_DamageEffect extends EffectTrialLog_UnitTargetingEffect
{
    public EffectTrialLog_DamageEffect(BigDecimal _eventTurn, AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed, int _targetId, String _targetName, String _targetNickname, int _targetLocationTileIndex,
        boolean _wasImmune, boolean _wasCritical, eEffectiveness _effectiveness, int _value, int _remainingHPAfterModification)
    {
    	super(_eventTurn, _animationInfo, _isDiffused, _didSucceed,_didActivate, _targetId, _targetName, _targetNickname, _targetLocationTileIndex);
    	
        WasImmune = _wasImmune;
        WasCritical = _wasCritical;
        Effectiveness = _effectiveness;

        Value = _value;
        RemainingHPAfterModification = _remainingHPAfterModification;
    }

    //Public Read-only Fields
    public final boolean WasImmune;
    public final boolean WasCritical;
    public final eEffectiveness Effectiveness;

    public final int Value;
    public final int RemainingHPAfterModification;
    //End Public Read-only Fields
}