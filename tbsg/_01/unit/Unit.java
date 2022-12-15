package eean_games.tbsg._01.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
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
import eean_games.tbsg._01.recipe.UnitEvolutionRecipe;
import eean_games.tbsg._01.skill.CounterSkill;
import eean_games.tbsg._01.skill.CounterSkillData;
import eean_games.tbsg._01.skill.OrdinarySkill;
import eean_games.tbsg._01.skill.OrdinarySkillData;
import eean_games.tbsg._01.skill.PassiveSkill;
import eean_games.tbsg._01.skill.PassiveSkillData;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.SkillData;
import eean_games.tbsg._01.skill.UltimateSkill;
import eean_games.tbsg._01.skill.UltimateSkillData;;

public class Unit implements DeepCopyable<Unit>, Cloneable
{
    public Unit(int _baseId, int _uniqueId, String _name, byte[] _iconAsByteArray, eGender _gender, eRarity _rarity,
        String _nickname, eTargetRangeClassification _movementRangeClassification, eTargetRangeClassification _nonMovementActionRangeClassification, List<eElement> _elements,
        List<eWeaponClassification> _equipableWeaponClassifications, List<eArmourClassification> _equipableArmourClassifications, List<eAccessoryClassification> _equipableAccessoryClassifications,
        int _accumulatedExp, int _maxLvHP, int _maxLvPhyStr, int _maxLvPhyRes, int _maxLvMagStr, int _maxLvMagRes, int _maxLvVit, boolean _isLocked,
        List<SkillData> _skills, Map<Integer, Integer> _skillLevels, List<String> _labels, String _description,
        List<UnitEvolutionRecipe> _progressiveEvolutionRecipes)
    {
    	this(_baseId, _uniqueId, _name, _iconAsByteArray, _gender, _rarity,
    			_nickname, _movementRangeClassification, _nonMovementActionRangeClassification, _elements, 
    			_equipableWeaponClassifications, _equipableArmourClassifications, _equipableAccessoryClassifications, 
    			_accumulatedExp, _maxLvHP, _maxLvPhyStr, _maxLvPhyRes, _maxLvMagStr, _maxLvMagRes, _maxLvVit, _isLocked,
    			_skills, _skillLevels, _labels, _description,
    			_progressiveEvolutionRecipes, null,
    			null, null, null, null);
    }
    public Unit(int _baseId, int _uniqueId, String _name, byte[] _iconAsByteArray, eGender _gender, eRarity _rarity,
            String _nickname, eTargetRangeClassification _movementRangeClassification, eTargetRangeClassification _nonMovementActionRangeClassification, List<eElement> _elements,
            List<eWeaponClassification> _equipableWeaponClassifications, List<eArmourClassification> _equipableArmourClassifications, List<eAccessoryClassification> _equipableAccessoryClassifications,
            int _accumulatedExp, int _maxLvHP, int _maxLvPhyStr, int _maxLvPhyRes, int _maxLvMagStr, int _maxLvMagRes, int _maxLvVit, boolean _isLocked,
            List<SkillData> _skills, Map<Integer, Integer> _skillLevels, List<String> _labels, String _description,
            List<UnitEvolutionRecipe> _progressiveEvolutionRecipes, UnitEvolutionRecipe _retrogressiveEvolutionRecipe,
            Weapon _mainWeapon, Weapon _subWeapon, Armour _armour, Accessory _accessory)
    {
        BaseInfo = new UnitData(_baseId, _name, _iconAsByteArray, _gender, _rarity,
            _movementRangeClassification, _nonMovementActionRangeClassification, _elements,
            _equipableWeaponClassifications, _equipableArmourClassifications, _equipableAccessoryClassifications,
            _maxLvHP, _maxLvPhyStr, _maxLvPhyRes, _maxLvMagStr, _maxLvMagRes, _maxLvVit,
            _skills, _labels, _description, _progressiveEvolutionRecipes, _retrogressiveEvolutionRecipe);

        UniqueId = _uniqueId;

        Nickname = StringExtension.CoalesceNull(_nickname);

        AccumulatedExperience = _accumulatedExp;
        
        IsLocked = _isLocked;

        m_skills = new ArrayList<Skill>();
        for (SkillData skillData : _skills)
        {
            int level = 1;
            if (_skillLevels.containsKey(skillData.Id))
            	level = _skillLevels.get(skillData.Id);

            if (skillData instanceof OrdinarySkillData)
                m_skills.add(new OrdinarySkill((OrdinarySkillData)skillData, level));
            else if (skillData instanceof CounterSkillData)
                m_skills.add(new CounterSkill((CounterSkillData)skillData, level));
            else if (skillData instanceof UltimateSkillData)
                m_skills.add(new UltimateSkill((UltimateSkillData)skillData, level));
            else // skillData is PassiveSkillData
                m_skills.add(new PassiveSkill((PassiveSkillData)skillData, level));
        }
        
        mainWeapon = _mainWeapon;
        subWeapon = _subWeapon;
        armour = _armour;
        accessory = _accessory;
        
        SkillInheritor = null;
        InheritingSkillId = 0;
    }

    public Unit(UnitData _unitData, int _uniqueId, String _nickname, int _accumulatedExp, boolean _isLocked)
    {
    	this(_unitData, _uniqueId, _nickname, _accumulatedExp, _isLocked, (Map<Integer, Integer>)null, null, null, null, null);
    }
    public Unit(UnitData _unitData, int _uniqueId, String _nickname, int _accumulatedExp, boolean _isLocked, Map<Integer, Integer> _skillLevels, Weapon _mainWeapon, Weapon _subWeapon, Armour _armour, Accessory _accessory)
    {
    	BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_unitData, false);

        UniqueId = _uniqueId;

        Nickname = StringExtension.CoalesceNull(_nickname);

        AccumulatedExperience = _accumulatedExp;
        
        IsLocked = _isLocked;

        m_skills = new ArrayList<Skill>();
        for (SkillData skillData : _unitData.getSkills())
        {
            int level = 1;
            if (_skillLevels != null && _skillLevels.containsKey(skillData.Id))
            	level = _skillLevels.get(skillData.Id);

            if (skillData instanceof OrdinarySkillData)
                m_skills.add(new OrdinarySkill((OrdinarySkillData)skillData, level));
            else if (skillData instanceof CounterSkillData)
                m_skills.add(new CounterSkill((CounterSkillData)skillData, level));
            else if (skillData instanceof UltimateSkillData)
                m_skills.add(new UltimateSkill((UltimateSkillData)skillData, level));
            else // skillData is PassiveSkillData
                m_skills.add(new PassiveSkill((PassiveSkillData)skillData, level));
        }
        
        mainWeapon = _mainWeapon;
        subWeapon = _subWeapon;
        armour = _armour;
        accessory = _accessory;
        
        SkillInheritor = null;
        InheritingSkillId = 0;
    }
    
    public Unit(UnitData _unitData, int _uniqueId, String _nickname, int _accumulatedExp, boolean _isLocked, List<Skill> _skills)
    {
    	this(_unitData, _uniqueId, _nickname, _accumulatedExp, _isLocked, _skills, null, null, null, null);
    }
    public Unit(UnitData _unitData, int _uniqueId, String _nickname, int _accumulatedExp, boolean _isLocked, List<Skill> _skills, Weapon _mainWeapon, Weapon _subWeapon, Armour _armour, Accessory _accessory)
    {
        BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_unitData, false);

        UniqueId = _uniqueId;

        Nickname = StringExtension.CoalesceNull(_nickname);

        AccumulatedExperience = _accumulatedExp;
        
        IsLocked = _isLocked;

        m_skills = ListExtension.CoalesceNullAndReturnCopyOptionally(_skills, eCopyType.Shallow);
        
        mainWeapon = _mainWeapon;
        subWeapon = _subWeapon;
        armour = _armour;
        accessory = _accessory;
        
        SkillInheritor = null;
        InheritingSkillId = 0;
    }

    //Public Read-only Fields
    public final UnitData BaseInfo; //All Units with same baseInfo will reference the same instance of UnitData
    
    public final int UniqueId;
    //End Public Read-only Fields
    
    //Public Fields
    public String Nickname;
    public boolean IsLocked;
    
    public Weapon mainWeapon; //Store actual reference to the Weapon
    public Weapon subWeapon; //Store actual reference to the Weapon
    public Armour armour; //Store actual reference to the Armour
    public Accessory accessory; //Store actual reference to the Accessory
    
    //The actual values for the two fields below will not be set through constructor
    public Unit SkillInheritor; // Store actual reference to the Unit
    public int InheritingSkillId;
    //End Public Fields
    
    //Getters
    public int getAccumulatedExperience() { return AccumulatedExperience; }
    
    public List<Skill> getSkills() { return Collections.unmodifiableList(m_skills); }
    //End Getters
    
    //Private Fields
    private int AccumulatedExperience;
    //End Private Fields
    
    //Private Read-only Fields
    private List<Skill> m_skills;
    //End Private Read-only Fields

    //Public Methods
    public void GainExperience(int _exp)
    {
        if (_exp <= 0 || AccumulatedExperience == Integer.MAX_VALUE)
            return;

        if (AccumulatedExperience + _exp >= Integer.MAX_VALUE)
            AccumulatedExperience = Integer.MAX_VALUE;
        else
            AccumulatedExperience += _exp;
    } 

    public Unit DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected Unit DeepCopyInternally()
    {
    	try {
			return (Unit)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}
