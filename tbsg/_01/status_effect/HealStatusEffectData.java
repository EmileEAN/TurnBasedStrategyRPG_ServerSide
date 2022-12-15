package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public class HealStatusEffectData extends ForegroundStatusEffectData implements DeepCopyable<StatusEffectData>
{
    public HealStatusEffectData(int _id, DurationData _duration, eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, ComplexCondition _activationCondition, byte[] _iconAsBytes, SimpleAnimationInfo _animationInfo,
        Tag _value)
    {
    	super(_id, _duration, _activationCondition, _iconAsBytes, _activationTurnClassification, _eventTriggerTiming, _animationInfo);
    	
        Value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_value, true);
    }

    //Getters
    public Tag getValue() { return Value; }
    //End Getters
    
    //Private Fields
    private Tag Value;
    //End Private Fields

    //Public Methods
    public HealStatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected HealStatusEffectData DeepCopyInternally()
    {
        HealStatusEffectData copy = (HealStatusEffectData)super.DeepCopyInternally();

        copy.Value = Value.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
