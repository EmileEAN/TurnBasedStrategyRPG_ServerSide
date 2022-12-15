package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public class DebuffStatusEffectData extends BackgroundStatusEffectData implements DeepCopyable<StatusEffectData>
{
    public DebuffStatusEffectData(int _id, DurationData _duration, eActivationTurnClassification _activationTurnClassification, ComplexCondition _activationCondition, byte[] _iconAsBytes,
        eStatusType _targetStatusType, Tag _value, boolean _isSum)
    {
    	super(_id, _duration, _activationCondition, _iconAsBytes, _activationTurnClassification);
    	
        TargetStatusType = _targetStatusType;

        Value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_value, true);

        IsSum = _isSum;
    }

    //Public Read-only Fields
    public final eStatusType TargetStatusType;
    public final boolean IsSum;
    //End Public Read-only Fields

    //Getters
    public Tag getValue() { return Value; }
    //End Getters
    
    //Private Fields
    private Tag Value;
    //End Private Fields
    
    //Public Methods
    public DebuffStatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected DebuffStatusEffectData DeepCopyInternally()
    {
    	DebuffStatusEffectData copy = (DebuffStatusEffectData)super.DeepCopyInternally();

        copy.Value = Value.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
