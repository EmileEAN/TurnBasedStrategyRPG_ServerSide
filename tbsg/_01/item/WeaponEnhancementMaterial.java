package eean_games.tbsg._01.item;

import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponClassification;

// Used to increase the level of Levelable Weapons
public class WeaponEnhancementMaterial extends EnhancementMaterial implements DeepCopyable<Item>, RarityMeasurable
{
    public WeaponEnhancementMaterial(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _expToApply,
    		List<eWeaponClassification> _targetingWeaponClassifications)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _expToApply);
    	m_targetingWeaponClassifications = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetingWeaponClassifications, eCopyType.Shallow);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    
    public List<eWeaponClassification> getTargetingWeaponClassifications() { return Collections.unmodifiableList(m_targetingWeaponClassifications); }
    //End Getters
    
    //Private Fields
    private List<eWeaponClassification> m_targetingWeaponClassifications;
    //End Private Fields
    
    //Public Methods
    public WeaponEnhancementMaterial DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected WeaponEnhancementMaterial DeepCopyInternally() 
    { 
    	WeaponEnhancementMaterial copy = (WeaponEnhancementMaterial)super.DeepCopyInternally();
    	
    	copy.m_targetingWeaponClassifications = ListExtension.DeepCopy(m_targetingWeaponClassifications);
    	
    	return copy;  
    }
    //End Protected Methods
}
