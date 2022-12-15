package eean_games.tbsg._01.status_effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public abstract class StatusEffect implements DeepCopyable<StatusEffect>, Cloneable //Poison, Confusion, Strength down, etc. This will be attached directly to Units
{
    public StatusEffect(Duration _duration, ComplexCondition _activationCondition, UnitInstance _effectUser, int _originSkillLevel, int _equipmentLevel)
    {
        Duration = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_duration, true);
        ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationCondition, true);
        EffectApplier = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_effectUser, false);

        OriginSkillLevel = _originSkillLevel;
        EquipmentLevel = _equipmentLevel;
    }

    public StatusEffect(StatusEffectData _data, UnitInstance _effectApplier, int _originSkillLevel, int _equipmentLevel, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_data.getActivationCondition(), true);
        EffectApplier = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_effectApplier, false);

        OriginSkillLevel = _originSkillLevel;
        EquipmentLevel = _equipmentLevel;

        Duration = new Duration(_data.getDuration(), _system, this, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }

    //Public Read-only Fields
    public final UnitInstance EffectApplier; //Store reference to original instance
    public final int OriginSkillLevel; // Level of the skill that generated or that contains this status effect
    public final int EquipmentLevel; // Level of the equipment that contains this status effect
    //End Public Read-only Fields
    
    //Getters
    public Duration getDuration() { return Duration; }
    public ComplexCondition getActivationCondition() { return ActivationCondition; }
    //End Getters
    
    //Private Fields
    private Duration Duration;
    private ComplexCondition ActivationCondition;
    //End Private Fields

    //Public Methods
    public StatusEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected StatusEffect DeepCopyInternally()
    {
		try {
			StatusEffect copy = (StatusEffect)super.clone();
			
	        copy.Duration = Duration.DeepCopy();
	        copy.ActivationCondition = ActivationCondition.DeepCopy();

	        return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}
