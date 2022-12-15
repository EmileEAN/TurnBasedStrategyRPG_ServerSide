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

public class TransformableWeapon extends Weapon implements DeepCopyable<Weapon>
{
    public TransformableWeapon(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, List<eWeaponClassification> _weaponClassifications, Skill _mainWeaponSkill, int _uniqueId, boolean _isLocked, 
        List<TransformableWeapon> _targetWeapons)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, eWeaponType.Transformable, _weaponClassifications, _mainWeaponSkill, _uniqueId, _isLocked);
    	
        m_transformableWeapons = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetWeapons, eCopyType.Deep);
    }

    public TransformableWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    	
    	List<TransformableWeapon> tmp_weaponPool = new ArrayList<TransformableWeapon>();
    	tmp_weaponPool.add(this);
    	
    	m_transformableWeapons = new ArrayList<TransformableWeapon>();
    	for (WeaponData weaponData : _weaponData.getTransformableWeapons())
    	{
    		m_transformableWeapons.add(new TransformableWeapon(weaponData, UniqueId, _isLocked, tmp_weaponPool));
    	}
    }
    
    private TransformableWeapon(WeaponData _weaponData, int _uniqueId, boolean _isLocked, List<TransformableWeapon> _weaponsGenerated)
    {
    	super(_weaponData, _uniqueId, _isLocked);
    	
    	_weaponsGenerated.add(this);
    	
    	m_transformableWeapons = new ArrayList<TransformableWeapon>();
    	for (WeaponData weaponData : _weaponData.getTransformableWeapons())
    	{
    		if (!Linq.any(_weaponsGenerated, x -> x.BaseInfo.Id == weaponData.Id))
    			m_transformableWeapons.add(new TransformableWeapon(weaponData, UniqueId, _isLocked, _weaponsGenerated));
    		else
    			m_transformableWeapons.add(Linq.first(_weaponsGenerated, x -> x.BaseInfo.Id == weaponData.Id));
    	}
    }

    //Getters
    public List<TransformableWeapon> getTransformableWeapons() { return Collections.unmodifiableList(m_transformableWeapons); }
    //End Getters
    
    //Private Fields
    private List<TransformableWeapon> m_transformableWeapons;
    //End Private Fields
    
    //Public Methods
    public TransformableWeapon DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected TransformableWeapon DeepCopyInternally() { return (TransformableWeapon)super.DeepCopyInternally(); }
    //End Protected Methods
}
