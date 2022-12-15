package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public class OrdinarySkill extends CostRequiringSkill implements DeepCopyable<Skill>
{
    public OrdinarySkill(OrdinarySkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final OrdinarySkillData BaseInfo = (OrdinarySkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public OrdinarySkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected OrdinarySkill DeepCopyInternally() { return (OrdinarySkill)super.DeepCopyInternally(); }
    //End Protected Methods
}
