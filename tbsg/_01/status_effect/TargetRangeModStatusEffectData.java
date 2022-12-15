package eean_games.tbsg._01.status_effect;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eModificationMethod;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;

public class TargetRangeModStatusEffectData extends BackgroundStatusEffectData implements DeepCopyable<StatusEffectData>
{
    public TargetRangeModStatusEffectData(int _id, DurationData _duration, eActivationTurnClassification _activationTurnClassification, ComplexCondition _activationCondition, byte[] _iconAsBytes,
        boolean _isMovementRangeClassification, eTargetRangeClassification _targetRangeClassification, eModificationMethod _modificationMethod)
    {
    	super(_id, _duration, _activationCondition, _iconAsBytes, _activationTurnClassification);
    	
        IsMovementRangeClassification = _isMovementRangeClassification;
        TargetRangeClassification = _targetRangeClassification;
        ModificationMethod = _modificationMethod;
    }
    
    //Public Read-only Fields
    public boolean IsMovementRangeClassification;
    public eTargetRangeClassification TargetRangeClassification;
    public eModificationMethod ModificationMethod;
    //End Public Read-only Fields

    //Public Methods
    public TargetRangeModStatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected TargetRangeModStatusEffectData DeepCopyInternally() { return (TargetRangeModStatusEffectData)super.DeepCopyInternally(); }
    //End Protected Methods
}