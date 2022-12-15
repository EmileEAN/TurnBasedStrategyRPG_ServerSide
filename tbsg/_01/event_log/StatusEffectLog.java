package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main.extension_method.StringExtension;

public abstract class StatusEffectLog extends AutomaticEventLog
{
    public StatusEffectLog(BigDecimal _eventTurn,
        int _effectHolderId, String _effectHolderName, String _effectHolderNickname)
    {
    	super(_eventTurn);
    	
        EffectHolderId = _effectHolderId;
        EffectHolderName = StringExtension.CoalesceNull(_effectHolderName);
        EffectHolderNickname = StringExtension.CoalesceNull(_effectHolderNickname);
    }

    public final int EffectHolderId;
    public final String EffectHolderName;
    public final String EffectHolderNickname;
}
