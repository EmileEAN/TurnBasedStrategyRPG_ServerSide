package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;

public abstract class ActiveSkill extends Skill implements DeepCopyable<Skill>
{
    public ActiveSkill(ActiveSkillData _skillData, int _level)
    {
    	super(_skillData, _level);
    }

    //Public Read-only Fields
    public final ActiveSkillData BaseInfo = (ActiveSkillData)super.BaseInfo;
    //End Public Read-only Fields

    //Public Methods
    public ActiveSkill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected ActiveSkill DeepCopyInternally() { return (ActiveSkill)super.DeepCopyInternally(); }
    //End Protected Methods
}