package eean_games.tbsg._01.status_effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public class DebuffStatusEffect extends BackgroundStatusEffect implements DeepCopyable<StatusEffect>
{
	public DebuffStatusEffect(Duration _duration, eActivationTurnClassification _activationTurnClassification, ComplexCondition _activateCondition, UnitInstance _effectUser, 
			eStatusType _targetStatusType, Tag _value, boolean _isSum, int _originSkillLevel, int _equipmentLevel)
	{
    	super(_duration, _activateCondition, _effectUser, _activationTurnClassification, _originSkillLevel, _equipmentLevel);
    	
        TargetStatusType = _targetStatusType;
        Value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_value, true);
        IsSum = _isSum;
	}
	public DebuffStatusEffect(DebuffStatusEffectData _data)
    {
    	this(_data, null, null, null, null, null, null, null, null, 0, 0, null);
    }
	public DebuffStatusEffect(DebuffStatusEffectData _data, int _level, boolean _isOriginSkillLevelAndNotEquipmentLevel)
    {
        this(_data, null, null, null, null, null, null, null, null, _isOriginSkillLevelAndNotEquipmentLevel ? _level : 0, _isOriginSkillLevelAndNotEquipmentLevel ? 0 : _level, null);
    }
	public DebuffStatusEffect(DebuffStatusEffectData _data, int _originSkillLevel, int _equipmentLevel)
    {
    	this(_data, null, null, null, null, null, null, null, null, _originSkillLevel, _equipmentLevel, null);
    }
	public DebuffStatusEffect(DebuffStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	this(_data, _effectApplier, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _originSkillLevel, 0, _secondaryTargetsForComplexTargetSelectionEffect);
    }
	private DebuffStatusEffect(DebuffStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, int _equipmentLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	super(_data, _effectApplier, _originSkillLevel, _equipmentLevel, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    	
        TargetStatusType = _data.TargetStatusType;
        Value = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_data.getValue(), true);
        IsSum = _data.IsSum;
    }

    //Public Read-only Methods
    public final eStatusType TargetStatusType;
    public final boolean IsSum;
    //End Public Read-only Methods

    //Getters
    public Tag getValue() { return Value; }
    //End Getters
    
    //Private Fields
    private Tag Value;
    //End Private Fields

    //Public Methods
    public DebuffStatusEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected DebuffStatusEffect DeepCopyInternally()
    {
    	DebuffStatusEffect copy = (DebuffStatusEffect)super.DeepCopyInternally();

        copy.Value = Value.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
