package eean_games.tbsg._01.skill;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public abstract class Skill implements DeepCopyable<Skill>, Cloneable
{
    public Skill(SkillData _skillData, int _level)
    {
        BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_skillData, false);

        Level = _level;
    }

    //Public Fields
    public int Level;
    //End Public Fields
    
    //Public Read-only Fields
    public final SkillData BaseInfo; // Store the reference to the original instance
    //End Public Read-only Fields

    //Public Methods
    public Skill DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected Skill DeepCopyInternally() 
    { 
    	try {
			return (Skill)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}