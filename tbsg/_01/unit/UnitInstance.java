package eean_games.tbsg._01.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eean_games.main.Linq;
import eean_games.main.extension_method.ListExtension;
import eean_games.tbsg._01.Calculator;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.recipe.UnitEvolutionRecipe;
import eean_games.tbsg._01.skill.CostRequiringSkill;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.SkillData;
import eean_games.tbsg._01.status_effect.StatusEffect;

public final class UnitInstance extends Unit implements Cloneable
{
    public UnitInstance(int _baseId, int _uniqueId, byte[] _iconAsByteArray,
        String _name, eGender _gender, eRarity _rarity, String _nickname,
        eTargetRangeClassification _movementRangeClassification, eTargetRangeClassification _nonMovementActionRangeClassification, List<eElement> _elements, List<eWeaponClassification> _equipableWeaponClassifications, List<eArmourClassification> _equipableArmourClassifications, List<eAccessoryClassification> _equipableAccessoryClassifications,
        int _accumulatedExp, int _maxLvHP, int _maxLvPhyStr, int _maxLvPhyRes, int _maxLvMagStr, int _maxLvMagRes, int _maxLvVit, List<SkillData> _skills, Map<Integer, Integer> _skillLevels, List<String> _labels, String _description, List<UnitEvolutionRecipe> _progressiveEvolutionRecipes, UnitEvolutionRecipe _retrogressiveEvolutionRecipe,
        Weapon _mainWeapon, Weapon _subWeapon, Armour _armour, Accessory _accessory, PlayerOnBoard _ownerInstance)
    {
    	super(_baseId, _uniqueId, _name, _iconAsByteArray, _gender, _rarity, _nickname, _movementRangeClassification, _nonMovementActionRangeClassification, _elements, _equipableWeaponClassifications, _equipableArmourClassifications, _equipableAccessoryClassifications, _accumulatedExp, _maxLvHP, _maxLvPhyStr, _maxLvPhyRes, _maxLvMagStr, _maxLvMagRes, _maxLvVit, false, _skills, _skillLevels, _labels, _description, _progressiveEvolutionRecipes, _retrogressiveEvolutionRecipe, _mainWeapon, _subWeapon, _armour, _accessory);
    	
        IsAlive = true;
        RemainingHP = Calculator.MaxHP(this);
        StatusEffects = new ArrayList<StatusEffect>();

        InheritedSkill = null;

        MainWeapon = (_mainWeapon != null) ? _mainWeapon.DeepCopy() : null;
        SubWeapon = (_subWeapon != null) ? _subWeapon.DeepCopy() : null;
        Armour = (_armour != null) ? _armour.DeepCopy() : null;
        Accessory = (_accessory != null) ? _accessory.DeepCopy() : null;

        OwnerInstance = NullPreventionAssignmentMethods.CoalesceNull(_ownerInstance);
    }

    public UnitInstance(Unit _baseUnit, PlayerOnBoard _ownerInstance)
    {
    	super(_baseUnit.BaseInfo, _baseUnit.UniqueId, _baseUnit.Nickname, _baseUnit.getAccumulatedExperience(), false, _baseUnit.getSkills());
    	
        IsAlive = true;
        RemainingHP = Calculator.MaxHP(this);
        StatusEffects = new ArrayList<StatusEffect>();

        InheritedSkill = (_baseUnit.SkillInheritor != null) ? Linq.first(_baseUnit.SkillInheritor.getSkills(), x -> x.BaseInfo.Id == _baseUnit.InheritingSkillId) : null;

        MainWeapon = (_baseUnit.mainWeapon != null) ? _baseUnit.mainWeapon.DeepCopy() : null;
        SubWeapon = (_baseUnit.subWeapon != null) ? _baseUnit.subWeapon.DeepCopy() : null;
        Armour = (_baseUnit.armour != null) ? _baseUnit.armour.DeepCopy() : null;
        Accessory = (_baseUnit.accessory != null) ? _baseUnit.accessory.DeepCopy() : null;

        OwnerInstance = NullPreventionAssignmentMethods.CoalesceNull(_ownerInstance);
    }

    //Public Fields
    public boolean IsAlive;

    public int RemainingHP;
    
    public Weapon MainWeapon;
    public Weapon SubWeapon;
    public Armour Armour;
    public Accessory Accessory;
    //End Public Fields
    
    //Public Read-only Fields
    public final Skill InheritedSkill; // Store reference to the original instance of Skill. Can be null.
    
    public final PlayerOnBoard OwnerInstance; // Get reference to the actual object
    //End Public Read-only Fields
    
    //Getters
    public List<StatusEffect> getStatusEffects() { return Collections.unmodifiableList(StatusEffects); }
    //End Getters
    
    //Private Fields
    private List<StatusEffect> StatusEffects;
    //End Private Fields

    //Public Methods
    public boolean IsMainWeaponSkill(Skill _skill) { return MainWeapon.BaseInfo.MainWeaponSkill == _skill; }

    public boolean AreResourcesEnoughForSkillExecution(CostRequiringSkill _skill)
    {
        try
        {
        	CostRequiringSkill skill = (CostRequiringSkill)(Linq.first(getSkills(), x -> x == _skill));
            if (skill == null 
                && MainWeapon.BaseInfo.MainWeaponSkill != null 
                && MainWeapon.BaseInfo.MainWeaponSkill instanceof CostRequiringSkill
                && MainWeapon.BaseInfo.MainWeaponSkill == _skill)
            {
                skill = (CostRequiringSkill)MainWeapon.BaseInfo.MainWeaponSkill;
            }
            if (skill == null)
                return false;

            if (skill.BaseInfo.SPCost <= OwnerInstance.RemainingSP && OwnerInstance.HasRequiredItems(skill.BaseInfo.getItemCosts()))
                return true;

            return false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
    public boolean AreResourcesEnoughForSkillExecution(String _skillName)
    {
        try
        {
        	CostRequiringSkill skill = Linq.firstOrDefault(Linq.ofType(getSkills(), CostRequiringSkill.class), x -> x.BaseInfo.Name == _skillName);
            if (skill == null
                && MainWeapon.BaseInfo.MainWeaponSkill != null
                && MainWeapon.BaseInfo.MainWeaponSkill instanceof CostRequiringSkill
                && MainWeapon.BaseInfo.MainWeaponSkill.BaseInfo.Name == _skillName)
            {
                skill = (CostRequiringSkill)MainWeapon.BaseInfo.MainWeaponSkill;
            }
            if (skill == null)
                return false;

            if (skill.BaseInfo.SPCost <= OwnerInstance.RemainingSP && OwnerInstance.HasRequiredItems(skill.BaseInfo.getItemCosts()))
                return true;

            return false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public UnitInstance DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected UnitInstance DeepCopyInternally()
    {
        UnitInstance copy = (UnitInstance)super.DeepCopyInternally();

        copy.StatusEffects = ListExtension.DeepCopy(StatusEffects);

        copy.MainWeapon = MainWeapon.DeepCopy();
        copy.SubWeapon = SubWeapon.DeepCopy();
        copy.Armour = Armour.DeepCopy();
        copy.Accessory = Accessory.DeepCopy();

        return copy;
    }
    //End Protected Methods
}