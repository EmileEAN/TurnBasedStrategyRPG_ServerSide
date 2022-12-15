package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main._2DCoord;
import eean_games.main.extension_method._2DCoordExtension;
import eean_games.tbsg._01.animationInfo.AnimationInfo;

public abstract class EffectTrialLog_TileTargetingEffect extends EffectTrialLog
{
    public EffectTrialLog_TileTargetingEffect(BigDecimal _eventTurn, AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed,
        _2DCoord _targetCoord)
    {
    	super(_eventTurn, _animationInfo, _isDiffused,_didActivate, _didSucceed);
    	
    	targetCoord = _2DCoordExtension.CoalesceNullAndReturnCopyOptionally(_targetCoord, true);
    }

    //Public Read-only Fields
    public final _2DCoord targetCoord;
    //End Public Read-only Fields
}
