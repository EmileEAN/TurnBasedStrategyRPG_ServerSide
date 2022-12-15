package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public class UltimateSkill extends ActiveSkill implements DeepCopyable<Skill>
{
    public UltimateSkill(UltimateSkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final UltimateSkillData BaseInfo = (UltimateSkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public UltimateSkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected UltimateSkill DeepCopyInternally() { return (UltimateSkill)super.DeepCopyInternally(); }
    //End Protected Methods
}
