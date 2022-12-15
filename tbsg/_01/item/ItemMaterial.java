package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;

// Required to craft Items
public class ItemMaterial extends Item implements DeepCopyable<Item>, RarityMeasurable
{
    public ItemMaterial(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Methods
    public ItemMaterial DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected ItemMaterial DeepCopyInternally() { return (ItemMaterial)super.DeepCopyInternally(); }
    //End Protected Methods
}
