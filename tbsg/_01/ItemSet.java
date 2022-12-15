package eean_games.tbsg._01;

import java.util.Map;

import eean_games.main.extension_method.MapExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.item.BattleItem;

public class ItemSet 
{
	public ItemSet(int _id, Map<BattleItem, Integer> _quantityPerItem)
	{
		Id = _id;
		quantityPerItem = MapExtension.CoalesceNullAndReturnCopyOptionally(_quantityPerItem, eCopyType.Shallow);
	}
	
	//Public Read-only Fields
    public final int Id;
    //End Public Read-only Fields
    
    //Public Fields
    public Map<BattleItem, Integer> quantityPerItem;
    //End Public Fields
}
