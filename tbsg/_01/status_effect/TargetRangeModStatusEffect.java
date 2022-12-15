package eean_games.tbsg._01.status_effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eModificationMethod;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public class TargetRangeModStatusEffect extends BackgroundStatusEffect implements DeepCopyable<StatusEffect>
{
	public TargetRangeModStatusEffect(Duration _duration, eActivationTurnClassification _activationTurnClassification, ComplexCondition _activateCondition, UnitInstance _effectUser, 
			boolean _isMovementRangeClassification, eTargetRangeClassification _targetRangeClassification, eModificationMethod _modificationMethod, int _originSkillLevel, int _equipmentLevel)
	{
    	super(_duration, _activateCondition, _effectUser, _activationTurnClassification, _originSkillLevel, _equipmentLevel);
    	
        IsMovementRangeClassification = _isMovementRangeClassification;
        TargetRangeClassification = _targetRangeClassification;
        ModificationMethod = _modificationMethod;
	}
	public TargetRangeModStatusEffect(TargetRangeModStatusEffectData _data)
    {
    	this(_data, null, null, null, null, null, null, null, null, 0, 0, null);
    }
	public TargetRangeModStatusEffect(TargetRangeModStatusEffectData _data, int _level, boolean _isOriginSkillLevelAndNotEquipmentLevel)
    {
        this(_data, null, null, null, null, null, null, null, null, _isOriginSkillLevelAndNotEquipmentLevel ? _level : 0, _isOriginSkillLevelAndNotEquipmentLevel ? 0 : _level, null);
    }
	public TargetRangeModStatusEffect(TargetRangeModStatusEffectData _data, int _originSkillLevel, int _equipmentLevel)
    {
    	this(_data, null, null, null, null, null, null, null, null, _originSkillLevel, _equipmentLevel, null);
    }
	public TargetRangeModStatusEffect(TargetRangeModStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	this(_data, _effectApplier, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _originSkillLevel, 0, _secondaryTargetsForComplexTargetSelectionEffect);
    }
	private TargetRangeModStatusEffect(TargetRangeModStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, int _equipmentLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	super(_data, _effectApplier, _originSkillLevel, _equipmentLevel, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    	
        IsMovementRangeClassification = _data.IsMovementRangeClassification;
        TargetRangeClassification = _data.TargetRangeClassification;
        ModificationMethod = _data.ModificationMethod;
    }

    //Public Read-only Methods
    public final boolean IsMovementRangeClassification;
    public final eTargetRangeClassification TargetRangeClassification;
    public final eModificationMethod ModificationMethod;
    //End Public Read-only Methods

    //Public Methods
    public TargetRangeModStatusEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected TargetRangeModStatusEffect DeepCopyInternally() { return (TargetRangeModStatusEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}