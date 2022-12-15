package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;

import eean_games.main.extension_method.StringExtension;

public abstract class ActionLog_Skill extends ActionLog
{
    public ActionLog_Skill(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname,
        String _skillName, int _actorLocationTileIndex, int _animationId)
    {
    	super(_actionTurn, _actorId, _actorName, _actorNickname);
    	
        SkillName = StringExtension.CoalesceNull(_skillName);

        ActorLocationTileIndex = _actorLocationTileIndex;

        AnimationId = _animationId;
    }

    //Public Read-only Fields
    public final String SkillName;

    public final int ActorLocationTileIndex;

    public final int AnimationId;
    //End Public Read-only Fields
}
