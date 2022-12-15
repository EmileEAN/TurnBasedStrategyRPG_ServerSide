package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main.extension_method.StringExtension;
import eean_games.tbsg._01.animationInfo.AnimationInfo;

public abstract class EffectTrialLog_UnitTargetingEffect extends EffectTrialLog
{
    public EffectTrialLog_UnitTargetingEffect(BigDecimal _eventTurn, AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed,
        int _targetId, String _targetName, String _targetNickname, int _targetLocationTileIndex)
    {
    	super(_eventTurn, _animationInfo, _isDiffused,_didActivate, _didSucceed);
    	
        TargetId = _targetId;
        TargetName = StringExtension.CoalesceNull(_targetName);
        TargetNickname = StringExtension.CoalesceNull(_targetNickname);
        TargetLocationTileIndex = _targetLocationTileIndex;
    }

    //Public Read-only Fields
    public final int TargetId;
    public final String TargetName;
    public final String TargetNickname;
    public final int TargetLocationTileIndex;
    //End Public Read-only Fields
}
