package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class LevelableWeapon extends Weapon implements DeepCopyable<Weapon>
{
    public LevelableWeapon(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill, int _uniqueId, boolean _isLocked,
        int _accumulatedExperience)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, eWeaponType.Levelable, _weaponClassifications, _mainWeaponSkill, _uniqueId, _isLocked);
    	
    	if (_accumulatedExperience > 0)
    		AccumulatedExperience = _accumulatedExperience;
    	else
    		AccumulatedExperience = 0;
    }
    public LevelableWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked, int _accumulatedExperience)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    	
    	if (_accumulatedExperience > 0)
    		AccumulatedExperience = _accumulatedExperience;
    	else
    		AccumulatedExperience = 0;
    }

    //Getters
    public int getAccumulatedExperience() { return AccumulatedExperience; }
    //End Getters
    
    //Private Fields
    private int AccumulatedExperience;
    //End Private Fields

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

    public LevelableWeapon DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected LevelableWeapon DeepCopyInternally() { return (LevelableWeapon)super.DeepCopyInternally(); }
    //End Protected Methods
}
