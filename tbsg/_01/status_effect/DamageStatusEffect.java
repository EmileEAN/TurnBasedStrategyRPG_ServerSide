package eean_games.tbsg._01.status_effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public class DamageStatusEffect extends ForegroundStatusEffect implements DeepCopyable<StatusEffect>
{
    public DamageStatusEffect(Duration _duration, eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, ComplexCondition _activateCondition, UnitInstance _effectUser, SimpleAnimationInfo _animationInfo,
            Tag _damage, int _originSkillLevel, int _equipmentLevel)
        {
        	super(_duration, _activateCondition, _effectUser, _activationTurnClassification, _eventTriggerTiming, _animationInfo, _originSkillLevel, _equipmentLevel);
        	
            Damage = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_damage, true);
        }
        public DamageStatusEffect(DamageStatusEffectData _data)
        {
        	this(_data, null, null, null, null, null, null, null, null, 0, 0, null);
        }
        public DamageStatusEffect(DamageStatusEffectData _data, int _level, boolean _isOriginSkillLevelAndNotEquipmentLevel)
        {
            this(_data, null, null, null, null, null, null, null, null, _isOriginSkillLevelAndNotEquipmentLevel ? _level : 0, _isOriginSkillLevelAndNotEquipmentLevel ? 0 : _level, null);
        }
        public DamageStatusEffect(DamageStatusEffectData _data, int _originSkillLevel, int _equipmentLevel)
        {
        	this(_data, null, null, null, null, null, null, null, null, _originSkillLevel, _equipmentLevel, null);
        }
        public DamageStatusEffect(DamageStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
        {
        	this(_data, _effectApplier, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _originSkillLevel, 0, _secondaryTargetsForComplexTargetSelectionEffect);
        }
        private DamageStatusEffect(DamageStatusEffectData _data, UnitInstance _effectApplier, BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, int _originSkillLevel, int _equipmentLevel, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
        {
        	super(_data, _effectApplier, _originSkillLevel, _equipmentLevel, _system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        	
            Damage = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_data.getValue(), true);
        }

        //Getters
        public Tag getDamage() { return Damage; }
        //End Getters
        
        //Private Fields
        private Tag Damage;
        //End Private Fields

        //Public Methods
        public DamageStatusEffect DeepCopy() { return DeepCopyInternally(); }
        //End Public Methods

        //Protected Methods
        @Override
        protected DamageStatusEffect DeepCopyInternally()
        {
        	DamageStatusEffect copy = (DamageStatusEffect)super.DeepCopyInternally();

            copy.Damage = Damage.DeepCopy();

            return copy;
        }
        //End Protected Methods
}
