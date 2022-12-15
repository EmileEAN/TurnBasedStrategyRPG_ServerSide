package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eRarity;

//Used during battle
public class BattleItem extends Item implements DeepCopyable<Item>
{
    public BattleItem(int _id, String _name, byte[] _iconAsByteArray, eRarity _rarity, int _sellingPrice)
    {
    	super(_id, _name, _iconAsByteArray, _rarity, _sellingPrice);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters

    //Public Methods
    public BattleItem DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected BattleItem DeepCopyInternally() { return (BattleItem)super.DeepCopyInternally(); }
    // EndProtected Methods
}
