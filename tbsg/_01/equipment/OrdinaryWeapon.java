package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class OrdinaryWeapon extends Weapon implements DeepCopyable<Weapon>
{
    public OrdinaryWeapon(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill, int _uniqueId,  boolean _isLocked)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, eWeaponType.Ordinary, _weaponClassifications, _mainWeaponSkill, _uniqueId, _isLocked);
    }
    public OrdinaryWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    }

    //Public Methods
    public OrdinaryWeapon DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected OrdinaryWeapon DeepCopyInternally() { return (OrdinaryWeapon)super.DeepCopyInternally(); }
    //End Protected Methods
}
