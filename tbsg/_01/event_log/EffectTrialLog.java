package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.tbsg._01.animationInfo.AnimationInfo;

public abstract class EffectTrialLog extends AutomaticEventLog
{
    public EffectTrialLog(BigDecimal _eventTurn,
    		AnimationInfo _animationInfo, boolean _isDiffused, boolean _didActivate, boolean _didSucceed)
    {
    	super(_eventTurn);
    	
    	AnimationInfo = _animationInfo;
    	IsDiffused = _isDiffused;
    	
    	DidActivate = _didActivate;
        DidSucceed = _didSucceed;
    }

    //Public Read-only Fields
    public final AnimationInfo AnimationInfo;
    public final boolean IsDiffused;
    
    public final boolean DidActivate;
    public final boolean DidSucceed;
    //End Public Read-only Fields
}
