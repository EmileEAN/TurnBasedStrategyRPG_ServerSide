package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;

// Required to enhance some objects
public class EnhancementMaterial extends Item implements DeepCopyable<Item>, RarityMeasurable
{
    public EnhancementMaterial(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice,
    		int _enhancementValue)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice);
    	enhancementValue = _enhancementValue;
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Read-only Fields
    public final int enhancementValue;
    //End Public Read-only Fields
    
    //Public Methods
    public EnhancementMaterial DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected EnhancementMaterial DeepCopyInternally() { return (EnhancementMaterial)super.DeepCopyInternally(); }
    //End Protected Methods
}
