package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public abstract class ForegroundStatusEffectData extends StatusEffectData implements DeepCopyable<StatusEffectData>
{
    public ForegroundStatusEffectData(int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes,
        eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, SimpleAnimationInfo _animationInfo)
    {
    	super(_id, _duration, _activationCondition, _iconAsBytes);
    	
        ActivationTurnClassification = _activationTurnClassification;
        EventTriggerTiming = _eventTriggerTiming;
        AnimationInfo = (SimpleAnimationInfo)(NullPreventionAssignmentMethods.CoalesceNull(_animationInfo));
    }
    
    //Public Read-only Fields
    public final eActivationTurnClassification ActivationTurnClassification;
    public final eEventTriggerTiming EventTriggerTiming;
    public final SimpleAnimationInfo AnimationInfo;
    //End Public Read-only Fields

    //Public Methods
    public ForegroundStatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected ForegroundStatusEffectData DeepCopyInternally() { return (ForegroundStatusEffectData)super.DeepCopyInternally(); }
    //End Protected Methods
}
