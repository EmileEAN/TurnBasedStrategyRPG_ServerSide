package eean_games.tbsg._01.player;

import java.util.List;
import java.util.Map;

import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.MapExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.ItemSet;
import eean_games.tbsg._01.Team;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.item.Item;
import eean_games.tbsg._01.unit.Unit;

public class Player
{
    public Player(int _id, String _name, List<Unit> _unitsOwned, List<Weapon> _weaponsOwned, List<Armour> _armoursOwned, List<Accessory> _accessoriesOwned, Map<Item, Integer> _itemsOwned, List<ItemSet> _itemSets, List<Team> _teams, int _gemsOwned, int _goldOwned)
    {
        //Assign Id
        Id = _id;

        //Assign Name
        Name = StringExtension.CoalesceNull(_name);

        //Assign Playable Units Owned
        UnitsOwned = ListExtension.CoalesceNullAndReturnCopyOptionally(_unitsOwned, eCopyType.Shallow);

        //Assign Weapons Owned
        WeaponsOwned = ListExtension.CoalesceNullAndReturnCopyOptionally(_weaponsOwned, eCopyType.Shallow);

        //Assign Armours Owned
        ArmoursOwned = ListExtension.CoalesceNullAndReturnCopyOptionally(_armoursOwned, eCopyType.Shallow);

        //Assign Accessories Owned
        AccessoriesOwned = ListExtension.CoalesceNullAndReturnCopyOptionally(_accessoriesOwned, eCopyType.Shallow);
        
        //Assign Items Owned
        ItemsOwned = MapExtension.CoalesceNullAndReturnCopyOptionally(_itemsOwned, eCopyType.Shallow);

        //Assign ItemSets Owned
        ItemSets = ListExtension.CoalesceNullAndReturnCopyOptionally(_itemSets, eCopyType.Shallow);
        
        //Assign Teams Owned
        Teams = ListExtension.CoalesceNullAndReturnCopyOptionally(_teams, eCopyType.Shallow);

        GemsOwned = _gemsOwned;
        GoldOwned = _goldOwned;
     }

    //Public Fields  
    public List<Unit> UnitsOwned; //Will Store References

    public List<Weapon> WeaponsOwned; //Will Store References
    public List<Armour> ArmoursOwned; //Will Store References
    public List<Accessory> AccessoriesOwned; //Will Store References

    public Map<Item, Integer> ItemsOwned; //Will Store References

    public List<ItemSet> ItemSets; //Will Store References
    public List<Team> Teams; //Will Store References

    public int GemsOwned;
    public int GoldOwned;
    //End Public Fields
    
    //Public Read-only Fields
    public final int Id;
    public final String Name;
    //End Public Read-only Fields
}
