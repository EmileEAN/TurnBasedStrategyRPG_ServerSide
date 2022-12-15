package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.Linq;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;


public class UnitTargetingEffectsWrapperEffect extends UnitTargetingEffect implements DeepCopyable<Effect>
{
    public UnitTargetingEffectsWrapperEffect(int _id, ComplexCondition _activationCondition, List<UnitTargetingEffect> _secondaryEffects, eTargetUnitClassification _targetClassification)
    {
    	super(_id, _activationCondition, null, null, null, Linq.cast(_secondaryEffects, Effect.class), null, _targetClassification);
    }

    //Public Methods
    public UnitTargetingEffectsWrapperEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected UnitTargetingEffectsWrapperEffect DeepCopyInternally() { return (UnitTargetingEffectsWrapperEffect)super.DeepCopyInternally();}
    //End Protected Methods
}
