package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public class PassiveSkill extends Skill implements DeepCopyable<Skill>
{
    public PassiveSkill(PassiveSkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final PassiveSkillData BaseInfo = (PassiveSkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public PassiveSkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected PassiveSkill DeepCopyInternally() { return (PassiveSkill)super.DeepCopyInternally(); }
    //End Protected Methods
}
