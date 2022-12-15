package eean_games.tbsg._01.event_log;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import eean_games.main._2DCoord;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;

public class ActionLog_TileTargetingSkill extends ActionLog_Skill
{
    public ActionLog_TileTargetingSkill(BigDecimal _actionTurn, int _actorId, String _actorName, String _actorNickname, String _skillName, int _actorLocationTileIndex, int _animationId,
        List<_2DCoord> _targetCoords, List<_2DCoord> _secondaryTargetCoords)
    {
    	super(_actionTurn, _actorId, _actorName, _actorNickname, _skillName, _actorLocationTileIndex, _animationId);
    	
        m_targetCoords = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetCoords, eCopyType.Deep);
        m_secondaryTargetCoords = ListExtension.CoalesceNullAndReturnCopyOptionally(_secondaryTargetCoords, eCopyType.Deep);
    }

    //Getters
    public List<_2DCoord> getTargetCoords() { return Collections.unmodifiableList(m_targetCoords); }
    public List<_2DCoord> getSecondaryTargetCoords() { return Collections.unmodifiableList(m_secondaryTargetCoords); }
    //End Getters

    //Private Read-only Fields
    private final List<_2DCoord> m_targetCoords;
    private final List<_2DCoord> m_secondaryTargetCoords;
    //End Private Read-only Fields
}
