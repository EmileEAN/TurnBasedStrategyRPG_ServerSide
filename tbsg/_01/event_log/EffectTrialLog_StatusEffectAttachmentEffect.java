package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.tbsg._01.animationInfo.AnimationInfo;

public class EffectTrialLog_StatusEffectAttachmentEffect extends EffectTrialLog_UnitTargetingEffect
{
    public EffectTrialLog_StatusEffectAttachmentEffect(BigDecimal _eventTurn, AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed, int _targetId, String _targetName, String _targetNickname, int _targetLocationTileIndex,
        int _attachedStatusEffectId)
    {
    	super(_eventTurn, _animationInfo, _isDiffused, _didSucceed,_didActivate, _targetId, _targetName, _targetNickname, _targetLocationTileIndex);
    	
    	AttachedStatusEffectId = _attachedStatusEffectId;
    }

    //Public Read-only Fields
    public final int AttachedStatusEffectId;
    //End Public Read-only Fields
}