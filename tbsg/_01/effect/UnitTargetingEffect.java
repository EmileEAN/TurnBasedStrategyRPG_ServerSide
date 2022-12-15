package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;

public abstract class UnitTargetingEffect extends Effect implements DeepCopyable<Effect>
{
    public UnitTargetingEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo,
        eTargetUnitClassification _targetClassification)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo);
    	
        TargetClassification = _targetClassification;
    }

    //Public Read-only Fields
    public final eTargetUnitClassification TargetClassification;
    //End Public Read-only Fields

    //Public Methods
    public UnitTargetingEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected UnitTargetingEffect DeepCopyInternally() { return (UnitTargetingEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}
