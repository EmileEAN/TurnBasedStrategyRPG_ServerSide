package eean_games.tbsg._01.gacha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import eean_games.main.Linq;
import eean_games.main.MTRandom;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.Calculator;
import eean_games.tbsg._01.SharedGameDataContainer;
import eean_games.tbsg._01.enumerable.eCostType;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.AccessoryData;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.ArmourData;
import eean_games.tbsg._01.equipment.LevelableTransformableWeapon;
import eean_games.tbsg._01.equipment.LevelableWeapon;
import eean_games.tbsg._01.equipment.OrdinaryWeapon;
import eean_games.tbsg._01.equipment.TransformableWeapon;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.equipment.WeaponData;
import eean_games.tbsg._01.item.GachaCostItem;
import eean_games.tbsg._01.item.Item;
import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitData;

public class Gacha
{
	public Gacha(int _id, String _title, eGachaClassification _gachaClassification, List<GachaObjectInfo> _gachaObjectInfos, ValuePerRarity _defaultDispensationValues, AlternativeDispensationInfo _alternativeDispensationInfo, List<DispensationOption> _dispensationOptions, byte[] _bannerImageAsBytes, byte[] _gachaSceneBackgroundImageAsBytes)
	{
		this(_id, _title, _gachaClassification, _gachaObjectInfos, _defaultDispensationValues, _alternativeDispensationInfo, _dispensationOptions, _bannerImageAsBytes, _gachaSceneBackgroundImageAsBytes, MIN_LEVEL_OF_OBJECTS);
	}
    public Gacha(int _id, String _title, eGachaClassification _gachaClassification, List<GachaObjectInfo> _gachaObjectInfos, ValuePerRarity _defaultDispensationValues, AlternativeDispensationInfo _alternativeDispensationInfo, List<DispensationOption> _dispensationOptions, byte[] _bannerImageAsBytes, byte[] _gachaSceneBackgroundImageAsBytes, int _levelOfObjects)
    {
        Id = _id;
        Title = StringExtension.CoalesceNull(_title);
        
        gachaClassification = _gachaClassification;

        m_gachaObjectInfos = ListExtension.CoalesceNullAndReturnCopyOptionally(_gachaObjectInfos, eCopyType.Shallow);

        DefaultDispensationValues = _defaultDispensationValues;

        AlternativeDispensationInfo = _alternativeDispensationInfo;
        
        m_dispensationOptions = ListExtension.CoalesceNullAndReturnCopyOptionally(_dispensationOptions, eCopyType.Shallow);

        m_bannerImageAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_bannerImageAsBytes, true);
        m_gachaSceneBackgroundImageAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_gachaSceneBackgroundImageAsBytes, true);
	
        if (_levelOfObjects < MIN_LEVEL_OF_OBJECTS)
        	levelOfObjects = MIN_LEVEL_OF_OBJECTS;
        else if (_levelOfObjects > MAX_LEVEL_OF_OBJECTS)
        	levelOfObjects = MAX_LEVEL_OF_OBJECTS;
        else
        	levelOfObjects = _levelOfObjects;
    }
    
	//Public Read-only Fields
    public final int Id;
    public final String Title;

    public final eGachaClassification gachaClassification;

    public final ValuePerRarity DefaultDispensationValues;
    public final AlternativeDispensationInfo AlternativeDispensationInfo;
    
    public final int levelOfObjects; 
    //End Public Read-only Fields

    //Getters
    public List<GachaObjectInfo> getGachaObjectInfos() { return Collections.unmodifiableList(m_gachaObjectInfos); }
    public List<DispensationOption> getDispensationOptions() { return Collections.unmodifiableList(m_dispensationOptions); }
    
    public byte[] getBannerImageAsBytes() { return m_bannerImageAsBytes; }
    public byte[] getGachaSceneBackgroundImageAsBytes() { return m_gachaSceneBackgroundImageAsBytes; }
    //End Getters
    
    //Private Read-only Fields
    private final List<GachaObjectInfo> m_gachaObjectInfos;
    private final List<DispensationOption> m_dispensationOptions;

    private final byte[] m_bannerImageAsBytes;
    private final byte[] m_gachaSceneBackgroundImageAsBytes;
    //End Private Read-only Fields
    
    //Private Constant Fields
    private final static int MIN_LEVEL_OF_OBJECTS = 1;
    private final static int MAX_LEVEL_OF_OBJECTS = 100;
    //End Private Constant Fields
    
    //Public Methods    
    @SuppressWarnings("static-access")
	public List<Object> dispenseObjects(Connection _con, int _playerId, int _dispensationOptionId)
    {
    	Player player = Linq.firstOrDefault(SharedGameDataContainer.getPlayers(), x -> x.Id == _playerId);
    	if (player == null)
    		return null;
    	
    	DispensationOption dispensationOption = Linq.first(m_dispensationOptions, x -> x.id == _dispensationOptionId);
    	Item costItem = null;
    	
    	int playerPossession = 0;
    	switch (dispensationOption.costType)
    	{
    		default: //case Gem
    			playerPossession = player.GemsOwned;
    			break;
    		case Gold:
    			playerPossession = player.GoldOwned;
    			break;
    		case Item:
    			{
    				costItem = Linq.first(SharedGameDataContainer.getItems(), x -> x.Id == dispensationOption.costItemId);
    				if (!(costItem instanceof GachaCostItem)) //If the item is not a GachaCostItem, it must not be used.
    					return null;
    				
    				playerPossession = player.ItemsOwned.get(costItem);
    			}
    			break;
    	}
    	if (playerPossession < dispensationOption.costValue) //Not enough gem, gold, or item
    		return null;
    	
        List<Object> result = new ArrayList<Object>();

        for (int i = 1; i <= dispensationOption.timesToDispense; i++)
        {
        	boolean areAltValuesNull = AlternativeDispensationInfo == null;
        	
            ValuePerRarity dispensationValues = (!areAltValuesNull 
            									&& AlternativeDispensationInfo.applyAtXthDispensation == i)
            										? AlternativeDispensationInfo.ratioPerRarity 
            										: DefaultDispensationValues;

            int referenceNumber = dispensationValues.getTotalValue(); //Initialize referenceNumber for rarity selection.

            MTRandom.randInit();
            int occurencePosition = MTRandom.getRand(1, referenceNumber); //Get the number used to select the rarity of the Object.

            eRarity targetRarity = dispensationValues.occurrenceValueToRarity(occurencePosition);

            referenceNumber = 0; //Re-set referenceNumber for unit selection.
            List<GachaObjectInfo> gachaObjectInfosOfGivenRarity = Linq.where(m_gachaObjectInfos, x -> x.object.getRarity() == targetRarity);
            for (GachaObjectInfo goInfo : gachaObjectInfosOfGivenRarity)
            {
                referenceNumber += goInfo.relativeOccurenceValue;
            }
            occurencePosition = MTRandom.getRand(1, referenceNumber); //Get the number used to select the Object.

            Object obj = new Object();
            int accumulatedRelativeOccurenceValue = 0;
            for (GachaObjectInfo goInfo : gachaObjectInfosOfGivenRarity)
            {
                accumulatedRelativeOccurenceValue += goInfo.relativeOccurenceValue;

                if (occurencePosition <= accumulatedRelativeOccurenceValue) //This goInfo is the one to be used
                {
                    obj = generateObject(_con, _playerId, goInfo.object);

                    if (obj == null) //If at least one object is null, the whole dispensation process is a failure
                    {
                    	eliminateGeneratedObjects(_con, _playerId, result);
                    	return null;
                    }

                    result.add(obj); //Register the object to dispense

                    break;
                }
            }
        }
        
        if (!updatePlayerAttempts(_con, player.Id, dispensationOption.id)
        	|| !collectPayment(_con, player, playerPossession, dispensationOption.costType, dispensationOption.costValue, (GachaCostItem)costItem))
        {
        	eliminateGeneratedObjects(_con, _playerId, result);
        	return null;
        }
        
        return result;
    }
    
    @SuppressWarnings("resource")
	public int remainingAttempts(Connection _con, int _playerId, DispensationOption _dispensationOption)
    {
    	if (_dispensationOption == null)
    		return 0;
    	
		String query;
		PreparedStatement statement;
		
		ResultSet resultSet;
    	
		try
		{
			int attemptsAllowed = _dispensationOption.attemptsAllowedPerPlayer;
			if (attemptsAllowed != -1) //If the number of attempts is not -1 (which means infinite)
			{
				query = "select Id from Gacha_DispensationOption where GachaId=? and DispensationOptionId=?";
				statement = _con.prepareStatement(query);
				statement.setInt(1, Id);
				statement.setInt(2, _dispensationOption.id);
				
				resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					int gachaDispensationOptionPairId = resultSet.getInt("Id");
					
					if (_dispensationOption.isNumberOfAttemptsPerDay) //If the number of attempts will reset each day
					{											
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date();
						String formattedDate = dateFormat.format(date);
						
						//Get the number of attempts for the day
						query = "select count(*) "
								+ "from Player_GachaDispensationOptionAttempt "
								+ "where PlayerId=? and GachaDispensationOptionPairId=? and DateTime>=? and DateTime<?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, _playerId);
						statement.setInt(2, gachaDispensationOptionPairId);
						statement.setString(3, formattedDate);
						statement.setString(4, formattedDate);
					}
					else //If the number of attempts will never reset
					{
						//Get the total amount of attempts
						query = "select count(*) "
								+ "from Player_GachaDispensationOptionAttempt "
								+ "where PlayerId=? and GachaDispensationOptionPairId=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, _playerId);
						statement.setInt(2, gachaDispensationOptionPairId);
					}
					
					ResultSet resultSet2 = statement.executeQuery();
					if (resultSet2.next())
					{
						int numberOfAttempts = resultSet2.getInt(1);
						
						if (numberOfAttempts <= attemptsAllowed)
							return attemptsAllowed - numberOfAttempts;
						else
							return 0;
					}
				}
			}
			else
				return -1; //Meaning infinite
		
			return 0;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 0;
		}
    }
    //End Public Methods

    //Private Methods    
    @SuppressWarnings("resource")
	private Object generateObject(Connection _con, int _playerId, Object _objectBase)
    {
		String query;
		PreparedStatement statement;
		
		int recordsAffected;
		ResultSet resultSet;
    	
        Object result = null;

        try
        {
        	int accumulatedLevel = 0;
        	int accumulatedExperience = 0;
        	
        	switch (gachaClassification)
            {
                default: //case Unit
                	{
                		UnitData unitData = (UnitData)_objectBase;
    					
    					accumulatedLevel = Calculator.AccumulatedLevel(unitData.getRarity(), levelOfObjects);
    					accumulatedExperience = Calculator.MinimumAccumulatedExperienceRequired(accumulatedLevel);
    					
    					query = "insert into PlayableUnits (BaseUnitId, OwnerPlayerId, AccumulatedExperience) values (?, ?, ?)";
    					statement = _con.prepareStatement(query);
    					statement.setInt(1, unitData.Id);
    					statement.setInt(2, _playerId);
    					statement.setInt(3, accumulatedExperience);

    					recordsAffected = statement.executeUpdate(); 
    					if (recordsAffected != 0) //If new item was added successfully...
    					{
    						query = "select Id from PlayableUnits order by Id desc limit 1"; //Get Id of the last entry in the table
    						statement = _con.prepareStatement(query);
    						
    						resultSet = statement.executeQuery();
    						if (resultSet.next())
    						{
    							int uniqueId = resultSet.getInt("Id");
    							result = new Unit(unitData, uniqueId, "", accumulatedExperience, false);
    						}
    					}
                	}
                    break;
                    
                case Weapon:
	            	{
	            		WeaponData weaponData = (WeaponData)_objectBase;
											
						query = "insert into UsableWeapons (BaseWeaponId, OwnerPlayerId) values (?, ?)";
						statement = _con.prepareStatement(query);
						statement.setInt(1, weaponData.Id);
						statement.setInt(2, _playerId);
						
						recordsAffected = statement.executeUpdate(); 
						if (recordsAffected != 0) //Check if new item was added successfully...
						{
							query = "select Id from UsableWeapons order by Id desc limit 1"; //Get Id of the last entry in the table
							statement = _con.prepareStatement(query);
							
							resultSet = statement.executeQuery();
							if (resultSet.next())
							{
								int uniqueId = resultSet.getInt("Id");
								
								switch (weaponData.WeaponType)
								{
									default: //case Ordinary
										result = new OrdinaryWeapon(weaponData, uniqueId, false);
										break;
										
									case Transformable:
										result = new TransformableWeapon(weaponData, uniqueId, false);
										break;
										
									case Levelable:
									case LevelableTransformable:
										{
											accumulatedLevel = Calculator.AccumulatedLevel(weaponData.getRarity(), levelOfObjects);
					    					accumulatedExperience = Calculator.RequiredExperienceForLevelUp(accumulatedLevel);	
					    					
											query = "insert into UsableLevelableWeapons (BaseUsableWeaponId, AccumulatedExperience) values (?, ?)";
											statement = _con.prepareStatement(query);
											statement.setInt(1, uniqueId);
											statement.setInt(2, accumulatedExperience);
					            		
											recordsAffected = statement.executeUpdate(); 
											if (recordsAffected != 0) //If new item was added successfully...
											{
												if (weaponData.WeaponType == eWeaponType.Levelable)
													result = new LevelableWeapon(weaponData, uniqueId, false, accumulatedExperience);
												else // if (weaponData.WeaponType == eWeaponType.LevelableTransformable)
													result = new LevelableTransformableWeapon(weaponData, uniqueId, false, accumulatedExperience);
											}
											else //if experience was not applied to the newly registered weapon, remove the weapon from the database
											{
												query = "delete from UsableWeapons where Id=?";
												statement = _con.prepareStatement(query);
												statement.setInt(1, uniqueId);
												
												statement.executeUpdate();
											}
										}
										break;
								}
							}
						}
	            	}
	                break;
                
                case Armour:
	            	{
	            		ArmourData armourData = (ArmourData)_objectBase;
											
						query = "insert into UsableArmours (BaseArmourId, OwnerPlayerId) values (?, ?)";
						statement = _con.prepareStatement(query);
						statement.setInt(1, armourData.Id);
						statement.setInt(2, _playerId);
						
						recordsAffected = statement.executeUpdate(); 
						if (recordsAffected != 0) //If new item was added successfully...
						{
							query = "select Id from UsableArmours order by Id desc limit 1"; //Get Id of the last entry in the table
							statement = _con.prepareStatement(query);
							
							resultSet = statement.executeQuery();
							if (resultSet.next())
							{
								int uniqueId = resultSet.getInt("Id");
								result = new Armour(armourData, uniqueId, false);
							}
						}
	            	}
	                break;
                
                case Accessory:
	            	{
	            		AccessoryData accessoryData = (AccessoryData)_objectBase;
											
						query = "insert into UsableAccessories (BaseAccessoryId, OwnerPlayerId) values (?, ?)";
						statement = _con.prepareStatement(query);
						statement.setInt(1, accessoryData.Id);
						statement.setInt(2, _playerId);
						
						recordsAffected = statement.executeUpdate(); 
						if (recordsAffected != 0) //If new item was added successfully...
						{
							query = "select Id from UsableAccessories order by Id desc limit 1"; //Get Id of the last entry in the table
							statement = _con.prepareStatement(query);
							
							resultSet = statement.executeQuery();
							if (resultSet.next())
							{
								int uniqueId = resultSet.getInt("Id");
								result = new Accessory(accessoryData, uniqueId, false);
							}
						}
	            	}
	                break;
                
                case SkillItem:
                case SkillMaterial:
                case ItemMaterial:
                case EquipmentMaterial:
                case EvolutionMaterial:
	    		case WeaponEnhMaterial:
	    		case UnitEnhMaterial:
	    		case SkillEnhMaterial:
	                {
	                	Item item = (Item)_objectBase;
	                	
	                	boolean succeededToAddItem = false;
	                	
						query = "select * from Player_ItemOwned where PlayerId=? AND ItemId=?"; //Find whether player has ever obtained the item
						statement = _con.prepareStatement(query);
						statement.setInt(1, _playerId);
						statement.setInt(2, item.Id);
						
						resultSet = statement.executeQuery();
						if (!resultSet.next()) //Player has never got the item. Hence, insert new row.
						{					
							query = "insert into Player_ItemOwned (PlayerId, ItemId, Quantity) values (?, ?, ?)";
							statement = _con.prepareStatement(query);
							statement.setInt(1, _playerId);
							statement.setInt(2, item.Id);
							statement.setInt(3, 1); //Add one item
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected != 0) //If new item was added successfully...
								succeededToAddItem = true;
						}
						else //Player has got the item before. Hence, update to add 1 to the quantity of the item.
						{
							query = "update Player_ItemOwned set Quantity=Quantity+1 where PlayerId=? AND ItemId=?";
							statement = _con.prepareStatement(query);
							statement.setInt(1, _playerId);
							statement.setInt(2, item.Id);
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected != 0) //If item was updated successfully...
								succeededToAddItem = true;
						}
						
						if (succeededToAddItem)
							result = Linq.first(SharedGameDataContainer.getItems(), x -> x.Id == item.Id);
	                }
	                break;
            }

        	statement.close();
        	
            return result;
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        	return null;
        }
    }
    
    private void eliminateGeneratedObjects(Connection _con, int _playerId, List<Object> _generatedObjects)
    {
    	for (Object generatedObject : _generatedObjects)
    	{
    		eliminateObject(_con, _playerId, generatedObject);
    	}
    }
    private void eliminateObject(Connection _con, int _playerId, Object _object)
    {
    	String query;
		PreparedStatement statement;
		
        try
        {
        	switch (gachaClassification)
            {
                default: //case Unit
                	{
                		Unit unit = (Unit)_object;
    					
    					query = "delete from PlayableUnits where Id=?";
    					statement = _con.prepareStatement(query);
    					statement.setInt(1, unit.UniqueId);
    					statement.executeUpdate(); 
                	}
                    break;
                    
                case Weapon:
            		{
	            		Weapon weapon = (Weapon)_object;
											
						query = "delete from UsableWeapons where Id=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, weapon.UniqueId);
						statement.executeUpdate();
            		}
            		break;
                
                case Armour:
            		{
	            		Armour armour = (Armour)_object;
						
						query = "delete from UsableArmours where Id=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, armour.UniqueId);
						statement.executeUpdate();
            		}
            		break;
                
                case Accessory:
	            	{
	            		Accessory accessory = (Accessory)_object;
						
						query = "delete from UsableAccessories where Id=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, accessory.UniqueId);
						statement.executeUpdate();
	            	}
	                break;
                
                case SkillItem:
                case SkillMaterial:
                case ItemMaterial:
                case EquipmentMaterial:
                case EvolutionMaterial:
	    		case WeaponEnhMaterial:
	    		case UnitEnhMaterial:
	    		case SkillEnhMaterial:
	                {
	                	Item item = (Item)_object;
	                	
						query = "update Player_ItemOwned set Quantity=Quantity-1 where PlayerId=? AND ItemId=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, _playerId);
						statement.setInt(2, item.Id);
						statement.executeUpdate();
	                }
	                break;
            }

        	statement.close();
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
    }
    
    private boolean updatePlayerAttempts(Connection _con, int _playerId, int _dispensationOptionId)
    {
    	String query;
		PreparedStatement statement;

		ResultSet resultSet;
		int recordsAffected;
		
		try
		{
			query = "select Id from Gacha_DispensationOption where GachaId=? and DispensationOptionId=?";
			statement = _con.prepareStatement(query);
			statement.setInt(1, Id);
			statement.setInt(2, _dispensationOptionId);
			
			resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				int gachaDispensationOptionPairId = resultSet.getInt("Id");
				
				query = "insert into Player_GachaDispensationOptionAttempt (PlayerId, GachaDispensationOptionPairId) values (?, ?)";
				statement = _con.prepareStatement(query);
				statement.setInt(1, _playerId);
				statement.setInt(2, gachaDispensationOptionPairId);
				
				recordsAffected = statement.executeUpdate();
				if (recordsAffected != 0)
				{
					//Also update the records in this server
					GachaDispensationAvailabilityInfo gachaDispensationAvailabilityInfo 
						= Linq.first(SharedGameDataContainer.getGachaDispensatioAvailabilityInfos(),
										x -> x.playerId == _playerId && x.gachaId == Id && x.dispensationOptionId == _dispensationOptionId);
					
					if (gachaDispensationAvailabilityInfo.remainingAttempts != -1) //If the number of attempts allowed is not infinite
						gachaDispensationAvailabilityInfo.remainingAttempts--;
					
					return true;
				}
			}
			
			return false;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
    }
    
    private boolean collectPayment(Connection _con, Player _player, int _playerPossession, eCostType _costType, int _costValue, GachaCostItem _costItem)
    {
    	String query;
		PreparedStatement statement;
		
		int recordsAffected;

		try
		{
			if (_costType == eCostType.Gem || _costType == eCostType.Gold)
			{
				String costTypeString = (_costType == eCostType.Gem) ? "Gems" : "Gold";
				
				query = "update Players"
						+ " set " + costTypeString + "Owned=" + costTypeString + "Owned-" + String.valueOf(_costValue) 
						+ " where Id=?";
			}
			else // _costType == eCostType.Item
			{
				query = "update Player_ItemOwned"
						+ " set Quantity=Quantity-" + String.valueOf(_costValue)
						+ " where PlayerId=? and ItemId=?";
			}
			statement = _con.prepareStatement(query);
			statement.setInt(1, _player.Id);
			if (_costType == eCostType.Item)
				statement.setInt(2, _costItem.Id);
			
			recordsAffected = statement.executeUpdate();
			if (recordsAffected != 0) //If transaction was performed successfully...
			{
				switch (_costType)
				{
					default: //case Gem
						_player.GemsOwned -= _costValue;
						break;
					case Gold:
						_player.GemsOwned -= _costValue;
						break;
					case Item:
						_player.ItemsOwned.replace(_costItem, _playerPossession - _costValue);
				}
				
				return true;
			}
			
			return false;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
    }
    //End Private Methods
}
