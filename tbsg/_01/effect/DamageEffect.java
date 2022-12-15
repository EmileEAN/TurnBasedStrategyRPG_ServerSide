package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.enumerable.eAttackClassification;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public class DamageEffect extends UnitTargetingEffect implements DeepCopyable<Effect>
{
    public DamageEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification,
        eAttackClassification _attackClassification, Tag _value, boolean _isFixedValue, eElement _element)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification);
    	
        AttackClassification = _attackClassification;

        Value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_value, true);

        IsFixedValue = _isFixedValue;
        Element = _element;
    }
    public DamageEffect(DrainEffect _drainEffect)
    {
    	super (_drainEffect.Id, _drainEffect.getActivationCondition(), _drainEffect.getTimesToApply(), _drainEffect.getSuccessRate(), _drainEffect.getDiffusionDistance(), _drainEffect.getSecondaryEffects(), _drainEffect.AnimationInfo, _drainEffect.TargetClassification);
    
    	AttackClassification = _drainEffect.AttackClassification;
    	
    	Value = _drainEffect.getValue();
    	
    	IsFixedValue = _drainEffect.IsFixedValue;
    	Element = _drainEffect.Element;
    }

    //Public Read-only Fields
    public final eAttackClassification AttackClassification;
    public final boolean IsFixedValue;
    public final eElement Element;
    //End Public Read-only Fields
    
    //Getters
    public Tag getValue() { return Value; }
    //End Getters
    
    //Private Fields
    private Tag Value;
    //End Private Fields

    //Public Methods
    public DamageEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected DamageEffect DeepCopyInternally()
    {
        DamageEffect copy = (DamageEffect)super.DeepCopyInternally();

        copy.Value = Value.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
