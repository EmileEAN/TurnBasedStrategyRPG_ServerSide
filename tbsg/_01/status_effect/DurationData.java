package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public final class DurationData implements DeepCopyable<DurationData>, Cloneable
{
    public DurationData(Tag _activationTimes, Tag _turns, ComplexCondition _whileConditions)
    {
        ActivationTimes = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationTimes, true);
        Turns = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_turns, true);
        WhileCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_whileConditions, true);
    }

    //Getters
    /*Can use either one or combine fields*/
    public Tag getActivationTimes() { return ActivationTimes; }
    public Tag getTurns() { return Turns; } //Decimal number. 0.5 represents a player turn. 1 represents a turn of each player.
    public ComplexCondition getWhileCondition() { return WhileCondition; }
    //End Getters

    //Private Fields
    private Tag ActivationTimes;
    private Tag Turns; //Decimal number. 0.5 represents a player turn. 1 represents a turn of each player.
    private ComplexCondition WhileCondition;
    //End Private Fields
    
    //Public Methods
    public DurationData DeepCopy()
    {
		try {
			DurationData copy = (DurationData)super.clone();
			
	        copy.ActivationTimes = ActivationTimes.DeepCopy();
	        copy.Turns = Turns.DeepCopy();
	        copy.WhileCondition = WhileCondition.DeepCopy();

	        return copy;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
