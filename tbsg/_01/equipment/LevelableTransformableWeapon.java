package eean_games.tbsg._01.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.Linq;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class LevelableTransformableWeapon extends Weapon implements DeepCopyable<Weapon>
{
    public LevelableTransformableWeapon(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill, int _uniqueId, boolean _isLocked,
        int _accumulatedExperience, List<LevelableTransformableWeapon> _targetWeapons)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, eWeaponType.Levelable, _weaponClassifications, _mainWeaponSkill, _uniqueId, _isLocked);
    	
        if (_accumulatedExperience > 0)
            AccumulatedExperience = _accumulatedExperience;
        else
            AccumulatedExperience = 0;
        
        m_transformableWeapons = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetWeapons, eCopyType.Shallow);
    }
    public LevelableTransformableWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked,
    		int _accumulatedExperience)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    	
        if (_accumulatedExperience > 0)
            AccumulatedExperience = _accumulatedExperience;
        else
            AccumulatedExperience = 0;
    	
    	List<LevelableTransformableWeapon> tmp_weaponPool = new ArrayList<LevelableTransformableWeapon>();
    	tmp_weaponPool.add(this);
    	
    	m_transformableWeapons = new ArrayList<LevelableTransformableWeapon>();
    	for (WeaponData weaponData : _weaponData.getTransformableWeapons())
    	{
    		m_transformableWeapons.add(new LevelableTransformableWeapon(weaponData, UniqueId, _isLocked, _accumulatedExperience, tmp_weaponPool));
    	}
    }
    private LevelableTransformableWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked,
    		int _accumulatedExperience, List<LevelableTransformableWeapon> _weaponsGenerated)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    	
        if (_accumulatedExperience > 0)
            AccumulatedExperience = _accumulatedExperience;
        else
            AccumulatedExperience = 0;
    	
    	_weaponsGenerated.add(this);
    	
    	m_transformableWeapons = new ArrayList<LevelableTransformableWeapon>();
    	for (WeaponData weaponData : _weaponData.getTransformableWeapons())
    	{
    		if (!Linq.any(_weaponsGenerated, x -> x.BaseInfo.Id == weaponData.Id))
    			m_transformableWeapons.add(new LevelableTransformableWeapon(weaponData, UniqueId, _isLocked, _accumulatedExperience, _weaponsGenerated));
    		else
    			m_transformableWeapons.add(Linq.first(_weaponsGenerated, x -> x.BaseInfo.Id == weaponData.Id));
    	}
    }

    //Getters
    public int getAccumulatedExperience() { return AccumulatedExperience; }
    
    public List<LevelableTransformableWeapon> getTransformableWeapons() { return Collections.unmodifiableList(m_transformableWeapons); }
    //End Getters
    
    //Private Fields
    private int AccumulatedExperience;
    
    private List<LevelableTransformableWeapon> m_transformableWeapons;
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

    public LevelableTransformableWeapon DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected LevelableTransformableWeapon DeepCopyInternally() { return (LevelableTransformableWeapon)super.DeepCopyInternally(); }
    //End Protected Methods
}