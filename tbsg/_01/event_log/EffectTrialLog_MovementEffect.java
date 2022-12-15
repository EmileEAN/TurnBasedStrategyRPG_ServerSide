package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main._2DCoord;
import eean_games.main.extension_method._2DCoordExtension;
import eean_games.tbsg._01.animationInfo.MovementAnimationInfo;

public class EffectTrialLog_MovementEffect extends EffectTrialLog_TileTargetingEffect
{
    public EffectTrialLog_MovementEffect(BigDecimal _eventTurn, MovementAnimationInfo _animationInfo, boolean _didActivate, _2DCoord _targetCoord,
    		_2DCoord _initialCoord)
    {
    	super(_eventTurn, _animationInfo, false,_didActivate, true, _targetCoord);
    	
    	initialCoord = _2DCoordExtension.CoalesceNullAndReturnCopyOptionally(_initialCoord, true);
    }

    //Public Read-only Fields
    public final _2DCoord initialCoord;
    //End Public Read-only Fields
}
