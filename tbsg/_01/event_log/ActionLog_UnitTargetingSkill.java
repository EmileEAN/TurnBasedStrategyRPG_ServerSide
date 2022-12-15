package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.TargetInfo;

public class ActionLog_UnitTargetingSkill extends ActionLog_Skill
{
    public ActionLog_UnitTargetingSkill(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname, String _skillName, int _actorLocationTileIndex, int _animationId,
        List<TargetInfo> _targetsName_Nickname_OwnerName, List<TargetInfo> _secondaryTargetsName_Nickname_OwnerName)
    {
    	super(_actionTurn, _actorId, _actorName, _actorNickname, _skillName, _actorLocationTileIndex, _animationId);
    	
        m_targetsName_Nickname_OwnerName = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetsName_Nickname_OwnerName, eCopyType.Deep);
        m_secondaryTargetsName_Nickname_OwnerName = ListExtension.CoalesceNullAndReturnCopyOptionally(_secondaryTargetsName_Nickname_OwnerName,  eCopyType.Deep);
    }

    //Getters
    public List<TargetInfo> getTargetsName_Nickname_OwnerName() { return Collections.unmodifiableList(m_targetsName_Nickname_OwnerName); }
    public List<TargetInfo> getSecondaryTargetsName_Nickname_OwnerName() { return Collections.unmodifiableList(m_secondaryTargetsName_Nickname_OwnerName); }
    //End Getters

    //Private Read-only Fields
    private final List<TargetInfo> m_targetsName_Nickname_OwnerName;
    private final List<TargetInfo> m_secondaryTargetsName_Nickname_OwnerName;
    //End Private Read-only Fields
}

