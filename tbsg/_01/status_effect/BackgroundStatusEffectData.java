package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;

public abstract class BackgroundStatusEffectData extends StatusEffectData implements DeepCopyable<StatusEffectData>
{
    public BackgroundStatusEffectData(int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes,
        eActivationTurnClassification _activationTurnClassification)
    {
    	super(_id, _duration, _activationCondition, _iconAsBytes);
    	
        ActivationTurnClassification = _activationTurnClassification;
    }

    //Public Read-only Fields
    public final eActivationTurnClassification ActivationTurnClassification;
    //End Public Read-only Fields

    //Public Methods
    public BackgroundStatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected BackgroundStatusEffectData DeepCopyInternally() { return (BackgroundStatusEffectData)super.DeepCopyInternally(); }
    //End Protected Methods
}