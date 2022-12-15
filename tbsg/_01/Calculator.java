package eean_games.tbsg._01;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import eean_games.main.CoreFunctions;
import eean_games.main.Linq;
import eean_games.main.MTRandom;
import eean_games.main._2DCoord;
import eean_games.main.eRelationType;
import eean_games.main.extension_method.BigDecimalExtension;
import eean_games.tbsg._01.effect.DamageEffect;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.effect.HealEffect;
import eean_games.tbsg._01.effect.StatusEffectAttachmentEffect;
import eean_games.tbsg._01.effect.TileSwapEffect;
import eean_games.tbsg._01.effect.UnitSwapEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffect;
import eean_games.tbsg._01.enumerable.eAttackClassification;
import eean_games.tbsg._01.enumerable.eEffectiveness;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.enumerable.eModificationMethod;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.equipment.LevelableTransformableWeapon;
import eean_games.tbsg._01.equipment.LevelableWeapon;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.skill.PassiveSkill;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.BuffStatusEffect;
import eean_games.tbsg._01.status_effect.BuffStatusEffectData;
import eean_games.tbsg._01.status_effect.DamageStatusEffect;
import eean_games.tbsg._01.status_effect.DamageStatusEffectData;
import eean_games.tbsg._01.status_effect.DebuffStatusEffect;
import eean_games.tbsg._01.status_effect.DebuffStatusEffectData;
import eean_games.tbsg._01.status_effect.ForegroundStatusEffect;
import eean_games.tbsg._01.status_effect.ForegroundStatusEffectData;
import eean_games.tbsg._01.status_effect.HealStatusEffect;
import eean_games.tbsg._01.status_effect.HealStatusEffectData;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffect;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffectData;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitInstance;

public final class Calculator
{
    //Public Methods
    public static int RequiredExperienceForLevelUp(int _level)
    {
        int requiredExp = CoreValues.REQUIRED_EXPERIENCE_FOR_FIRST_LEVEL_UP;

        for (int i = 2; i <= _level; i++)
        {
            int tmpExp = BigDecimalExtension.multiply(CoreValues.LEVEL_EXPERIENCE_MULTIPLIER, requiredExp).setScale(0, RoundingMode.CEILING).intValue();
            if (tmpExp != requiredExp)
                requiredExp = tmpExp;
            else
                requiredExp++;
        }

        return requiredExp;
    }

    public static int MinimumAccumulatedExperienceRequired(int _level)
    {
    	int minAccumulatedExpRequired = 0;
        
        for (int i = 1; i < _level; i++)
        {
            minAccumulatedExpRequired += RequiredExperienceForLevelUp(i);
        }
        
        return minAccumulatedExpRequired;
    }
    
    //The amount of Exp gained after becoming the current level
    public static int LevelExperience(int _accumulatedExp)
    {
        if (_accumulatedExp < 0)
            return -1;

        int accumulatedRequiredExpForLevel = 0;
        int level = Level(_accumulatedExp);

        for (int i = 2; i <= level; i++)
        {
            accumulatedRequiredExpForLevel += RequiredExperienceForLevelUp(i - 1);
        }

        return _accumulatedExp - accumulatedRequiredExpForLevel;
    }

    public static int Level(Unit _unit)
    {
        if (_unit == null)
            return -1;

        return Level(_unit.getAccumulatedExperience());
    }
    public static int Level(Weapon _weapon)
    {
        if (_weapon == null)
            return -1;

        if (_weapon instanceof LevelableWeapon)
        	return Level(((LevelableWeapon)_weapon).getAccumulatedExperience());
        else if (_weapon instanceof LevelableTransformableWeapon)
        	return Level(((LevelableTransformableWeapon)_weapon).getAccumulatedExperience());
        
        return 0; //...if not a levelable weapon
    }
    public static int Level(int _accumulatedExp)
    {
        int actualLevel = AccumulatedLevel(_accumulatedExp);

        int maxLevel_common = eRarity.Common.numericValue();
        int maxLevel_uncommon = eRarity.Uncommon.numericValue();
        int maxLevel_rare = eRarity.Rare.numericValue();
        int maxLevel_epic = eRarity.Epic.numericValue();

        if (actualLevel > maxLevel_common) //It must be eRarity.Uncommon or higher
            actualLevel -= maxLevel_common;

        if (actualLevel > maxLevel_uncommon) //It must be eRarity.Rare or higher
            actualLevel -= maxLevel_uncommon;

        if (actualLevel > maxLevel_rare) //It must be eRarity.Epic or higher
            actualLevel -= maxLevel_rare;

        if (actualLevel > maxLevel_epic) //It must be eRarity.Legendary
            actualLevel -= maxLevel_epic;

        return actualLevel;
    }
    public static int AccumulatedLevel(eRarity _rarity, int _actualLevel)
    {
        int maxLevel_common = eRarity.Common.numericValue();
        int maxLevel_uncommon = eRarity.Uncommon.numericValue();
        int maxLevel_rare = eRarity.Rare.numericValue();
        int maxLevel_epic = eRarity.Epic.numericValue();
        int maxLevel_legendary = eRarity.Legendary.numericValue();

        switch (_rarity)
        {
            default: //Common
                {
                    if (_actualLevel > maxLevel_common)
                        return -1;
                    return _actualLevel;
                }
            case Uncommon:
                {
                    if (_actualLevel > maxLevel_uncommon)
                        return -1;
                    return _actualLevel + maxLevel_common;
                }
            case Rare:
                {
                    if (_actualLevel > maxLevel_rare)
                        return -1;
                    return _actualLevel + maxLevel_common + maxLevel_uncommon;
                }
            case Epic:
                {
                    if (_actualLevel > maxLevel_epic)
                        return -1;
                    return _actualLevel + maxLevel_common + maxLevel_uncommon + maxLevel_rare;
                }
            case Legendary:
                {
                    if (_actualLevel > maxLevel_legendary)
                        return -1;
                    return _actualLevel + maxLevel_common + maxLevel_uncommon + maxLevel_rare + maxLevel_epic;
                }
        }
    }
    private static int AccumulatedLevel(int _accumulatedExp)
    {
        if (_accumulatedExp < 0)
            return -1;
    	
        int accumulatedRequiredExpForLevel = 0;
        int accumulatedRequiredExpForNextLevel = 0;

        int maxAccumulatedLevel = MaxAccumulatedLevelForRarity(eRarity.Legendary);
        
        for (int i = 1; i < maxAccumulatedLevel; i++)
        {
            accumulatedRequiredExpForLevel = accumulatedRequiredExpForNextLevel;
            accumulatedRequiredExpForNextLevel += RequiredExperienceForLevelUp(i);

            if (_accumulatedExp >= accumulatedRequiredExpForLevel && _accumulatedExp < accumulatedRequiredExpForNextLevel)
                return i;
        }

        return maxAccumulatedLevel; //Must not exceed the maxAccumulatedLevel
    }
    private static int MaxAccumulatedLevelForRarity(eRarity _rarity) { return AccumulatedLevel(_rarity, _rarity.numericValue()); }
    
    public static boolean isMaxLevel(Unit _unit)
    {
    	int level = Level(_unit);
    	int maxLevelForRarity = _unit.BaseInfo.getRarity().numericValue();
    	
    	return level == maxLevelForRarity;
    }
    public static boolean isMaxLevel(Weapon _weapon)
    {
    	int level = Level(_weapon);
    	
    	if (_weapon instanceof LevelableWeapon || _weapon instanceof LevelableTransformableWeapon)
    	{
        	int maxLevelForRarity = _weapon.BaseInfo.getRarity().numericValue();
        	return level == maxLevelForRarity;	
    	}
    	
    	return true;
    }
    
    public static int MaxHP(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_HP).setScale(0, RoundingMode.CEILING).intValue();
    }
    public static int MaxHP(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return MaxHP_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int MaxHP(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return MaxHP_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int MaxHP_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal maxHP = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_HP);

        maxHP = ApplyBuffAndDebuffStatusEffects_Simple(maxHP, _system, _unit, eStatusType.MaxHP, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return maxHP.setScale(0, RoundingMode.CEILING).intValue();
    }

    public static int PhysicalStrength(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_PhysicalStrength).setScale(0, RoundingMode.HALF_UP).intValue();
    }
    public static int PhysicalStrength(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return PhysicalStrength_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int PhysicalStrength(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return PhysicalStrength_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int PhysicalStrength_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal phyStr = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_PhysicalStrength);

        phyStr = ApplyBuffAndDebuffStatusEffects_Simple(phyStr, _system, _unit, eStatusType.PhyStr, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return phyStr.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /// <summary>
    /// PreCondition: _character has been initialized successfully.
    /// PostCondition: Physical Resistance of _character is calculated and returned as int.
    /// </summary>
    /// <param name="_unit"></param>
    /// <returns></returns>
    public static int PhysicalResistance(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_PhysicalResistance).setScale(0, RoundingMode.HALF_UP).intValue();
    }
    public static int PhysicalResistance(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return PhysicalResistance_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int PhysicalResistance(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return PhysicalResistance_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int PhysicalResistance_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal phyRes = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_PhysicalResistance);

        phyRes = ApplyBuffAndDebuffStatusEffects_Simple(phyRes, _system, _unit, eStatusType.PhyRes, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return phyRes.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /// <summary>
    /// PreCondition: _character has been initialized successfully.
    /// PostCondition: Magical Strength of _character is calculated and returned as int.
    /// </summary>
    /// <param name="_unit"></param>
    /// <returns></returns>
    public static int MagicalStrength(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_MagicalStrength).setScale(0, RoundingMode.HALF_UP).intValue();
    }
    public static int MagicalStrength(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return MagicalStrength_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int MagicalStrength(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return MagicalResistance_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int MagicalStrength_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal magStr = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_MagicalStrength);

        magStr = ApplyBuffAndDebuffStatusEffects_Simple(magStr, _system, _unit, eStatusType.MagStr, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return magStr.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /// <summary>
    /// PreCondition: _character has been initialized successfully.
    /// PostCondition: Magical Resistance of _character is calculated and returned as int.
    /// </summary>
    /// <param name="_unit"></param>
    /// <returns></returns>
    public static int MagicalResistance(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_MagicalResistance).setScale(0, RoundingMode.HALF_UP).intValue();
    }
    public static int MagicalResistance(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return MagicalResistance_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int MagicalResistance(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return MagicalResistance_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int MagicalResistance_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal magRes = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_MagicalResistance);

        magRes = ApplyBuffAndDebuffStatusEffects_Simple(magRes, _system, _unit, eStatusType.MagRes, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return magRes.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /// <summary>
    /// PreCondition: _character has been initialized successfully.
    /// PostCondition: Vitality of _character is calculated and returned as int.
    /// </summary>
    /// <param name="_unit"></param>
    /// <returns></returns>
    public static int Vitality(Unit _unit)
    {
        if (_unit == null)
            return -1;

        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        return BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_Vitality).setScale(0, RoundingMode.HALF_UP).intValue();
    }
    public static int Vitality(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return -1;

        return Vitality_ActualDefinition(_unit, _system, null, null, null, null, null, null, null);
    }
    public static int Vitality(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return -1;
        }

        return Vitality_ActualDefinition(_unit, _system, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    private static int Vitality_ActualDefinition(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        BigDecimal levelRate = BigDecimalExtension.divide(AccumulatedLevel(_unit.getAccumulatedExperience()), MaxAccumulatedLevelForRarity(_unit.BaseInfo.getRarity()));

        BigDecimal vit = BigDecimalExtension.multiply(levelRate, _unit.BaseInfo.MaxLevel_Vitality);

        vit = ApplyBuffAndDebuffStatusEffects_Simple(vit, _system, _unit, eStatusType.Vit, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return vit.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public static BigDecimal Precision(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return BigDecimal.ZERO;

        BigDecimal pre = BigDecimal.ONE;

        pre = ApplyBuffAndDebuffStatusEffects_Simple(pre, _system, _unit, eStatusType.Pre);

        return pre;
    }
    public static BigDecimal Precision(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skill == null
            || _effect == null
            || _targetArea == null)
        {
            return BigDecimal.ZERO;
        }

        BigDecimal pre = BigDecimal.ONE;

        pre = ApplyBuffAndDebuffStatusEffects_Simple(pre, _system, _unit, eStatusType.Pre, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return pre;
    }

    public static BigDecimal Evasion(UnitInstance _unit, BattleSystemCore _system)
    {
        if (_unit == null || _system == null)
            return BigDecimal.ZERO;

        BigDecimal eva = BigDecimal.ONE;

        eva = ApplyBuffAndDebuffStatusEffects_Simple(eva, _system, _unit, eStatusType.Eva);

        return eva;
    }
    public static BigDecimal Evasion(UnitInstance _unit, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _targetArea, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_unit == null
            || _system == null
            || _skillUser == null
            || _skill == null
            || _effect == null
            || _targetArea == null
            || _targets == null)
        {
            return BigDecimal.ZERO;
        }

        BigDecimal eva = BigDecimal.ONE;

        eva = ApplyBuffAndDebuffStatusEffects_Simple(eva, _system, _unit, eStatusType.Eva, _skillUser, _skill, _effect, _targetArea, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        boolean isUnitEqualToTarget = false;
        if (_skill.BaseInfo.getEffect() instanceof UnitTargetingEffect)
        {
            if (_target != null && _unit == _target)
            	isUnitEqualToTarget = true;
        }

        if (isUnitEqualToTarget)
        {
            //If _effect is positive to _unit, change the value of eva to its reciprocal. That is, increase _unit's probability to evade the _effect if eva is low and be hit by it if eva is high. Use it as dexterity : this particular case.
            if (IsEffectPositiveForTarget(_unit, _effect, _system))
                eva = BigDecimalExtension.Reciprocal(eva);
        }

        return eva;
    }

    private static boolean IsEffectPositiveForTarget(UnitInstance _target, Effect _effect, BattleSystemCore _system)
    {
        boolean isEffectPositive = false;

        //Set of all possible positive effects
        if (_effect instanceof HealEffect)
            isEffectPositive = true;
        else if (_effect instanceof StatusEffectAttachmentEffect)
        {
        	StatusEffectAttachmentEffect detailedEffect = (StatusEffectAttachmentEffect)_effect;

            if (detailedEffect.getDataOfStatusEffectToAttach() instanceof BuffStatusEffectData
                || detailedEffect.getDataOfStatusEffectToAttach() instanceof HealStatusEffectData)
            {
                isEffectPositive = true;
            }
            else if (detailedEffect.getDataOfStatusEffectToAttach() instanceof TargetRangeModStatusEffectData)
            {
            	TargetRangeModStatusEffectData statusEffectData = (TargetRangeModStatusEffectData)(detailedEffect.getDataOfStatusEffectToAttach());

                if (statusEffectData.ModificationMethod == eModificationMethod.Add // The method of target range modification is addition
                    || (statusEffectData.ModificationMethod == eModificationMethod.Overwrite && RelativeTargetArea(_target, statusEffectData.IsMovementRangeClassification, _system).size() > TargetArea.GetTargetArea(statusEffectData.IsMovementRangeClassification ? _target.BaseInfo.MovementRangeClassification : _target.BaseInfo.NonMovementActionRangeClassification).size()))// OR the method is Overwrite and the total number of targetable coords : the effect's target area is greater than the current target area
                    isEffectPositive = true;
            }
        }
        //End of set of possible postive effects

        return isEffectPositive;
    }

    public static int MaxNumOfTargets(UnitInstance _skillUser, ActiveSkill _skill, List<_2DCoord> _targetArea, BattleSystemCore _system)
    {
        BigDecimal maxNumOfTargets = _skill.BaseInfo.getMaxNumberOfTargets().ToValue(BigDecimal.class, _system, _skillUser, _skill, _targetArea);

        if (_skill.BaseInfo.getEffect() instanceof UnitSwapEffect || _skill.BaseInfo.getEffect() instanceof TileSwapEffect)
            maxNumOfTargets = new BigDecimal("2");

        maxNumOfTargets = ApplyBuffAndDebuffStatusEffects_Simple(maxNumOfTargets, _system, _skillUser, eStatusType.NumOfTargets, _skillUser, _skill, _targetArea);

        return maxNumOfTargets.setScale(0, RoundingMode.FLOOR).intValue();
    }

    public static List<_2DCoord> RelativeTargetArea(UnitInstance _unit, boolean _isMovementRangeClassification, BattleSystemCore _system)
    {
        return RelativeTargetArea(_unit, _isMovementRangeClassification, _system, null);
    }
    public static List<_2DCoord> RelativeTargetArea(UnitInstance _unit, boolean _isMovementRangeClassification, BattleSystemCore _system, ActiveSkill _skill)
    {
        List<_2DCoord> relativeTargetArea;
        if (_isMovementRangeClassification)
            relativeTargetArea = TargetArea.GetTargetArea(_unit.BaseInfo.MovementRangeClassification);
        else
            relativeTargetArea = TargetArea.GetTargetArea(_unit.BaseInfo.NonMovementActionRangeClassification);

        if (_skill == null)
            relativeTargetArea = ApplyTargetRangeModStatusEffects(relativeTargetArea, _unit, _isMovementRangeClassification, _system);
        else
        	relativeTargetArea = ApplyTargetRangeModStatusEffects(relativeTargetArea, _unit, _isMovementRangeClassification, _system, _skill, _skill.BaseInfo.getEffect()); // _skill.Effect will actually not be used.

        return relativeTargetArea;
    }

    public static DamageInfo Damage(BattleSystemCore _system, UnitInstance _attacker, _2DCoord _attackerCoord, ActiveSkill _skill, DamageEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return Damage(_system, _attacker, _attackerCoord, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0);
    }
    public static DamageInfo Damage(BattleSystemCore _system, UnitInstance _attacker, _2DCoord _attackerCoord, ActiveSkill _skill, DamageEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect, int _diffusionDistance)
    {
        if (_system == null
            || _attacker == null
            || _skill == null
            || _effect == null
            || _effectRange == null
            || _target == null
            || _attackerCoord == null)
        {
            return null;
        }

        BigDecimal damage = new BigDecimal(String.valueOf(CoreValues.DAMAGE_BASE_VALUE));

        BigDecimal strength = BigDecimal.ZERO;
        BigDecimal resistance = BigDecimal.ZERO;
        
        List<Object> targets = Linq.cast(_targets, Object.class);
        List<Object> secondaryTargetsForComplexTargetSelectionEffect = Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, Object.class);

        if (_effect.AttackClassification == eAttackClassification.Physic)
        {
            strength = BigDecimalExtension.valueOf(PhysicalStrength(_attacker, _system, _attacker, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect));
            resistance = BigDecimalExtension.valueOf(PhysicalResistance(_target, _system, _attacker, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect));
        }
        else
        {
            strength = BigDecimalExtension.valueOf(MagicalStrength(_attacker, _system, _attacker, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect));
            resistance = BigDecimalExtension.valueOf(MagicalResistance(_target, _system, _attacker, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect));
        }

        damage = damage.multiply((strength.divide(resistance, 50, RoundingMode.HALF_UP)).multiply(BigDecimalExtension.divide(strength, CoreValues.MAX_BASE_ATTRIBUTE_VALUE))); //use BigDecimal to avoid unexpected round ups

        if (!_effect.IsFixedValue)
            damage = damage.multiply(CorrectionRate_Force(_system, _attacker, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect));

        EffectivenessInfo effectivenessInfo = DamageCorrectionRate_Element(_attacker, _effect.Element, _target);
        
        damage = damage.multiply(effectivenessInfo.correctionRate);

        //Based on relation ship between tile type and unit/effect
        damage = damage.multiply(CorrectionRate_TileType(_attacker, _effect, _system.Field.Board.Sockets[_attackerCoord.X][_attackerCoord.Y].TileType));

        boolean isCritical = false;
        if (IsCritical(_attacker, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _system))
        {
            damage = damage.multiply(CoreValues.MULTIPLIER_FOR_CRITICALHIT);
            isCritical = true;
        }

        damage = ApplyBuffAndDebuffStatusEffects_Compound(damage, _system, eStatusType.FixedDamage, _attacker, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect); // Applied the buff and debuff effects of effect user and target that are related to damage

        if (_diffusionDistance > 0)
        	damage = damage.multiply((new BigDecimal("0.5")).pow(_diffusionDistance));
        
        if (CoreFunctions.Compare(damage, eRelationType.LessThan, BigDecimal.ZERO)) // damage must not be a negative value
            damage = BigDecimal.ZERO;

        return new DamageInfo(damage.setScale(0, RoundingMode.FLOOR).intValue(), isCritical, effectivenessInfo.effectiveness);
    }

    public static HealInfo HealValue(BattleSystemCore _system, UnitInstance _effectUser, _2DCoord _effectUserCoord, ActiveSkill _skill, HealEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return HealValue(_system, _effectUser, _effectUserCoord, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0);
    }
    public static HealInfo HealValue(BattleSystemCore _system, UnitInstance _effectUser, _2DCoord _effectUserCoord, ActiveSkill _skill, HealEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect, int _diffusionDistance)
    {
        if (_system == null
            || _effectUser == null
            || _skill == null
            || _effect == null
            || _effectRange == null
            || _targets == null
            || _target == null
            || _effectUserCoord == null)
        {
            return null;
        }

        BigDecimal restoringHp = BigDecimalExtension.multiply(new BigDecimal("0.05"), (Vitality(_effectUser) + 1) * (Vitality(_target) + 1));

        if (!_effect.IsFixedValue)
            restoringHp = restoringHp.multiply(CorrectionRate_Force(_system, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect));

        //Based on whether the tile is heal tile
        restoringHp = restoringHp.multiply((_system.Field.Board.Sockets[_effectUserCoord.X][_effectUserCoord.Y].TileType == eTileType.Heal) ? CoreValues.MULTIPLIER_FOR_TILETYPEMATCH : BigDecimal.ONE);

        boolean isCritical = false;
        if (IsCritical(_effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _system))
        {
            restoringHp = restoringHp.multiply(CoreValues.MULTIPLIER_FOR_CRITICALHIT);
            isCritical = true;
        }

        restoringHp = ApplyBuffAndDebuffStatusEffects_Compound(restoringHp, _system, eStatusType.FixedHeal, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect); // Applied the buff and debuff effects of effect user and target that are related to healing

        if (_diffusionDistance > 0)
        	restoringHp = restoringHp.multiply((new BigDecimal("0.5")).pow(_diffusionDistance));
        
        if (CoreFunctions.Compare(restoringHp, eRelationType.LessThan, BigDecimal.ZERO)) // restoringHp must not be a negative value
            restoringHp = BigDecimal.ZERO;

        return new HealInfo(restoringHp.setScale(0, RoundingMode.FLOOR).intValue(), isCritical);
    }

    //private static void ApplyBuffAndDebuffStatusEffects(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    //{
    //    if (_system == null
    //        || _unit == null)
    //    {
    //        return;
    //    }

    //    ApplyBuffAndDebuffStatusEffects_Compound(ref _value, _system, _unit, _statusType, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //}

    private static BigDecimal ApplyBuffAndDebuffStatusEffects_Simple(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType)
    {
        if (_system == null || _unit == null)
            return BigDecimal.ZERO;

        List<BuffStatusEffect> buffStatusEffects = Linq.where(Linq.ofType(_unit.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == _statusType);
        List<DebuffStatusEffect> debuffStatusEffects = Linq.where(Linq.ofType(_unit.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == _statusType);

        AddPassiveSkillBuffAndDebuffStatusEffects(buffStatusEffects, debuffStatusEffects, _system, _unit, _statusType);
        AddEquipmentBuffAndDebuffStatusEffects(buffStatusEffects, debuffStatusEffects, _system, _unit, _statusType);

        _value = ApplyMultiplicativeBuffStatusEffects(_value, _unit, buffStatusEffects, _system);
        _value = ApplyMultiplicativeDebuffStatusEffects(_value, _unit, debuffStatusEffects, _system);

        _value = ApplySummativeBuffStatusEffects(_value, _unit, buffStatusEffects, _system);
        _value = ApplySummativeDebuffStatusEffects(_value, _unit, debuffStatusEffects, _system);
        
        return _value;
    }
    public static BigDecimal ApplyBuffAndDebuffStatusEffects_Simple(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, List<_2DCoord> _effectRange)
    {
    	return ApplyBuffAndDebuffStatusEffects_Simple(_value, _system, _unit, _statusType, _skillUser, _skill, null, _effectRange, null, null, null);
    }
    public static BigDecimal ApplyBuffAndDebuffStatusEffects_Simple(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return ApplyBuffAndDebuffStatusEffects_Simple(_value, _system, _unit, _statusType, _skillUser, _skill, null, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    }
    public static BigDecimal ApplyBuffAndDebuffStatusEffects_Simple(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_system == null
            || _unit == null
            || _skillUser == null
            || _effect == null)
        {
            return _value;
        }

        List<BuffStatusEffect> buffStatusEffects = new ArrayList<BuffStatusEffect>();
        List<DebuffStatusEffect> debuffStatusEffects = new ArrayList<DebuffStatusEffect>();

        // Get buff and debuff status effects
        buffStatusEffects = Linq.where(Linq.ofType(_unit.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == _statusType);
        debuffStatusEffects = Linq.where(Linq.ofType(_unit.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == _statusType);

        // Get status effects from skills if _skillUser
        if (_unit == _skillUser)
        {
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_skill.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                buffStatusEffects.add(new BuffStatusEffect(data, _skill.Level, true));
            }

            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_skill.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                debuffStatusEffects.add(new DebuffStatusEffect(data, _skill.Level, true));
            }
        }

        // Get status effects from passive skills and equipments
        AddPassiveSkillBuffAndDebuffStatusEffects(buffStatusEffects, debuffStatusEffects, _system, _unit, _statusType);
        AddEquipmentBuffAndDebuffStatusEffects(buffStatusEffects, debuffStatusEffects, _system, _unit, _statusType);

        // Apply multiplicative status effects
        _value = ApplyMultiplicativeBuffStatusEffects(_value, _unit, buffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplyMultiplicativeDebuffStatusEffects(_value, _unit, debuffStatusEffects, _system, _unit, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        // Apply summative status effects
        _value = ApplySummativeBuffStatusEffects(_value, _unit, buffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplySummativeDebuffStatusEffects(_value, _unit, debuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    
        return _value;
    }
    //private static void ApplyBuffAndDebuffStatusEffects_Compound(BigDecimal _value, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    //{
    //    if (_system == null
    //        || _unit == null
    //        || _skillUser == null
    //        || _effect == null
    //        || _effectRange == null
    //        || _targets == null
    //        || _target == null)
    //    {
    //        return;
    //    }

    //    if (_unit != _skillUser)
    //    {
    //        if (!(_skill.BaseInfo.getEffect() is UnitTargetingEffect))
    //            return;

    //        if (_unit != _target)
    //        {
    //            if (_secondaryTargetsForComplexTargetSelectionEffect == null || _unit != _secondaryTargetsForComplexTargetSelectionEffect)
    //                return;

    //            if (!(_skill.BaseInfo.getEffect() is IComplexTargetSelectionEffect) || !_applyEffectToSecondaryTargets)
    //                return;
    //        }
    //        else if (_applyEffectToSecondaryTargets)
    //            return;
    //    }
    //    else
    //    {
    //        if (_skill.BaseInfo.getEffect() is UnitTargetingEffect)
    //        {
    //            if (_secondaryTargetsForComplexTargetSelectionEffect == null)
    //                return;

    //            if (!_applyEffectToSecondaryTargets)
    //                return;

    //            if (_secondaryTargetsForComplexTargetSelectionEffect == null && _applyEffectToSecondaryTargets)
    //                return;
    //        }
    //    }

    //    //If it did not return, _unit is one of the entities involved : the execution of _skill and _effect.

    //    //Furthermore, if _unit is the _skillUser, either _skill.BaseInfo.getEffect() does not target objects of UnitInstance OR there exists an appropriate target UnitInstance.

    //    List<Object> targets = Linq.ofType(_targets, Object.class);

    //    List<BuffStatusEffect> skillUserBuffStatusEffects = new ArrayList<BuffStatusEffect>();
    //    List<DebuffStatusEffect> skillUserDebuffStatusEffects = new ArrayList<DebuffStatusEffect>();

    //    List<BuffStatusEffect> targetBuffStatusEffects = new ArrayList<BuffStatusEffect>();
    //    List<DebuffStatusEffect> targetDebuffStatusEffects = new ArrayList<DebuffStatusEffect>();

    //    // Get buff and debuff status effects for _skillUser
    //    skillUserBuffStatusEffects = _skillUser.getStatusEffects().stream().filter(x -> x instanceof BuffStatusEffect).mp(x -> (BuffStatusEffect)x).filter(x -> x.TargetStatusType == eStatusType.MaxHP);
    //    skillUserDebuffStatusEffects = _skillUser.getStatusEffects().stream().filter(x -> x instanceof DebuffStatusEffect).mp(x -> (DebuffStatusEffect)x).filter(x -> x.TargetStatusType == eStatusType.MaxHP);

    //    for (BuffStatusEffectData data : _skill.BaseInfo.getStatusEffectsData().stream().filter(x -> x instanceof BuffStatusEffect).mp(x -> (BuffStatusEffect)x).stream().filter(x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _skillUser)))
    //    {
    //        skillUserBuffStatusEffects.add(new BuffStatusEffect(data, null, _skill.Level));
    //    }

    //    for (DebuffStatusEffectData data : _skill.BaseInfo.getStatusEffectsData().stream().filter(x -> x instanceof DebuffStatusEffect).mp(x -> (DebuffStatusEffect)x).stream().filter(x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _skillUser)))
    //    {
    //        skillUserDebuffStatusEffects.add(new DebuffStatusEffect(data, null, _skill.Level));
    //    }

    //    AddPassiveSkillBuffAndDebuffStatusEffects(ref skillUserBuffStatusEffects, ref skillUserDebuffStatusEffects, _system, _skillUser, _statusType);
    //    AddEquipmentBuffAndDebuffStatusEffects(ref skillUserBuffStatusEffects, ref skillUserDebuffStatusEffects, _system, _skillUser, _statusType);

    //    // Apply multiplicative status effects for _skillUser
    //    ApplyMultiplicativeBuffStatusEffects(ref _value, _skillUser, skillUserBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //    ApplyMultiplicativeDebuffStatusEffects(ref _value, _skillUser, skillUserDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

    //    // Get buff and debuff status effects for target (if available).
    //    UnitInstance target;
    //    if (_skill.BaseInfo.getEffect() is UnitTargetingEffect)
    //    {
    //        target = (_applyEffectToSecondaryTargets) ? _secondaryTargetsForComplexTargetSelectionEffect as UnitInstance : _target as UnitInstance;

    //        targetBuffStatusEffects = target.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.MaxHP);
    //        targetDebuffStatusEffects = target.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.MaxHP);

    //        if (_statusType == eStatusType.FixedDamage) // It means that damage dealing effect called this function
    //        {
    //            targetBuffStatusEffects = target.getStatusEffects().OfType<BuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.DamageResistance).ToList();
    //            targetDebuffStatusEffects = target.getStatusEffects().OfType<DebuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.DamageResistance).ToList();
    //            AddPassiveSkillBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.DamageResistance);
    //            AddEquipmentBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.DamageResistance);
    //        }
    //        else if (_statusType == eStatusType.FixedHeal) // It means that healin effect called this function
    //        {
    //            targetBuffStatusEffects = target.getStatusEffects().OfType<BuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.FixedHeal_Self).ToList();
    //            targetDebuffStatusEffects = target.getStatusEffects().OfType<DebuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.FixedHeal_Self).ToList();
    //            AddPassiveSkillBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.FixedHeal_Self);
    //            AddEquipmentBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.FixedHeal_Self);
    //        }
    //        else if (_statusType == eStatusType.Cri)
    //        {
    //            targetBuffStatusEffects = target.getStatusEffects().OfType<BuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.CriRes).ToList();
    //            targetDebuffStatusEffects = target.getStatusEffects().OfType<DebuffStatusEffect>().stream().filter(x -> x.TargetStatusType == eStatusType.CriRes).ToList();
    //            AddPassiveSkillBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.CriRes);
    //            AddEquipmentBuffAndDebuffStatusEffects(ref targetBuffStatusEffects, ref targetDebuffStatusEffects, _system, target, eStatusType.CriRes);
    //        }

    //        // Apply multiplicative status effects for target
    //        ApplyMultiplicativeBuffStatusEffects(ref _value, target, targetBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //        ApplyMultiplicativeDebuffStatusEffects(ref _value, target, targetDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //    }

    //    // Apply summative status effects for _skillUser
    //    ApplySummativeBuffStatusEffects(ref _value, _skillUser, skillUserBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //    ApplySummativeDebuffStatusEffects(ref _value, _skillUser, skillUserDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

    //    if (target != null)
    //    {
    //        // Apply summative status effects for target
    //        ApplySummativeBuffStatusEffects(ref _value, target, targetBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //        ApplySummativeDebuffStatusEffects(ref _value, target, targetDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
    //    }
    //}
    private static BigDecimal ApplyBuffAndDebuffStatusEffects_Compound(BigDecimal _value, BattleSystemCore _system, eStatusType _statusType, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_system == null
            || _skillUser == null
            || _effect == null
            || _effectRange == null
            || _targets == null
            || _target == null)
        {
            return BigDecimal.ZERO;
        }

        if (_statusType != eStatusType.FixedDamage && _statusType != eStatusType.FixedHeal && _statusType != eStatusType.Cri)
            return BigDecimal.ZERO;

        if (!(_skill.BaseInfo.getEffect() instanceof UnitTargetingEffect))
            return BigDecimal.ZERO;

        List<Object> targets = Linq.cast(_targets, Object.class);
        List<Object> secondaryTargetsForComplexTargetSelectionEffect = Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, Object.class);
        
        List<BuffStatusEffect> skillUserBuffStatusEffects = new ArrayList<BuffStatusEffect>();
        List<DebuffStatusEffect> skillUserDebuffStatusEffects = new ArrayList<DebuffStatusEffect>();

        List<BuffStatusEffect> targetBuffStatusEffects = new ArrayList<BuffStatusEffect>();
        List<DebuffStatusEffect> targetDebuffStatusEffects = new ArrayList<DebuffStatusEffect>();

        // Get buff and debuff status effects for _skillUser
        skillUserBuffStatusEffects = Linq.where(Linq.ofType(_skillUser.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == _statusType);
        skillUserDebuffStatusEffects = Linq.where(Linq.ofType(_skillUser.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == _statusType);

        for (BuffStatusEffectData data : Linq.where(Linq.ofType(_skill.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _skillUser)))
        {
            skillUserBuffStatusEffects.add(new BuffStatusEffect(data, _skill.Level, true));
        }

        for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_skill.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _skillUser)))
        {
            skillUserDebuffStatusEffects.add(new DebuffStatusEffect(data, _skill.Level, true));
        }

        AddPassiveSkillBuffAndDebuffStatusEffects(skillUserBuffStatusEffects, skillUserDebuffStatusEffects, _system, _skillUser, _statusType);
        AddEquipmentBuffAndDebuffStatusEffects(skillUserBuffStatusEffects, skillUserDebuffStatusEffects, _system, _skillUser, _statusType);

        // Apply multiplicative status effects for _skillUser
        _value = ApplyMultiplicativeBuffStatusEffects(_value, _skillUser, skillUserBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplyMultiplicativeDebuffStatusEffects(_value, _skillUser, skillUserDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

        targetBuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == _statusType);
        targetDebuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == _statusType);

        if (_statusType == eStatusType.FixedDamage) // It means that damage dealing effect called this function
        {
            targetBuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.DamageResistance);
            targetDebuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.DamageResistance);
            AddPassiveSkillBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.DamageResistance);
            AddEquipmentBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.DamageResistance);
        }
        else if (_statusType == eStatusType.FixedHeal) // It means that healin effect called this function
        {
            targetBuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.FixedHeal_Self);
            targetDebuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.FixedHeal_Self);
            AddPassiveSkillBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.FixedHeal_Self);
            AddEquipmentBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.FixedHeal_Self);
        }
        else if (_statusType == eStatusType.Cri)
        {
            targetBuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), BuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.CriRes);
            targetDebuffStatusEffects = Linq.where(Linq.ofType(_target.getStatusEffects(), DebuffStatusEffect.class), x -> x.TargetStatusType == eStatusType.CriRes);
            AddPassiveSkillBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.CriRes);
            AddEquipmentBuffAndDebuffStatusEffects(targetBuffStatusEffects, targetDebuffStatusEffects, _system, _target, eStatusType.CriRes);
        }

        // Apply multiplicative status effects for target
        _value = ApplyMultiplicativeBuffStatusEffects(_value, _target, targetBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplyMultiplicativeDebuffStatusEffects(_value, _target, targetDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

        // Apply summative status effects for _skillUser
        _value = ApplySummativeBuffStatusEffects(_value, _skillUser, skillUserBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplySummativeDebuffStatusEffects(_value, _skillUser, skillUserDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

        // Apply summative status effects for target
        _value = ApplySummativeBuffStatusEffects(_value, _target, targetBuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);
        _value = ApplySummativeDebuffStatusEffects(_value, _target, targetDebuffStatusEffects, _system, _skillUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);
    
        return _value;
    }

    private static void AddPassiveSkillBuffAndDebuffStatusEffects(List<BuffStatusEffect> _buffStatusEffects, List<DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system, UnitInstance _unit, eStatusType _statusType)
    {
        if (_buffStatusEffects == null
            || _debuffStatusEffects == null
            || _system == null
            || _unit == null)
        {
            return;
        }

        for (PassiveSkill passiveSkill : Linq.ofType(_unit.getSkills(), PassiveSkill.class))
        {
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(passiveSkill.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data, passiveSkill.Level, true));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(passiveSkill.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data, passiveSkill.Level, true));
            }
        }

        if (_unit.InheritedSkill instanceof PassiveSkill)
        {
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_unit.InheritedSkill.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data, _unit.InheritedSkill.Level, true));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_unit.InheritedSkill.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _unit)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data, _unit.InheritedSkill.Level, true));
            }
        }
    }

    private static void AddEquipmentBuffAndDebuffStatusEffects(List<BuffStatusEffect> _buffStatusEffects, List<DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system, UnitInstance _equipmentOwner, eStatusType _statusType)
    {
        if (_buffStatusEffects == null
            || _debuffStatusEffects == null
            || _system == null
            || _equipmentOwner == null)
        {
            return;
        }

        if (_equipmentOwner.MainWeapon != null)
        {
            int mainWeaponLevel = Level(_equipmentOwner.MainWeapon);
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class),
            											x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data, mainWeaponLevel, false));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class),
            											x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data, mainWeaponLevel, false));
            }

            if (_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill != null && _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill instanceof PassiveSkill)
            {
                for (BuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
                {
                    _buffStatusEffects.add(new BuffStatusEffect(data, _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.Level, mainWeaponLevel));
                }
                for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
                {
                    _debuffStatusEffects.add(new DebuffStatusEffect(data, _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.Level, mainWeaponLevel));
                }
            }
        }

        if (_equipmentOwner.SubWeapon != null)
        {
            int subWeaponLevel = Level(_equipmentOwner.SubWeapon);
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.SubWeapon.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data, subWeaponLevel, false));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.SubWeapon.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data, subWeaponLevel, false));
            }
        }

        if (_equipmentOwner.Armour != null)
        {
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Armour.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Armour.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data));
            }
        }

        if (_equipmentOwner.Accessory != null)
        {
            for (BuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Accessory.BaseInfo.getStatusEffectsData(), BuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _buffStatusEffects.add(new BuffStatusEffect(data));
            }
            for (DebuffStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Accessory.BaseInfo.getStatusEffectsData(), DebuffStatusEffectData.class), x -> x.TargetStatusType == _statusType && _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner)))
            {
                _debuffStatusEffects.add(new DebuffStatusEffect(data));
            }
        }
    }

    private static BigDecimal ApplyMultiplicativeBuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<BuffStatusEffect> _buffStatusEffects, BattleSystemCore _system)
    {
    	return ApplyMultiplicativeBuffStatusEffects(_value, _effectHolder, _buffStatusEffects, _system, null, null, null, null, null, null, null);
    }
    private static BigDecimal ApplyMultiplicativeBuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<BuffStatusEffect> _buffStatusEffects, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectHolder == null || _buffStatusEffects == null || _system == null)
            return BigDecimal.ZERO;

        for (BuffStatusEffect bse : Linq.where(_buffStatusEffects, x -> !x.IsSum))
        {
            if (bse.getActivationCondition().IsTrue(_system, _effectHolder, bse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect))
            {
                BigDecimal effectValue = bse.getValue().ToValue(BigDecimal.class, _system, _effectHolder, bse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

                if (bse.TargetStatusType != eStatusType.DamageResistance && bse.TargetStatusType != eStatusType.CriRes)
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ONE)) // _value must not become negative. Furthermore, if effect value is less than 1, then multiplying _value by it means that _value decreases. That is, bse would no longer be a buff (positive) status effect.
                        _value = _value.multiply(effectValue);
                }
                else // As exception, _value must not increase.
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ONE))
                        _value = _value.divide(effectValue, 50, RoundingMode.HALF_UP);
                }

                if (bse.getDuration().ActivationTimes > 0)
                    bse.getDuration().ActivationTimes--; // Subtract one from the remaining activation times
            }
        }
        
        return _value;
    }
    private static BigDecimal ApplyMultiplicativeDebuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system)
    {
    	return ApplyMultiplicativeDebuffStatusEffects(_value, _effectHolder, _debuffStatusEffects, _system, null, null, null, null, null, null, null);
    }
    private static BigDecimal ApplyMultiplicativeDebuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<
        DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectHolder == null || _debuffStatusEffects == null || _system == null)
            return BigDecimal.ZERO;

        for (DebuffStatusEffect dse : Linq.where(_debuffStatusEffects, x -> !x.IsSum))
        {
            if (dse.getActivationCondition().IsTrue(_system, _effectHolder, dse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect))
            {
                BigDecimal effectValue = dse.getValue().ToValue(BigDecimal.class, _system, _effectHolder, dse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

                if (dse.TargetStatusType != eStatusType.DamageResistance && dse.TargetStatusType != eStatusType.CriRes)

                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ONE)) // _value must not become negative. Furthermore, if effect value is less than 1, then dividing _value by it means that _value increases. That is, dse would no longer be a debuff (negative) status effect.
                        _value = _value.divide(effectValue, 50, RoundingMode.HALF_UP);
                }
                else // As exception, _value must not decrease
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ONE))
                        _value = _value.multiply(effectValue);
                }

                if (dse.getDuration().ActivationTimes > 0)
                    dse.getDuration().ActivationTimes--; // Subtract one from the remaining activation times
            }
        }
        
        return _value;
    }

    private static BigDecimal ApplySummativeBuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<BuffStatusEffect> _buffStatusEffects, BattleSystemCore _system)
    {
    	return ApplyMultiplicativeBuffStatusEffects(_value, _effectHolder, _buffStatusEffects, _system, null, null, null, null, null, null, null);
    }
    private static BigDecimal ApplySummativeBuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<BuffStatusEffect> _buffStatusEffects, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectHolder == null || _buffStatusEffects == null || _system == null)
            return BigDecimal.ZERO;

        for (BuffStatusEffect bse : Linq.where(_buffStatusEffects, x -> x.IsSum))
        {
            if (bse.getActivationCondition().IsTrue(_system, _effectHolder, bse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect))
            {
                BigDecimal effectValue = bse.getValue().ToValue(BigDecimal.class, _system, _effectHolder, bse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

                if (bse.TargetStatusType != eStatusType.DamageResistance && bse.TargetStatusType != eStatusType.CriRes)
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ZERO)) // If effect value is negative, then adding it to _value means that _value decreases. That is, bse would no longer be a buff (positive) status effect.
                        _value = _value.add(effectValue);
                }
                else // As exception, _value must not increase. However, _value must not become negative either.
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.LessThanOrEqualTo, _value))
                        _value = _value.subtract(effectValue);
                }

                if (bse.getDuration().ActivationTimes > 0)
                    bse.getDuration().ActivationTimes--; // Subtract one from the remaining activation times
            }
        }
        
        return _value;
    }
    private static BigDecimal ApplySummativeDebuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system)
    {
    	return ApplyMultiplicativeDebuffStatusEffects(_value, _effectHolder, _debuffStatusEffects, _system, null, null, null, null, null, null, null);
    }
    private static BigDecimal ApplySummativeDebuffStatusEffects(BigDecimal _value, UnitInstance _effectHolder, List<DebuffStatusEffect> _debuffStatusEffects, BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectHolder == null || _debuffStatusEffects == null || _system == null)
            return BigDecimal.ZERO;

        for (DebuffStatusEffect dse : Linq.where(_debuffStatusEffects, x -> x.IsSum))
        {
            if (dse.getActivationCondition().IsTrue(_system, _effectHolder, dse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect))
            {
                BigDecimal effectValue = dse.getValue().ToValue(BigDecimal.class, _system, _effectHolder, dse, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

                if (dse.TargetStatusType != eStatusType.DamageResistance && dse.TargetStatusType != eStatusType.CriRes)
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, _value)) // _value must not become negative. Furthermore, if effect value is negative, then subtracting it from _value means that _value increases. That is, dse would no longer be a debuff (negative) status effect.
                        _value = _value.subtract(effectValue);
                }
                else // As exception, _valude must not decrease.
                {
                    if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ZERO))
                        _value = _value.add(effectValue);
                }

                if (dse.getDuration().ActivationTimes > 0)
                    dse.getDuration().ActivationTimes--; // Subtract one from the remaining activation times
            }
        }
        
        return _value;
    }

    //public static void ApplyMultipleBuffStatusEffects(BigDecimal _value, UnitInstance _unit, List<eStatusType> _statusTypes, BattleSystemCore _system, ActiveSkill _skill)
    //{
    //    _statusTypes = _statusTypes.Distinct().ToList();

    //    List<BuffStatusEffect> buffStatusEffects = new ArrayList<BuffStatusEffect>();
    //    for (eStatusType statusType : _statusTypes)
    //    {
    //        buffStatusEffects.addRange(_unit.getStatusEffects().OfType<BuffStatusEffect>().stream().filter(x -> x.TargetStatusType == statusType).ToList());

    //        if (_skill != null)
    //            buffStatusEffects.addRange(_skill.TemporalStatusEffects.OfType<BuffStatusEffect>().stream().filter(x -> x.TargetStatusType == statusType).ToList());
    //    }

    //    for (BuffStatusEffect bse : buffStatusEffects.stream().filter(x -> !x.IsSum))
    //    {
    //        if (bse.getActivationCondition().IsTrue(_system, _unit))
    //        {
    //            BigDecimal effectValue = bse.getValue().ToValue(BigDecimal.class, _system);
    //            if (effectValue > 0) // If effect value is 0 or negative, then multiplying _value by it means that _value decreases. That is, bse would no longer be a buff (positive) status effect.
    //                _value = _value.multiply(effectValue);
    //        }
    //    }

    //    for (BuffStatusEffect bse : buffStatusEffects.stream().filter(x -> x.IsSum))
    //    {
    //        if (bse.getActivationCondition().IsTrue(_system, _unit))
    //        {
    //            BigDecimal effectValue = bse.getValue().ToValue(BigDecimal.class, _system);
    //            if (CoreFunctions.Compare(effectValue, eRelationType.GreaterThanOrEqualTo, BigDecimal.ZERO)) // If effect value is negative, then adding it to _value means that _value decreases. That is, bse would no longer be a buff (positive) status effect.
    //                _value = _value.add(effectValue);
    //        }
    //    }

    //    for (BuffStatusEffect bse : buffStatusEffects)
    //    {
    //        if (bse.getDuration().ActivationTimes > 0) // If it could be activated more than once
    //            bse.getDuration().ActivationTimes--; // Subtract one from the remaining activation times
    //    }
    //}

    public static List<_2DCoord> ApplyTargetRangeModStatusEffects(List<_2DCoord> _targetRange, UnitInstance _effectHolder, boolean _isMovementRangeClassification, BattleSystemCore _system)
    {
        if (_targetRange == null
            || _effectHolder == null
            || _system == null)
        {
            return null;
        }

        return ApplyTargetRangeModStatusEffects_ActualDefinition(_targetRange, _effectHolder, _isMovementRangeClassification, _system, null, null);
    }
    public static List<_2DCoord> ApplyTargetRangeModStatusEffects(List<_2DCoord> _targetRange, UnitInstance _effectHolder, boolean _isMovementRangeClassification, BattleSystemCore _system, ActiveSkill _skill, Effect _effect)
    {
        if (_targetRange == null
            || _effectHolder == null
            || _system == null
            || _skill == null
            || _effect == null)
        {
            return null;
        }

        return ApplyTargetRangeModStatusEffects_ActualDefinition(_targetRange, _effectHolder, _isMovementRangeClassification, _system, _skill, _effect);
    }
    private static List<_2DCoord> ApplyTargetRangeModStatusEffects_ActualDefinition(List<_2DCoord> _targetRange, UnitInstance _effectHolder, boolean _isMovementRangeClassification, BattleSystemCore _system, Skill _skill, Effect _effect)
    {
        List<TargetRangeModStatusEffect> targetRangeModStatusEffects = Linq.where(Linq.ofType(_effectHolder.getStatusEffects(), TargetRangeModStatusEffect.class), x -> x.IsMovementRangeClassification == _isMovementRangeClassification);

        AddPassiveSkillTargetRangeModStatusEffects(targetRangeModStatusEffects, _system, _effectHolder);
        AddEquipmentTargetRangeModStatusEffects(targetRangeModStatusEffects, _system, _effectHolder);

        if (_skill != null && !_isMovementRangeClassification)
        {
            for (TargetRangeModStatusEffectData data : Linq.ofType(_skill.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data));
            }
        }

        // If there is at least one status effect such that its modification method is 'Overwrite,'
        // get a list of status effects where modification method is Overwrite.
        // Then apply only the last status effect : such list.
        if (Linq.any(targetRangeModStatusEffects, x -> x.ModificationMethod == eModificationMethod.Overwrite))
        {
            targetRangeModStatusEffects = Linq.where(targetRangeModStatusEffects, x -> x.ModificationMethod == eModificationMethod.Overwrite);
            _targetRange = TargetArea.GetTargetArea(targetRangeModStatusEffects.get(targetRangeModStatusEffects.size() - 1).TargetRangeClassification);
        }
        else
        {
            // Add each target range allowing duplicates
            for (TargetRangeModStatusEffect trmse : Linq.where(targetRangeModStatusEffects, x -> x.ModificationMethod == eModificationMethod.Add))
            {
                if (trmse.getActivationCondition().IsTrue(_system, _effectHolder, trmse, _effectHolder, _skill, _effect))
                    _targetRange.addAll(TargetArea.GetTargetArea(trmse.TargetRangeClassification));
            }

            // For each coordinate : each target range, subtract the first occurence of the coordinate from _targetRange if it contains the coordinate
            for (TargetRangeModStatusEffect trmse : Linq.where(targetRangeModStatusEffects, x -> x.ModificationMethod == eModificationMethod.Subtract))
            {
                if (trmse.getActivationCondition().IsTrue(_system, _effectHolder, trmse, _effectHolder, _skill, _effect))
                {
                    for (_2DCoord coord : TargetArea.GetTargetArea(trmse.TargetRangeClassification))
                    {
                        _targetRange.remove(coord);
                    }
                }
            }

            // Remove duplicates
            _targetRange = Linq.distinct(_targetRange);
        }
        
        return _targetRange;
    }

    private static void AddPassiveSkillTargetRangeModStatusEffects(List<TargetRangeModStatusEffect> _targetRangeModStatusEffects, BattleSystemCore _system, UnitInstance _unit)
    {
        if (_targetRangeModStatusEffects == null
            || _system == null
            || _unit == null)
        {
            return;
        }

        for (PassiveSkill passiveSkill : Linq.ofType(_unit.getSkills(), PassiveSkill.class))
        {
            for (TargetRangeModStatusEffectData data : Linq.ofType(passiveSkill.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data, passiveSkill.Level, true));
            }
        }

        if (_unit.InheritedSkill instanceof PassiveSkill)
        {
            for (TargetRangeModStatusEffectData data : Linq.ofType(_unit.InheritedSkill.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data, _unit.InheritedSkill.Level, true));
            }
        }
    }

    private static void AddEquipmentTargetRangeModStatusEffects(List<TargetRangeModStatusEffect> _targetRangeModStatusEffects, BattleSystemCore _system, UnitInstance _equipmentOwner)
    {
        if (_targetRangeModStatusEffects == null
            || _system == null
            || _equipmentOwner == null)
        {
            return;
        }

        if (_equipmentOwner.MainWeapon != null)
        {
            int mainWeaponLevel = Level(_equipmentOwner.MainWeapon);
            for (TargetRangeModStatusEffectData data : Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data, mainWeaponLevel, false));
            }

            if (_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill != null && _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill instanceof PassiveSkill)
            {
                for (TargetRangeModStatusEffectData data : Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
                {
                    _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data, _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.Level, mainWeaponLevel));
                }
            }
        }

        if (_equipmentOwner.SubWeapon != null)
        {
            int subWeaponLevel = Level(_equipmentOwner.SubWeapon);
            for (TargetRangeModStatusEffectData data : Linq.ofType(_equipmentOwner.SubWeapon.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data, subWeaponLevel, false));
            }
        }

        if (_equipmentOwner.Armour != null)
        {
            for (TargetRangeModStatusEffectData data : Linq.ofType(_equipmentOwner.Armour.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data));
            }
        }

        if (_equipmentOwner.Accessory != null)
        {
            for (TargetRangeModStatusEffectData data : Linq.ofType(_equipmentOwner.Accessory.BaseInfo.getStatusEffectsData(), TargetRangeModStatusEffectData.class))
            {
                _targetRangeModStatusEffects.add(new TargetRangeModStatusEffect(data));
            }
        }
    }

    public static void AddPassiveSkillForegroundStatusEffects(List<ForegroundStatusEffect> _foregroundStatusEffects, BattleSystemCore _system, UnitInstance _unit, eEventTriggerTiming _eventTriggerTiming)
    {
        if (_foregroundStatusEffects == null
            || _system == null
            || _unit == null)
        {
            return;
        }

        for (PassiveSkill passiveSkill : Linq.ofType(_unit.getSkills(), PassiveSkill.class))
        {
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(passiveSkill.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
            		x -> _system.DoesStatusEffectActivationPhaseMatch(x, _unit, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data, passiveSkill.Level, true));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data, passiveSkill.Level, true));
            }
        }

        if (_unit.InheritedSkill instanceof PassiveSkill)
        {
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_unit.InheritedSkill.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
                x -> _system.DoesStatusEffectActivationPhaseMatch(x, _unit, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data, _unit.InheritedSkill.Level, true));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data, _unit.InheritedSkill.Level, true));
            }
        }
    }

    public static void AddEquipmentForegroundStatusEffects(List<ForegroundStatusEffect> _foregroundStatusEffects, BattleSystemCore _system, UnitInstance _equipmentOwner, eEventTriggerTiming _eventTriggerTiming)
    {
        if (_foregroundStatusEffects == null
            || _system == null
            || _equipmentOwner == null)
        {
            return;
        }

        if (_equipmentOwner.MainWeapon != null)
        {
            int mainWeaponLevel = Level(_equipmentOwner.MainWeapon);
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
                												x -> _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data, mainWeaponLevel, false));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data, mainWeaponLevel, false));
            }

            if (_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill != null && _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill instanceof PassiveSkill)
            {
                for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
                    												x -> _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner, _eventTriggerTiming)))
                {
                    if (data instanceof DamageStatusEffectData)
                        _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data, _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.Level, mainWeaponLevel));
                    else if (data instanceof HealStatusEffectData)
                        _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data, _equipmentOwner.MainWeapon.BaseInfo.MainWeaponSkill.Level, mainWeaponLevel));
                }
            }
        }

        if (_equipmentOwner.SubWeapon != null)
        {
            int subWeaponLevel = Level(_equipmentOwner.SubWeapon);
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.SubWeapon.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
                												x -> _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data, subWeaponLevel, false));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data, subWeaponLevel, false));
            }
        }

        if (_equipmentOwner.Armour != null)
        {
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Armour.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
                												x -> _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data));
            }
        }

        if (_equipmentOwner.Accessory != null)
        {
            for (ForegroundStatusEffectData data : Linq.where(Linq.ofType(_equipmentOwner.Accessory.BaseInfo.getStatusEffectsData(), ForegroundStatusEffectData.class),
            													x -> _system.DoesStatusEffectActivationPhaseMatch(x, _equipmentOwner, _eventTriggerTiming)))
            {
                if (data instanceof DamageStatusEffectData)
                    _foregroundStatusEffects.add(new DamageStatusEffect((DamageStatusEffectData)data));
                else if (data instanceof HealStatusEffectData)
                    _foregroundStatusEffects.add(new HealStatusEffect((HealStatusEffectData)data));
            }
        }
    }

    // Example:
    // 0 represents the initial position and other numbers show the distance.
    // 6 5 4 3 4 5 6
    // 5 4 3 2 3 4 5
    // 4 3 2 1 2 3 4
    // 3 2 1 0 1 2 3
    // 4 3 2 1 2 3 4
    // 5 4 3 2 3 4 5
    // 6 5 4 3 4 5 6
    public static int distance(_2DCoord _initialCoord, _2DCoord _targetCoord)
    {
    	int xDistance = Math.abs(_initialCoord.getX() - _targetCoord.getX());
    	int yDistance = Math.abs(_initialCoord.getY() - _targetCoord.getY());
    	
    	return xDistance + yDistance;
    }
    
    public static List<_2DCoord> coordsInDistance(_2DCoord _referenceCoord, int _distance)
    {
    	List<_2DCoord> result = new ArrayList<_2DCoord>();
    	
    	for (int y = 0; y < CoreValues.SIZE_OF_A_SIDE_OF_BOARD; y++)
    	{
    		for (int x = 0; x < CoreValues.SIZE_OF_A_SIDE_OF_BOARD; x++)
    		{
    			_2DCoord coord = new _2DCoord(x, y);
    			if (distance(_referenceCoord, coord) == _distance)
    				result.add(coord);
    		}
    	}
    	
    	return result;
    }

    public static List<_2DCoord> coordsBetween(_2DCoord _a, _2DCoord _b)
    {
    	List<_2DCoord> result = new ArrayList<_2DCoord>();
    	
    	if (_a.X == _b.X)
    	{
    		int yDistance = Math.abs(_a.Y - _b.Y);
    		int lowerY = (_a.Y < _b.Y) ? _a.Y : _b.Y;
    		for (int i = 1; i <= yDistance - 1; i++) //The number of coords between the given two coords is yDistance - 1
    		{
    			result.add(new _2DCoord(_a.X, lowerY + i));
    		}
    	}
    	else if (_a.Y == _b.Y)
    	{
    		int xDistance = Math.abs(_a.X - _b.X);
    		int lowerX = (_a.X < _b.X) ? _a.X : _b.X;
    		for (int i = 1; i <= xDistance - 1; i++) //The number of coords between the given two coords is xDistance - 1
    		{
    			result.add(new _2DCoord(lowerX + i, _a.Y));
    		}
    	}
    	else
    	{
    		int yDistance = Math.abs(_a.Y - _b.Y);
    		int xDistance = Math.abs(_a.X - _b.X);
    		if (yDistance == xDistance) //Diagonally connected
    		{
        		int lowerY = (_a.Y < _b.Y) ? _a.Y : _b.Y;
    			int lowerX = (_a.X < _b.X) ? _a.X : _b.X;
    			
    			for (int i = 1; i <= xDistance - 1; i++)
    			{
    				result.add(new _2DCoord(lowerX + i, lowerY + i));
    			}
    		}
    	}
    	
    	return result;
    }
    
    public static EffectivenessInfo DamageCorrectionRate_Element(UnitInstance _attacker, eElement _effectElement, UnitInstance _defender)
    {
        BigDecimal correctionRate = BigDecimal.ONE;

        if (DoesElementMatch(_attacker.BaseInfo.getElements(), _effectElement))
            correctionRate = correctionRate.multiply(CoreValues.MULTIPLIER_FOR_ELEMENT_MATCH);

        EffectivenessInfo effectivenessInfo = ElementEffectiveness(_attacker, _effectElement, _defender);

        correctionRate = correctionRate.multiply(effectivenessInfo.correctionRate);
        
        return new EffectivenessInfo(effectivenessInfo.effectiveness, correctionRate);
    }

    public static boolean DoesSucceed(BattleSystemCore _system, UnitInstance _skillUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_system == null
            || _skillUser == null
            || _skill == null
            || _effect == null
            || _effectRange == null)
        {
            return false;
        }

        BigDecimal successRate = _effect.getSuccessRate().ToValue(BigDecimal.class, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        BigDecimal precision = Precision(_skillUser, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        if (CoreFunctions.Compare(precision, eRelationType.LessThanOrEqualTo, BigDecimal.ZERO))
            return false;
        else
            successRate = successRate.multiply(precision);

        if (_target != null)
        {
            BigDecimal evasion = Evasion(_skillUser, _system, _skillUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
            if (CoreFunctions.Compare(evasion, eRelationType.LessThanOrEqualTo, BigDecimal.ZERO))
                return true;
            else
                successRate = successRate.divide(evasion, 50, RoundingMode.HALF_UP);
        }
        
        return isSuccess(successRate);
    }

    /// <summary>
    /// PreCondition: _attacker, _defender, and _effect have been initialized successfully.
    /// PostCondition: A boolean value representing whether the action(or skill) was critical will be returned based on the properties of _attacker, _defender, and _effect.
    /// </summary>
    /// <param name="_attacker"></param>
    /// <param name="_defender"></param>
    /// <param name="_effect"></param>
    /// <returns></returns>
    public static boolean IsCritical(UnitInstance _attacker, ActiveSkill _skill, DamageEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect, BattleSystemCore _system)
    {
        if (_attacker == null
            || _skill == null
            || _effect == null
            || _effectRange == null
            || _target == null
            || _system == null)
        {
            return false;
        }

        BigDecimal criticalRate = CoreValues.DEFAULT_CRITICAL_RATE;

        criticalRate = ApplyBuffAndDebuffStatusEffects_Compound(criticalRate, _system, eStatusType.Cri, _attacker, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return isSuccess(criticalRate);
    }

    public static boolean IsCritical(UnitInstance _effectUser, ActiveSkill _skill, HealEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect, BattleSystemCore _system)
    {
        if (_effectUser == null
            || _target == null
            || _effect == null)
        {
            return false;
        }

        BigDecimal criticalRate = CoreValues.DEFAULT_CRITICAL_RATE;

        criticalRate = ApplyBuffAndDebuffStatusEffects_Compound(criticalRate, _system, eStatusType.Cri, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        return isSuccess(criticalRate);
    }
    //End Public Methods

    //Private Methods
    private static BigDecimal CorrectionRate_Force(BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, DamageEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_system == null
            || _effectUser == null
            || _skill == null
            || _effect == null
            || _target == null
            || _effectRange == null)
        {
            return BigDecimal.ZERO;
        }
        
        try
        {
            List<Object> targets = Linq.cast(_targets, Object.class);
            List<Object> secondaryTargetsForComplexTargetSelectionEffect = Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, Object.class);
            
            BigDecimal force = _effect.getValue().ToValue(BigDecimal.class, _system, _effectUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

            if (CoreFunctions.Compare(force, eRelationType.GreaterThan, BigDecimal.ZERO))
            	force = CoreValues.POW_ADJUSTMENT_CONST_A.multiply(force.pow(2)).add(CoreValues.POW_ADJUSTMENT_CONST_B); // A = 19/9999 and B = (1 - A) so that this equation equals 1 when force is 1 

            force = ApplyBuffAndDebuffStatusEffects_Simple(force, _system, _effectUser, eStatusType.DamageForce, _effectUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

            return force;
        }
        catch (Exception ex)
        {
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal CorrectionRate_Force(BattleSystemCore _system, UnitInstance _effectUser, ActiveSkill _skill, HealEffect _effect, List<_2DCoord> _effectRange, List<UnitInstance> _targets, UnitInstance _target, List<UnitInstance> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_system == null
            || _effectUser == null
            || _skill == null
            || _effect == null
            || _target == null
            || _effectRange == null)
        {
            return BigDecimal.ZERO;
        }

        try
        {
            List<Object> targets = Linq.cast(_targets, Object.class);
            List<Object> secondaryTargetsForComplexTargetSelectionEffect = Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, Object.class);
            
            BigDecimal force = _effect.getValue().ToValue(BigDecimal.class, _system, _effectUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

            if (CoreFunctions.Compare(force, eRelationType.GreaterThan, BigDecimal.ZERO))
                force = CoreValues.POW_ADJUSTMENT_CONST_A.multiply(force.pow(2)).add(CoreValues.POW_ADJUSTMENT_CONST_B); // A = 19/9999 and B = (1 - A) so that this equation equals 1 when force is 1 

            force = ApplyBuffAndDebuffStatusEffects_Simple(force, _system, _effectUser, eStatusType.HealForce, _effectUser, _skill, _effect, _effectRange, targets, _target, secondaryTargetsForComplexTargetSelectionEffect);

            return force;
        }
        catch (Exception ex)
        {
            return BigDecimal.ZERO;
        }
    }

    private static EffectivenessInfo ElementEffectiveness(UnitInstance _attacker, eElement _effectElement, UnitInstance _defender)
    {
        eEffectiveness effectiveness = eEffectiveness.Neutral;
        BigDecimal effectivenessValue = BigDecimal.ONE;
        
        int effectiveCount = 0;
        int ineffectiveCount = 0;

        //Effect base effectivenessValue
        for (eElement targetElement : _defender.BaseInfo.getElements())
        {
            switch (_effectElement)
            {
                case Blue:
                    if (targetElement == eElement.Red)
                        effectiveCount++;
                    else if (targetElement == eElement.Ocher)
                        ineffectiveCount++;
                    break;
                case Red:
                    if (targetElement == eElement.Green)
                        effectiveCount++;
                    else if (targetElement == eElement.Blue)
                        ineffectiveCount++;
                    break;
                case Green:
                    if (targetElement == eElement.Ocher)
                        effectiveCount++;
                    else if (targetElement == eElement.Red)
                        ineffectiveCount++;
                    break;
                case Ocher:
                    if (targetElement == eElement.Blue)
                        effectiveCount++;
                    else if (targetElement == eElement.Green)
                        ineffectiveCount++;
                    break;
                case Purple:
                    if (targetElement == eElement.Yellow)
                        effectiveCount++;
                    break;
                case Yellow:
                    if (targetElement == eElement.Purple)
                        effectiveCount++;
                    break;
                default: //case None
                	break;
            }
        }

        if (effectiveCount > ineffectiveCount)
        {
            effectiveness = eEffectiveness.Effective;
            effectivenessValue = BigDecimalExtension.multiply(CoreValues.MULTIPLIER_FOR_EFFECTIVE_ELEMENT, effectiveCount - ineffectiveCount);
        }
        else if (effectiveCount < ineffectiveCount)
        {
            effectiveness = eEffectiveness.Ineffective;
            effectivenessValue = BigDecimalExtension.multiply(CoreValues.MULTIPLIER_FOR_INEFFECTIVE_ELEMENT, ineffectiveCount - effectiveCount);
        }

        return new EffectivenessInfo(effectiveness, effectivenessValue);
    }

    private static boolean DoesElementMatch(List<eElement> _unitElements, eElement _targetElement)
    {
        for (eElement e : _unitElements)
        {
            if (e == _targetElement)
                return true;
        }

        return false;
    }

    private static BigDecimal CorrectionRate_TileType(UnitInstance _unit, Effect _effect, eTileType _tileType)
    {
        switch (_tileType)
        {
            case Blue:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Blue))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Red:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Red))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Green:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Green))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Ocher:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Ocher))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Purple:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Purple))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Yellow:
                if (DoesElementMatch(_unit.BaseInfo.getElements(), eElement.Yellow))
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            case Heal:
                if (_effect instanceof HealEffect)
                    return CoreValues.MULTIPLIER_FOR_TILETYPEMATCH;
                break;
            default: //case Normal
                return BigDecimal.ONE;
        }

        return BigDecimal.ONE;
    }
    
    private static boolean isSuccess(BigDecimal _value)
    {
        if (CoreFunctions.Compare(_value, eRelationType.LessThanOrEqualTo, BigDecimal.ZERO))
            return false;
        else if (CoreFunctions.Compare(_value, eRelationType.GreaterThanOrEqualTo, BigDecimal.ONE))
            return true;
        else
        {
            int rangeNumber = BigDecimalExtension.multiply(_value, 10000).setScale(0, RoundingMode.HALF_UP).intValue();

            MTRandom.randInit();
            int randomNumber = MTRandom.getRand(1, 10000);

            if (randomNumber < rangeNumber)
                return true;
            else
                return false;
        }
    }
    //End Private Methods
}