package eean_games.tbsg._01.extension_method;

import java.math.BigDecimal;
import java.util.List;

import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.animationInfo.LaserAnimationInfo;
import eean_games.tbsg._01.animationInfo.MovementAnimationInfo;
import eean_games.tbsg._01.animationInfo.ProjectileAnimationInfo;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.effect.DamageEffect;
import eean_games.tbsg._01.effect.DrainEffect;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.effect.HealEffect;
import eean_games.tbsg._01.effect.MovementEffect;
import eean_games.tbsg._01.effect.StatusEffectAttachmentEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffectsWrapperEffect;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eAttackClassification;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eModificationMethod;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.equipment.AccessoryData;
import eean_games.tbsg._01.equipment.ArmourData;
import eean_games.tbsg._01.equipment.WeaponData;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.skill.CounterSkill;
import eean_games.tbsg._01.skill.CounterSkillData;
import eean_games.tbsg._01.skill.OrdinarySkill;
import eean_games.tbsg._01.skill.OrdinarySkillData;
import eean_games.tbsg._01.skill.PassiveSkill;
import eean_games.tbsg._01.skill.PassiveSkillData;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.SkillData;
import eean_games.tbsg._01.skill.UltimateSkill;
import eean_games.tbsg._01.skill.UltimateSkillData;
import eean_games.tbsg._01.status_effect.BuffStatusEffect;
import eean_games.tbsg._01.status_effect.BuffStatusEffectData;
import eean_games.tbsg._01.status_effect.DamageStatusEffect;
import eean_games.tbsg._01.status_effect.DamageStatusEffectData;
import eean_games.tbsg._01.status_effect.DebuffStatusEffect;
import eean_games.tbsg._01.status_effect.DebuffStatusEffectData;
import eean_games.tbsg._01.status_effect.Duration;
import eean_games.tbsg._01.status_effect.DurationData;
import eean_games.tbsg._01.status_effect.HealStatusEffect;
import eean_games.tbsg._01.status_effect.HealStatusEffectData;
import eean_games.tbsg._01.status_effect.StatusEffect;
import eean_games.tbsg._01.status_effect.StatusEffectData;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffect;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffectData;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitData;
import eean_games.tbsg._01.unit.UnitInstance;

public class NullPreventionAssignmentMethods 
{
    public static PlayerOnBoard CoalesceNull(PlayerOnBoard _playerOnBoard)
    {
        if (_playerOnBoard != null)
            return _playerOnBoard;
        else
            return new PlayerOnBoard(null, 0, false);
    }

    public static UnitData CoalesceNullAndReturnDeepCopyOptionally(UnitData _unitData, boolean _returnDeepCopyInstead)
    {
        if (_unitData != null)
        {
            if (_returnDeepCopyInstead)
                return _unitData.DeepCopy();
            else
                return _unitData;
        }
        else
            return new UnitData(0, "", null, eGender.values()[0], eRarity.values()[0], eTargetRangeClassification.values()[0], eTargetRangeClassification.values()[0], null, null, null, null, 0, 0, 0, 0, 0, 0, null, null, "", null, null);
    }

    public static Unit CoalesceNullAndReturnDeepCopyOptionally(Unit _unit, boolean _returnDeepCopyInstead)
    {
        if (_unit != null)
        {
            if (_returnDeepCopyInstead)
                return _unit.DeepCopy();
            else
                return _unit;
        }
        else
            return new Unit(null, 0, "", 0, false, (List<Skill>)null);
    }

    public static UnitInstance CoalesceNullAndReturnDeepCopyOptionally(UnitInstance _unitInstance, boolean _returnDeepCopyInstead)
    {
        if (_unitInstance != null)
        {
            if (_returnDeepCopyInstead)
                return _unitInstance.DeepCopy();
            else
                return _unitInstance;
        }
        else
            return new UnitInstance(new Unit(null, 0, "", 0, false, (List<Skill>)null), null);
    }

    public static WeaponData CoalesceNullAndReturnDeepCopyOptionally(WeaponData _weaponData, boolean _returnDeepCopyInstead)
    {
        if (_weaponData != null)
        {
            if (_returnDeepCopyInstead)
                return _weaponData.DeepCopy();
            else
                return _weaponData;
        }
        else
            return new WeaponData(0, "", null, eRarity.values()[0], null, eWeaponType.values()[0], null, null);
    }

    public static ArmourData CoalesceNullAndReturnDeepCopyOptionally(ArmourData _armourData, boolean _returnDeepCopyInstead)
    {
        if (_armourData != null)
        {
            if (_returnDeepCopyInstead)
                return _armourData.DeepCopy();
            else
                return _armourData;
        }
        else
            return new ArmourData(0, "", null, eRarity.values()[0], null, eArmourClassification.values()[0], eGender.values()[0]);
    }

    public static AccessoryData CoalesceNullAndReturnDeepCopyOptionally(AccessoryData _accessoryData, boolean _returnDeepCopyInstead)
    {
        if (_accessoryData != null)
        {
            if (_returnDeepCopyInstead)
                return _accessoryData.DeepCopy();
            else
                return _accessoryData;
        }
        else
            return new AccessoryData(0, "", null, eRarity.Common, null, eAccessoryClassification.values()[0], eGender.values()[0]);
    }

    public static SkillData CoalesceNullAndReturnDeepCopyOptionally(SkillData _skillData, boolean _returnDeepCopyInstead)
    {
        if (_skillData != null)
        {
            if (_returnDeepCopyInstead)
                return _skillData.DeepCopy();

            return _skillData;
        }
        else
        {
            if (_skillData instanceof OrdinarySkillData) { return new OrdinarySkillData(0, "", null, null, 0, null, null, 0, null); }
            else if (_skillData instanceof CounterSkillData) { return new CounterSkillData(0, "", null, null, 0, null, null, 0, null, eEventTriggerTiming.values()[0], null); }
            else if (_skillData instanceof UltimateSkillData) { return new UltimateSkillData(0, "", null, null, 0, null, null); }
            else /*(_skillData is PassiveSkillData)*/ { return new PassiveSkillData(0, "", null, null, 0, eTargetUnitClassification.values()[0], null); }
        }
    }

    public static Skill CoalesceNullAndReturnDeepCopyOptionally(Skill _skill, boolean _returnDeepCopyInstead)
    {
        if (_skill != null)
        {
            if (_returnDeepCopyInstead)
                return _skill.DeepCopy();

            return _skill;
        }
        else
        {
            if (_skill instanceof OrdinarySkill) { return new OrdinarySkill(null, 0); }
            else if (_skill instanceof CounterSkill) { return new CounterSkill(null, 0); }
            else if (_skill instanceof UltimateSkill) { return new UltimateSkill(null, 0); }
            else /*(_skill is PassiveSkill)*/ { return new PassiveSkill(null, 0); }
        }
    }

    public static Effect CoalesceNullAndReturnDeepCopyOptionally(Effect _effect, boolean _returnDeepCopyInstead)
    {
        if (_effect != null)
        {
            if (_returnDeepCopyInstead)
                return _effect.DeepCopy();

            return _effect;
        }
        else
        {
        	if (_effect instanceof UnitTargetingEffectsWrapperEffect) { return new UnitTargetingEffectsWrapperEffect(0, null, null, eTargetUnitClassification.values()[0]); }
        	else if (_effect instanceof DamageEffect) { return new DamageEffect(0, null, null, null, null, null, null, eTargetUnitClassification.values()[0], eAttackClassification.values()[0], null, false, eElement.values()[0]); }
        	else if (_effect instanceof DrainEffect) { return new DrainEffect(0, null, null, null, null, null, null, eTargetUnitClassification.values()[0], null, eAttackClassification.values()[0], null, false, eElement.values()[0], null, null); }
        	else if (_effect instanceof HealEffect) { return new HealEffect(0, null, null, null, null, null, null, eTargetUnitClassification.values()[0], null, false); }
            else if (_effect instanceof StatusEffectAttachmentEffect) { return new StatusEffectAttachmentEffect(0, null, null, null, null, null, null, eTargetUnitClassification.values()[0], null); }
            else /*if (_effect instanceof MovementEffect)*/ { return new MovementEffect(0, null, null, null, null); }
            //else if (_effect instanceof UnitSwapEffect) { return new UnitSwapEffect(null, null, null, 0, eTargetUnitClassification.values()[0]); }
            //else if (_effect instanceof TileTrapEffect) { return new TileTrapEffect(null, null, null, null, null, 0, null); }
            //else /*(_effect is TileSwapEffect)*/ { return new TileSwapEffect(null, null, null, null, null, 0); }
        }
    }

    public static StatusEffectData CoalesceNullAndReturnDeepCopyOptionally(StatusEffectData _statusEffectData, boolean _returnDeepCopyInstead)
    {
        if (_statusEffectData != null)
        {
            if (_returnDeepCopyInstead)
                return _statusEffectData.DeepCopy();

            return _statusEffectData;
        }
        else
        {
            if (_statusEffectData instanceof BuffStatusEffectData) { return new BuffStatusEffectData(0, null, eActivationTurnClassification.values()[0], null, null, eStatusType.values()[0], null, false); }
            else if (_statusEffectData instanceof DebuffStatusEffectData) { return new DebuffStatusEffectData(0, null, eActivationTurnClassification.values()[0], null, null, eStatusType.values()[0], null, false); }
            else if (_statusEffectData instanceof TargetRangeModStatusEffectData) { return new DebuffStatusEffectData(0, null, eActivationTurnClassification.values()[0], null, null, eStatusType.values()[0], null, false); }
            else if (_statusEffectData instanceof DamageStatusEffectData) { return new DamageStatusEffectData(0, null, eActivationTurnClassification.values()[0], eEventTriggerTiming.values()[0], null, null, null, null); }
            else /*(_statusEffectData is HealStatusEffectData)*/ { return new HealStatusEffectData(0, null, eActivationTurnClassification.values()[0], eEventTriggerTiming.values()[0], null, null, null, null); }
        }
    }
    
    public static StatusEffect CoalesceNullAndReturnDeepCopyOptionally(StatusEffect _statusEffect, boolean _returnDeepCopyInstead)
    {
        if (_statusEffect != null)
        {
            if (_returnDeepCopyInstead)
                return _statusEffect.DeepCopy();

            return _statusEffect;
        }
        else
        {
            if (_statusEffect instanceof BuffStatusEffect) { return new BuffStatusEffect(null, eActivationTurnClassification.values()[0], null, null, eStatusType.values()[0], null, false, 0, 0); }
            else if (_statusEffect instanceof DebuffStatusEffect) { return new DebuffStatusEffect(null, eActivationTurnClassification.values()[0], null, null, eStatusType.values()[0], null, false, 0, 0); }
            else if (_statusEffect instanceof TargetRangeModStatusEffect) { return new TargetRangeModStatusEffect(null, eActivationTurnClassification.values()[0], null, null, false, eTargetRangeClassification.values()[0], eModificationMethod.values()[0], 0, 0); }
            else if (_statusEffect instanceof DamageStatusEffect) { return new DamageStatusEffect(null, eActivationTurnClassification.values()[0], eEventTriggerTiming.values()[0], null, null, null, null, 0, 0); }
            else /*(_statusEffect is HealStatusEffect)*/ { return new HealStatusEffect(null, eActivationTurnClassification.values()[0], eEventTriggerTiming.values()[0], null, null, null, null, 0, 0); }
        }
    }

    public static DurationData CoalesceNullAndReturnDeepCopyOptionally(DurationData _durationData, boolean _returnDeepCopyInstead)
    {
        if (_durationData!= null)
        {
            if (_returnDeepCopyInstead)
                return _durationData.DeepCopy();
            else
                return _durationData;
        }
        else
            return new DurationData(null, null, null);
    }

    public static Duration CoalesceNullAndReturnDeepCopyOptionally(Duration _duration, boolean _returnDeepCopyInstead)
    {
        if (_duration != null)
        {
            if (_returnDeepCopyInstead)
                return _duration.DeepCopy();
            else
                return _duration;
        }
        else
            return new Duration(0, new BigDecimal("0.0"), null);
    }

    public static Tag CoalesceNullAndReturnDeepCopyOptionally(Tag _tag, boolean _returnDeepCopyInstead)
    {
        if (_tag != null)
        {
            if (_returnDeepCopyInstead)
                return _tag.DeepCopy();
            else
                return _tag;
        }
        else
            return Tag.newTag("");
    }

    public static ComplexCondition CoalesceNullAndReturnDeepCopyOptionally(ComplexCondition _complexCondition, boolean _returnDeepCopyInstead)
    {
        if (_complexCondition != null)
        {
            if (_returnDeepCopyInstead)
                return _complexCondition.DeepCopy();
            else
                return _complexCondition;
        }
        else
            return new ComplexCondition(null);
    }

    public static AnimationInfo CoalesceNull(AnimationInfo _animationInfo)
    {
        if (_animationInfo != null)
            return _animationInfo;
        else
        {
        	if (_animationInfo instanceof SimpleAnimationInfo) { return new SimpleAnimationInfo(0); }
        	else if (_animationInfo instanceof ProjectileAnimationInfo) { return new ProjectileAnimationInfo(0, 0, 0); }
        	else if (_animationInfo instanceof LaserAnimationInfo) { return new LaserAnimationInfo(0, 0, 0); }
            else /*if (_animationInfo instanceof MovementAnimationInfo)*/ { return new MovementAnimationInfo(0); }
        }
    }
}
