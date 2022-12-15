package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.enumerable.eAttackClassification;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public class DrainEffect extends UnitTargetingEffect implements DeepCopyable<Effect>, ComplexTargetSelectionEffect
{
    public DrainEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification,
        Tag _maxNumberOfSecondaryTargets, eAttackClassification _attackClassification, Tag _value, boolean _isFixedValue, eElement _element, Tag _drainingEfficiency, SimpleAnimationInfo _healAnimationInfo)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification);
    	
    	maxNumberOfSecondaryTargets = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_maxNumberOfSecondaryTargets, true);
    	
        AttackClassification = _attackClassification;

        value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_value, true);

        IsFixedValue = _isFixedValue;
        Element = _element;
        
        drainingEfficiency = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_drainingEfficiency, true);
        
        healAnimationInfo = (SimpleAnimationInfo)(NullPreventionAssignmentMethods.CoalesceNull(_healAnimationInfo));
    }

    //Public Read-only Fields
    public final eAttackClassification AttackClassification;
    public final boolean IsFixedValue;
    public final eElement Element;
    public final SimpleAnimationInfo healAnimationInfo;
    //End Public Read-only Fields
    
    //Getters
    public Tag getMaxNumberOfSecondaryTargets() { return maxNumberOfSecondaryTargets; }
    public Tag getValue() { return value; }
    public Tag getDrainingEfficiency() { return drainingEfficiency; }
    //End Getters
    
    //Private Fields
    private Tag maxNumberOfSecondaryTargets;
    private Tag value;
    private Tag drainingEfficiency;
    //End Private Fields

    //Public Methods
    public DrainEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected DrainEffect DeepCopyInternally()
    {
        DrainEffect copy = (DrainEffect)super.DeepCopyInternally();

        copy.value = value.DeepCopy();

        copy.drainingEfficiency = drainingEfficiency.DeepCopy();
        
        return copy;
    }
    //End Protected Methods
}
