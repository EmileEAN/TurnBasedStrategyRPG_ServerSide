package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;

// Required for some Gachas, instead of gems or gold
public class GachaCostItem extends Item implements DeepCopyable<Item>, RarityMeasurable
{
    public GachaCostItem(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Methods
    public GachaCostItem DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected GachaCostItem DeepCopyInternally() { return (GachaCostItem)super.DeepCopyInternally(); }
    //End Protected Methods
}
