package eean_games.tbsg._01.status_effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public abstract class ForegroundStatusEffect extends StatusEffect implements DeepCopyable<StatusEffect>
{
    public ForegroundStatusEffect(Duration _duration, ComplexCondition _activateCondition, UnitInstance _effectUser,
        eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, SimpleAnimationInfo _animationInfo, int _originSkillLevel, int _equipmentLevel)
    {
    	super(_duration, _activateCondition, _effectUser, _originSkillLevel, _equipmentLevel);
    	
        ActivationTurnClassification = _activationTurnClassification;
        EventTriggerTiming = _eventTriggerTiming;
        AnimationInfo = _animationInfo;
    }

    public ForegroundStatusEffect(ForegroundStatusEffectData _data, UnitInstance _effectApplier, int _originSkillLevel, int _equipmentLevel, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	super(_data, _effectApplier, _originSkillLevel, _equipmentLevel, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    	
        ActivationTurnClassification = _data.ActivationTurnClassification;
        EventTriggerTiming = _data.EventTriggerTiming;
        AnimationInfo = _data.AnimationInfo;
    }

    //Public Read-only Fields
    public final eActivationTurnClassification ActivationTurnClassification;
    public final eEventTriggerTiming EventTriggerTiming;
    public final SimpleAnimationInfo AnimationInfo;
    //End Public Read-only Fields

    //Public Methods
    public ForegroundStatusEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected ForegroundStatusEffect DeepCopyInternally() { return (ForegroundStatusEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}
