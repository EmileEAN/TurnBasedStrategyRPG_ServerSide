package eean_games.tbsg._01.item;

import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eRarity;

// Used to increase the level of Units
public class UnitEnhancementMaterial extends EnhancementMaterial implements DeepCopyable<Item>, RarityMeasurable
{
    public UnitEnhancementMaterial(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _expToApply,
    		List<eElement> _bonusElements)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _expToApply);
    	m_bonusElements = ListExtension.CoalesceNullAndReturnCopyOptionally(_bonusElements, eCopyType.Shallow);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    
    public List<eElement> getBonusElements() { return Collections.unmodifiableList(m_bonusElements); }
    //End Getters
    
    //Private Fields
    private List<eElement> m_bonusElements;
    //End Private Fields
    
    //Public Methods
    public UnitEnhancementMaterial DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected UnitEnhancementMaterial DeepCopyInternally() { return (UnitEnhancementMaterial)super.DeepCopyInternally(); }
    //End Protected Methods
}
