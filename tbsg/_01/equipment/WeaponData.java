package eean_games.tbsg._01.equipment;

import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class WeaponData extends EquipmentData implements DeepCopyable<EquipmentData>, RarityMeasurable
{
    public WeaponData(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData,
            eWeaponType _weaponType, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill)
    {
    	this(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, _weaponType, _weaponClassifications, _mainWeaponSkill, null);
    	
    	isTransformableWeaponsListModifiable = false;
    }
    public WeaponData(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData,
            eWeaponType _weaponType, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill,
                List<WeaponData> _targetWeaponsInCaseTypeIsTransformable)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData);
    	
        WeaponType = _weaponType;

        m_weaponClassifications = ListExtension.CoalesceNullAndReturnCopyOptionally(_weaponClassifications, eCopyType.Deep);

        MainWeaponSkill = _mainWeaponSkill;

        m_transformableWeapons = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetWeaponsInCaseTypeIsTransformable, eCopyType.Shallow);
    
        isTransformableWeaponsListModifiable = true;
    }
    
    //Public Read-only Fields
    public eWeaponType WeaponType;
    
    public Skill MainWeaponSkill; // Can be null.
    //End Public Read-only Fields

    //Getters
    public eRarity getRarity() { return rarity; }
    
    public List<eWeaponClassification> getWeaponClassifications() { return Collections.unmodifiableList(m_weaponClassifications); }

    public List<WeaponData> getTransformableWeapons() 
    { 
    	if (isTransformableWeaponsListModifiable)
    		return m_transformableWeapons;
    	else
    		return Collections.unmodifiableList(m_transformableWeapons); 
    } //used if WeaponType == eWeaponType.TRANSFORMABLE
    //End Getters
    
    //Private Fields
    private List<eWeaponClassification> m_weaponClassifications;

    private List<WeaponData> m_transformableWeapons; //Store the reference to the original object
    
    private boolean isTransformableWeaponsListModifiable;
    //End Private Fields

    //Public Methods
    public void DisableModification() { isTransformableWeaponsListModifiable = false; }
    
    public WeaponData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected WeaponData DeepCopyInternally()
    {
        WeaponData copy = (WeaponData)super.DeepCopyInternally();

        copy.m_weaponClassifications = ListExtension.DeepCopy(m_weaponClassifications);

        return copy;
    }
    //End Protected Methods
}
