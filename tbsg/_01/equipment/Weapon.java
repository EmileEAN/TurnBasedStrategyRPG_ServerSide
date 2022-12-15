package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public abstract class Weapon implements DeepCopyable<Weapon>, Cloneable
{
    public Weapon(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, eWeaponType _weaponType, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill,
                    int _uniqueId, boolean _isLocked)
    {
        BaseInfo = new WeaponData(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, _weaponType, _weaponClassifications, _mainWeaponSkill);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }
    public Weapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked)
    {
        BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_weaponData, false);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }

    //Public Read-only Fields
    public final WeaponData BaseInfo; //Store reference to original instance

    public final int UniqueId;
    //End Public Read-only Fields

    //Public Fields
    public boolean IsLocked;
    //End Public Fields
    
    //Public Methods
    public Weapon DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected Weapon DeepCopyInternally() 
    { 
    	try {
			return (Weapon)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		} 
    }
    //End Protected Methods
}