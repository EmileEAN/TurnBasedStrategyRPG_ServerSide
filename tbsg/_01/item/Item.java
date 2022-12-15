package eean_games.tbsg._01.item;

import java.util.Arrays;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;

public abstract class Item implements DeepCopyable<Item>, Cloneable, RarityMeasurable
{
    public Item(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice)
    {
        Id = _id;

        Name = StringExtension.CoalesceNull(_name);

        m_iconAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_iconAsBytes, true);

        rarity = _rarity;

        SellingPrice = _sellingPrice;
    }

    //Public Read-only Fields
    public final int Id;
    public final String Name;

    public final int SellingPrice;
    //End Public Read-only Fields
    
    //Getters
    public byte[] getIconAsBytes() { return m_iconAsBytes; }
    //End Getters
    
    //Protected Read-only Fields
    protected final eRarity rarity;
    //End Protected Read-only Fields
    
    //Private Fields
    private byte[] m_iconAsBytes;
    //End Private Fields
    
    //Public Methods
    public Item DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected Item DeepCopyInternally()
    {
		try {
			Item copy = (Item)super.clone();
			
	        copy.m_iconAsBytes = Arrays.copyOf(m_iconAsBytes, m_iconAsBytes.length);

	        return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}
