package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public class CounterSkill extends CostRequiringSkill implements DeepCopyable<Skill>
{
    public CounterSkill(CounterSkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final CounterSkillData BaseInfo = (CounterSkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public CounterSkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected CounterSkill DeepCopyInternally() { return (CounterSkill)super.DeepCopyInternally(); }
    //End Protected Methods
}
