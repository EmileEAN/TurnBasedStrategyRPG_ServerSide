package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;

// Required to obtain some Equipments
public class EquipmentTradingItem extends Item implements DeepCopyable<Item>, RarityMeasurable
{
    public EquipmentTradingItem(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Methods
    public EquipmentTradingItem DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected EquipmentTradingItem DeepCopyInternally() { return (EquipmentTradingItem)super.DeepCopyInternally(); }
    //End Protected Methods
}
