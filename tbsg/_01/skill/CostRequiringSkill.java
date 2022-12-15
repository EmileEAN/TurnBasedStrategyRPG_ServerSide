package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public abstract class CostRequiringSkill extends ActiveSkill implements DeepCopyable<Skill>
{
    public CostRequiringSkill(CostRequiringSkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final CostRequiringSkillData BaseInfo = (CostRequiringSkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public CostRequiringSkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected CostRequiringSkill DeepCopyInternally() { return (CostRequiringSkill)super.DeepCopyInternally(); }
    //End Protected Methods
}