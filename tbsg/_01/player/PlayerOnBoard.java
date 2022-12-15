package eean_games.tbsg._01.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eean_games.main.extension_method.MapExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.tbsg._01.Team;
import eean_games.tbsg._01.item.BattleItem;
import eean_games.tbsg._01.item.Item;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitInstance;

public final class PlayerOnBoard
{
    public PlayerOnBoard(int _id, String _name, Team _team, boolean _isPlayer1)
    {
    	Id = _id;
        Name = StringExtension.CoalesceNull(_name);

        IsPlayer1 = _isPlayer1;

        Moved = false;
        Attacked = false;

        UsedUltimateSkill = false;
        
        //Assign Allied Units
        AlliedUnits = new ArrayList<UnitInstance>();
        //Assign Items
        Items = new ArrayList<BattleItem>();

        if (_team != null)
        {
        	boolean isTeamEmpty = true;
        	for (Unit member : _team.members)
        	{
        		if (member != null)
        		{
        			isTeamEmpty = false;
        			break;
        		}
        	}
        	
            if (!isTeamEmpty)
            {
                for (Unit member : _team.members)
                {
                	if (member != null)
                		AlliedUnits.add(new UnitInstance(member, this));
                }
            }
            else
                System.out.println("No units in team!");

            if (_team.ItemSet != null)
            {
                for (Map.Entry<BattleItem, Integer> itemQuantity : _team.ItemSet.quantityPerItem.entrySet())
                {
                	for (int i = 0; i < itemQuantity.getValue(); i++)
                	{
                        Items.add(itemQuantity.getKey().DeepCopy());
                	}
                }
            }
        }
        else
            System.out.println("Null Team object!");

        //ID of Unit Currently Selected
        SelectedUnitIndex = -1; //Meaning none of the characters is selected

        MaxSP = 0;
        RemainingSP = 0;
    }

    public PlayerOnBoard(Player _player, int _teamIndex, boolean _isPlayer1)
    {  	
        IsPlayer1 = _isPlayer1;

        Moved = false;
        Attacked = false;

        UsedUltimateSkill = false;

        //Assign Allied Units
        AlliedUnits = new ArrayList<UnitInstance>();
        //Assign Items
        Items = new ArrayList<BattleItem>();

        if (_player != null)
        {
        	Id = _player.Id;
            Name = _player.Name;

            List<Team> tmp_teams = Collections.unmodifiableList(_player.Teams);
            
            if (tmp_teams.size() > _teamIndex && _teamIndex >= 0)
            {   
            	boolean isTeamEmpty = true;
            	for (Unit member : tmp_teams.get(_teamIndex).members)
            	{
            		if (member != null)
            		{
            			isTeamEmpty = false;
            			break;
            		}
            	}
            	
                if (isTeamEmpty)
                    System.out.println("No units in team!");
                else
                {
                    for (Unit member : tmp_teams.get(_teamIndex).members)
                    {
                    	if (member != null)
                    		AlliedUnits.add(new UnitInstance(member, this));
                    }
                }

                if (tmp_teams.get(_teamIndex).ItemSet != null)
                {
                    for (Map.Entry<BattleItem, Integer> itemQuantity : tmp_teams.get(_teamIndex).ItemSet.quantityPerItem.entrySet())
                    {
                    	for (Map.Entry<Item, Integer> itemOwned : _player.ItemsOwned.entrySet())
                    	{
                    		if (itemQuantity.getKey().Id == itemOwned.getKey().Id)
                    		{
                    			int quantity = (itemOwned.getValue() >= itemQuantity.getValue()) ? itemQuantity.getValue() : itemOwned.getValue();
                    			for (int i = 0; i < quantity; i++)
                    			{
                    				Items.add((BattleItem)(itemOwned.getKey().DeepCopy()));
                    			}
                    		}
                    	}
                    }
                }
            }
            else if (tmp_teams.size() > 0)
            {
            	boolean isTeamEmpty = true;
            	for (Unit member : tmp_teams.get(0).members)
            	{
            		if (member != null)
            		{
            			isTeamEmpty = false;
            			break;
            		}
            	}
            	
                if (isTeamEmpty)
                    System.out.println("No units in team!");
                else
                {
                    for (Unit member : tmp_teams.get(0).members)
                    {
                    	if (member != null)
                    		AlliedUnits.add(new UnitInstance(member, this));
                    }
                }

                Map<Item, Integer> tmp_itemsOwned = MapExtension.DeepCopy(_player.ItemsOwned);
                for (Map.Entry<BattleItem, Integer> itemQuantity : tmp_teams.get(0).ItemSet.quantityPerItem.entrySet())
                {
                	for (Map.Entry<Item, Integer> itemOwned : tmp_itemsOwned.entrySet())
                	{
                		if (itemQuantity.getKey().Id == itemOwned.getKey().Id)
                		{
                			int quantity = (itemOwned.getValue() >= itemQuantity.getValue()) ? itemQuantity.getValue() : itemOwned.getValue();
                			for (int i = 0; i < quantity; i++)
                			{
                				Items.add((BattleItem)(itemOwned.getKey().DeepCopy()));
                			}
                		}
                	}
                }
            }
            else
                System.out.println("No teams found!");
        }
        else
        {
        	Id = 0;
        	Name = "";
        }

        //ID of Unit Currently Selected
        SelectedUnitIndex = -1; //Meaning none of the characters is selected

        MaxSP = 0;
        RemainingSP = 0;
    }

    //Public Fields
    public boolean IsPlayer1;

    public boolean Moved;
    public boolean Attacked;
    
    public int SelectedUnitIndex; //Id of the unit in AlliedUnits 

    public int MaxSP;
    public int RemainingSP;
    //End Public Fields
    
    //Public Read-only Fields
    public final int Id;
    public final String Name;
    
    public final List<UnitInstance> AlliedUnits; //List elements are modifiable

    public final List<BattleItem> Items; //List elements are modifiable
    //End Public Read-only Fields
    
    //Getters
    public boolean getUsedUltimateSkill() { return UsedUltimateSkill; }
    //End Getters
    
    //Private Fields
    private boolean UsedUltimateSkill;
    //End Private Fields

    //Public Methods
    public boolean HasRequiredItems(Map<Integer, Integer> _itemCosts)
    {
        List<Integer> availableItemIds = new ArrayList<Integer>();

        for (Item item : Collections.unmodifiableList(Items))
        {
            availableItemIds.add(item.Id);
        }

        for (Map.Entry<Integer, Integer> itemCost : _itemCosts.entrySet())
        {
            for (int i = 0; i < itemCost.getValue(); i++)
            {
                if (availableItemIds.contains(itemCost.getKey()))
                	availableItemIds.remove(itemCost.getKey());
                else
                    return false;
            }
        }

        return true;
    }

    public void SetUsedUltimateSkillToTrue() { UsedUltimateSkill = true; }
    //End Public Methods
}
