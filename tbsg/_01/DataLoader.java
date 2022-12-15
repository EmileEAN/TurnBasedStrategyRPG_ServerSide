package eean_games.tbsg._01;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.skill.CostRequiringSkill;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.UltimateSkill;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitInstance;
import eean_games.main.Linq;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.LevelableWeapon;
import eean_games.tbsg._01.equipment.LevelableTransformableWeapon;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.event_log.ActionLog;
import eean_games.tbsg._01.event_log.ActionLog_Attack;
import eean_games.tbsg._01.event_log.ActionLog_Move;
import eean_games.tbsg._01.event_log.ActionLog_Skill;
import eean_games.tbsg._01.event_log.ActionLog_TileTargetingSkill;
import eean_games.tbsg._01.event_log.ActionLog_UnitTargetingSkill;
import eean_games.tbsg._01.event_log.AutomaticEventLog;
import eean_games.tbsg._01.event_log.EffectTrialLog;
import eean_games.tbsg._01.event_log.EffectTrialLog_DamageEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_HealEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_MovementEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_StatusEffectAttachmentEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_TileTargetingEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_UnitTargetingEffect;
import eean_games.tbsg._01.event_log.EventLog;
import eean_games.tbsg._01.event_log.StatusEffectLog;
import eean_games.tbsg._01.event_log.StatusEffectLog_HPModification;
import eean_games.tbsg._01.event_log.TurnChangeEventLog;
import eean_games.tbsg._01.gacha.DispensationOption;
import eean_games.tbsg._01.gacha.Gacha;
import eean_games.tbsg._01.gacha.eGachaClassification;
import eean_games.tbsg._01.item.BattleItem;
import eean_games.tbsg._01.item.Item;

public class DataLoader 
{	
    public static String getPlayerDataAsResponseString(int _id)
    {
    	String responseString = "<Player>";
    	
    	try 
    	{	
    		Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == _id);
    		
			responseString = responseString + "<Id>" + String.valueOf(_id) + "</Id>";
			responseString = responseString + "<PlayerName>" + player.Name + "</PlayerName>";
			responseString = responseString + "<GemsOwned>" + String.valueOf(player.GemsOwned) + "</GemsOwned>";
			responseString = responseString + "<GoldOwned>" + String.valueOf(player.GoldOwned) + "</GoldOwned>";
			
			responseString = responseString + getUnitsOwnedAsResponseString(player);
			responseString = responseString + getItemsOwnedAsResponseString(player);
			responseString = responseString + getWeaponsOwnedAsResponseString(player);
			responseString = responseString + getArmoursOwnedAsResponseString(player);
			responseString = responseString + getAccessoriesOwnedAsResponseString(player);
			responseString = responseString + getItemSetsAsResponseString(player);
			responseString = responseString + getTeamsAsResponseString(player);
			
			responseString = responseString + "</Player>";
			
			return responseString;
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    private static String getUnitsOwnedAsResponseString(Player _player)
    {
    	String responseString = "<UnitsOwned>";
    	
		try 
		{
			for (Unit unit : _player.UnitsOwned)
			{
				responseString = responseString + "<Unit>";
				
				responseString = responseString + "<UniqueId>" + String.valueOf(unit.UniqueId) + "</UniqueId>";
				responseString = responseString + "<BaseUnitId>" + String.valueOf(unit.BaseInfo.Id) + "</BaseUnitId>";
				responseString = responseString + "<AccumulatedExperience>" + String.valueOf(unit.getAccumulatedExperience()) + "</AccumulatedExperience>";
				responseString = responseString + "<Nickname>" + unit.Nickname + "</Nickname>";
				responseString = responseString + "<IsLocked>" + String.valueOf(unit.IsLocked) + "</IsLocked>";
				
				responseString = responseString + "<Skills>";
				for (Skill skill : unit.getSkills())
				{
					responseString = responseString + "<Skill>";
					
					responseString = responseString + "<SkillId>" + String.valueOf(skill.BaseInfo.Id) + "</SkillId>";
					responseString = responseString + "<SkillLevel>" + skill.Level + "</SkillLevel>";
					
					responseString = responseString + "</Skill>";
				}
				responseString = responseString + "</Skills>";
				
				responseString = responseString + "<MainWeaponId>";
				if (unit.mainWeapon != null)
					responseString = responseString + String.valueOf(unit.mainWeapon.UniqueId);
				else
					responseString = responseString + String.valueOf(0);
				responseString = responseString + "</MainWeaponId>";
				responseString = responseString + "<SubWeaponId>";
				if (unit.subWeapon != null)
					responseString = responseString + String.valueOf(unit.subWeapon.UniqueId);
				else
					responseString = responseString + String.valueOf(0);
				responseString = responseString + "</SubWeaponId>";
				responseString = responseString + "<ArmourId>";
				if (unit.armour != null)
					responseString = responseString + String.valueOf(unit.armour.UniqueId);
				else
					responseString = responseString + String.valueOf(0);
				responseString = responseString + "</ArmourId>";
				responseString = responseString + "<AccessoryId>";
				if (unit.accessory != null)
					responseString = responseString + String.valueOf(unit.accessory.UniqueId);
				else
					responseString = responseString + String.valueOf(0);
				responseString = responseString + "</AccessoryId>";
				
				responseString = responseString + "<SkillInheritorUnitId>";
				if (unit.SkillInheritor != null)
					responseString = responseString + String.valueOf(unit.SkillInheritor.UniqueId);
				else
					responseString = responseString + String.valueOf(0);
				responseString = responseString + "</SkillInheritorUnitId>";
				responseString = responseString + "<InheritingSkillId>" + String.valueOf(unit.InheritingSkillId) + "</InheritingSkillId>";
			
				responseString = responseString + "</Unit>";
			}
			
			responseString = responseString + "</UnitsOwned>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}   
    }
    
    private static String getWeaponsOwnedAsResponseString(Player _player)
    {
    	String responseString = "<WeaponsOwned>";
    	
		try 
		{
			for (Weapon weapon : _player.WeaponsOwned)
			{
				responseString = responseString + "<Weapon>";
				
				responseString = responseString + "<UniqueId>" + String.valueOf(weapon.UniqueId) + "</UniqueId>";
				responseString = responseString + "<BaseWeaponId>" + String.valueOf(weapon.BaseInfo.Id) + "</BaseWeaponId>";
				responseString = responseString + "<IsLocked>" + String.valueOf(weapon.IsLocked) + "</IsLocked>";
				
				if (weapon.BaseInfo.WeaponType == eWeaponType.Levelable)
					responseString = responseString + "<AccumulatedExperience>" + String.valueOf(((LevelableWeapon)weapon).getAccumulatedExperience()) + "</UniqueId>";
				else if (weapon.BaseInfo.WeaponType == eWeaponType.LevelableTransformable)
					responseString = responseString + "<AccumulatedExperience>" + String.valueOf(((LevelableTransformableWeapon)weapon).getAccumulatedExperience()) + "</UniqueId>";
				
				responseString = responseString + "</Weapon>";
			}
			
			responseString = responseString + "</WeaponsOwned>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getArmoursOwnedAsResponseString(Player _player)
    {
    	String responseString = "<ArmoursOwned>";
    	
		try 
		{
			for (Armour armour : _player.ArmoursOwned)
			{
				responseString = responseString + "<Armour>";
				
				responseString = responseString + "<UniqueId>" + String.valueOf(armour.UniqueId) + "</UniqueId>";
				responseString = responseString + "<BaseArmourId>" + String.valueOf(armour.BaseInfo.Id) + "</BaseArmourId>";
				responseString = responseString + "<IsLocked>" + String.valueOf(armour.IsLocked) + "</IsLocked>";
				
				responseString = responseString + "</Armour>";
			}
			
			responseString = responseString + "</ArmoursOwned>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getAccessoriesOwnedAsResponseString(Player _player)
    {
    	String responseString = "<AccessoriesOwned>";
    	
		try 
		{
			for (Accessory accessory : _player.AccessoriesOwned)
			{
				responseString = responseString + "<Accessory>";
				
				responseString = responseString + "<UniqueId>" + String.valueOf(accessory.UniqueId) + "</UniqueId>";
				responseString = responseString + "<BaseAccessoryId>" + String.valueOf(accessory.BaseInfo.Id) + "</BaseAccessoryId>";
				responseString = responseString + "<IsLocked>" + String.valueOf(accessory.IsLocked) + "</IsLocked>";
				
				responseString = responseString + "</Accessory>";
			}
			
			responseString = responseString + "</AccessoriesOwned>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getItemsOwnedAsResponseString(Player _player)
    {
    	String responseString = "<ItemsOwned>";
    	
		try 
		{
			for (Map.Entry<Item, Integer> quantityPerItem : _player.ItemsOwned.entrySet())
			{
				responseString = responseString + "<Item>";
				
				responseString = responseString + "<ItemId>" + String.valueOf(quantityPerItem.getKey().Id) + "</ItemId>";
				responseString = responseString + "<Quantity>" + String.valueOf(quantityPerItem.getValue()) + "</Quantity>";
				
				responseString = responseString + "</Item>";
			}
			
			responseString = responseString + "</ItemsOwned>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getItemSetsAsResponseString(Player _player)
    {
    	String responseString = "<ItemSets>";
    	
		try 
		{
			for (ItemSet itemSet : _player.ItemSets)
			{
				responseString = responseString + "<ItemSet>";
				
				responseString = responseString + "<Id>" + String.valueOf(itemSet.Id) + "</Id>";
				
				responseString = responseString + "<Items>";
				for (Map.Entry<BattleItem, Integer> itemQuantity : itemSet.quantityPerItem.entrySet())
				{
					responseString = responseString + "<Item>";
					
					responseString = responseString + "<ItemId>" + String.valueOf(itemQuantity.getKey().Id) + "</ItemId>";
					responseString = responseString + "<Quantity>" + String.valueOf(itemQuantity.getValue()) + "</Quantity>";
					
					responseString = responseString + "</Item>";
				}
				responseString = responseString + "</Items>";
				
				responseString = responseString + "</ItemSet>";
			}
			
			responseString = responseString + "</ItemSets>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getTeamsAsResponseString(Player _player)
    {
    	String responseString = "<Teams>";
    	
		try 
		{
			for (Team team : _player.Teams)
			{
				responseString = responseString + "<Team>";
				
				responseString = responseString + "<MemberIds>";
				for (Unit member : team.members)
				{
					responseString = responseString + "<MemberId>";
					if (member != null)
						responseString = responseString + String.valueOf(member.UniqueId);
					else
						responseString = responseString + String.valueOf(0);
					responseString = responseString + "</MemberId>";
				}
				responseString = responseString + "</MemberIds>";
				
				if (team.ItemSet != null)
					responseString = responseString + "<ItemSetId>" + String.valueOf(team.ItemSet.Id) + "</ItemSetId>";
				
				responseString = responseString + "</Team>";
			}
			
			responseString = responseString + "</Teams>";
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

	public static String getGachaDispensationOptionsRemainingAttemptsAsResponseStrings(Connection _con, int _playerId)
    {
    	String responseString = "<GachaDispensationOptionsRemainingAttempts>";
    	
    	for (Gacha gacha : SharedGameDataContainer.getGachas())
    	{
    		for (DispensationOption dispensationOption : gacha.getDispensationOptions())
    		{
    			responseString = responseString + "<Entry>";
    			responseString = responseString + "<GachaId>" + String.valueOf(gacha.Id) + "</GachaId>";
    			responseString = responseString + "<DispensationOptionId>" + String.valueOf(dispensationOption.id) + "</DispensationOptionId>";
    			int remainingAttempts = Linq.first(SharedGameDataContainer.getGachaDispensatioAvailabilityInfos(),
    												x -> x.playerId == _playerId && x.gachaId == gacha.Id && x.dispensationOptionId == dispensationOption.id).remainingAttempts;
    			responseString = responseString + "<RemainingAttempts>" + String.valueOf(remainingAttempts) + "</RemainingAttempts>";
    			responseString = responseString + "</Entry>";
    		}
    	}
    	
		responseString = responseString + "</GachaDispensationOptionsRemainingAttempts>";
		
		return responseString;
    }
    
    public static String getMatchDataAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(), 
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Set player data. (The client-side app is using the data downloaded from server-side and, hence, properties other than IsPlayer1 must coincide.)
	    	responseString = responseString + "<Player>";
	    	responseString = responseString + "<IsPlayer1>" + String.valueOf(player.IsPlayer1) + "</IsPlayer1>";
	    	responseString = responseString + "<MaxSP>" + String.valueOf(player.MaxSP) + "</MaxSP>";
	    	responseString = responseString + "<Units>";
	    	for (UnitInstance unit : player.AlliedUnits)
	    	{
		    	responseString = responseString + "<Unit>";
		    	responseString = responseString + "<UniqueId>" + String.valueOf(unit.UniqueId) + "</UniqueId>";
		    	responseString = responseString + "<Index>" + String.valueOf(match.Field.GetUnitIndex(unit)) + "</Index>";
		    	responseString = responseString + "<MaxHP>" + String.valueOf(Calculator.MaxHP(unit)) + "</MaxHP>";
		    	responseString = responseString + "</Unit>";
	    	}
	    	responseString = responseString + "</Units>";
	    	responseString = responseString + "</Player>";
	    	
	    	//Get opponent player data
	    	PlayerOnBoard opponent = Linq.first(players, z -> z.Id != _playerId);
	    	
	    	//Set opponent player data
	    	responseString = responseString + "<Opponent>";
	    	responseString = responseString + "<Name>" + opponent.Name + "</Name>";
	    	responseString = responseString + "<MaxSP>" + String.valueOf(opponent.MaxSP) + "</MaxSP>";
	    	responseString = responseString + "<RemainingSP>" + String.valueOf(opponent.RemainingSP) + "</RemainingSP>";
	    	responseString = responseString + "<Units>";
	    	for (UnitInstance unit : opponent.AlliedUnits)
	    	{
		    	responseString = responseString + "<Unit>";
		    	responseString = responseString + "<Index>" + String.valueOf(match.Field.GetUnitIndex(unit)) + "</Index>";
		    	responseString = responseString + "<Id>" + String.valueOf(unit.BaseInfo.Id) + "</Id>";
		    	responseString = responseString + "<MaxHP>" + Calculator.MaxHP(unit) + "</MaxHP>";
		    	responseString = responseString + "<RemainingHP>" + String.valueOf(unit.RemainingHP) + "</RemainingHP>";
		    	responseString = responseString + "</Unit>";
	    	}
	    	responseString = responseString + "</Units>";
	    	responseString = responseString + "</Opponent>";
	    	
	    	//Get and set tile map and unit positions
	    	responseString = responseString + "<Sockets>";
	    	Socket[][] sockets = match.Field.Board.Sockets;
			for (int x = 0; x < sockets.length; x++)
			{
				for (int y = 0; y < sockets[x].length; y++)
				{
					responseString = responseString + "<Socket>";
			    	responseString = responseString + "<XCoord>" + String.valueOf(x) + "</XCoord>";
			    	responseString = responseString + "<YCoord>" + String.valueOf(y) + "</YCoord>";
			    	responseString = responseString + "<TileType>" + sockets[x][y].TileType.toString() + "</TileType>";
			    	if (sockets[x][y].Unit != null)
				    	responseString = responseString + "<UnitIndex>" + match.Field.GetUnitIndex(sockets[x][y].Unit) + "</UnitIndex>";
					responseString = responseString + "</Socket>";
				}
			}
	    	responseString = responseString + "</Sockets>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

    public static String getMatchPlayerInfoAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Set player info
	    	responseString = responseString + "<Player>";
	    	responseString = responseString + "<IsMyTurn>" + String.valueOf((match.getCurrentTurnPlayer() == player) ? true : false) + "</IsMyTurn>";
	    	responseString = responseString + "<HasMoved>" + String.valueOf(player.Moved) + "</HasMoved>";
	    	responseString = responseString + "<HasAttacked>" + String.valueOf(player.Attacked) + "</HasAttacked>";
	    	responseString = responseString + "<HasUsedUltimateSkill>" + String.valueOf(player.getUsedUltimateSkill()) + "</HasUsedUltimateSkill>";
	    	responseString = responseString + "<MaxSP>" + String.valueOf(player.MaxSP) + "</MaxSP>";
	    	responseString = responseString + "<RemainingSP>" + String.valueOf(player.RemainingSP) + "</RemainingSP>";
	    	responseString = responseString + "<SelectedAlliedUnitId>" + String.valueOf(player.SelectedUnitIndex) + "</SelectedAlliedUnitId>";
	    	responseString = responseString + "</Player>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

    public static String getMatchOpponentInfoAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(), 
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get opponent data
	    	PlayerOnBoard opponent = Linq.first(players, z -> z.Id != _playerId);
	    	
	    	//Set player info
	    	responseString = responseString + "<Opponent>";
	    	responseString = responseString + "<MaxSP>" + String.valueOf(opponent.MaxSP) + "</MaxSP>";
	    	responseString = responseString + "<RemainingSP>" + String.valueOf(opponent.RemainingSP) + "</RemainingSP>";
	    	responseString = responseString + "</Opponent>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

    public static String getMatchEndStatusAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
			boolean isMatchEnd = match.getIsMatchEnd();
			responseString = responseString + "<IsMatchEnd>" + String.valueOf(isMatchEnd) + "</IsMatchEnd>";
			
			if (isMatchEnd)
			{
				boolean isPlayerWinner;
				if (match.getIsPlayer1Winner()) //Player 1 is winner
					isPlayerWinner = match.Field.getPlayers().get(0).Id == _playerId;
				else //Player 2 is winner
					isPlayerWinner = match.Field.getPlayers().get(1).Id == _playerId;
			
				responseString = responseString + "<IsPlayerWinner>" + String.valueOf(isPlayerWinner) + "</IsPlayerWinner>";
			}
			
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    public static String getMissingEventLogsAsResponseString(int _playerId, int _latestLogIndex)
    {    	
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
			//Get the logs that the player needs
			List<EventLog> allEventLogs = match.getEventLogs();
	    	List<EventLog> targetEventLogs = new ArrayList<EventLog>();
	    	for (int i = _latestLogIndex + 1; i < allEventLogs.size(); i++)
	    	{
	    		targetEventLogs.add(allEventLogs.get(i));
	    	}
	    	
	    	//Set event logs
	    	for (EventLog eventLog : targetEventLogs)
	    	{
	    		responseString = responseString + getEventLogAsResponseString(eventLog);
	    	}
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getEventLogAsResponseString(EventLog _eventLog)
    {
    	String responseString = "";
    	
    	if (_eventLog instanceof AutomaticEventLog)
    	{
    		AutomaticEventLog automaticEventLog = (AutomaticEventLog)_eventLog;
			
    		if (automaticEventLog instanceof TurnChangeEventLog)
    			responseString = "<TurnChangeEventLog>";
    		else if (automaticEventLog instanceof EffectTrialLog_DamageEffect)
    			responseString = "<EffectTrialLog_DamageEffect>";
        	else if (automaticEventLog instanceof EffectTrialLog_HealEffect)
        		responseString = "<EffectTrialLog_HealEffect>";
        	else if (automaticEventLog instanceof EffectTrialLog_StatusEffectAttachmentEffect)
        		responseString = "<EffectTrialLog_StatusEffectAttachmentEffect>";
        	else if (automaticEventLog instanceof EffectTrialLog_MovementEffect)
        		responseString = "<EffectTrialLog_MovementEffect>";
    		else if (automaticEventLog instanceof StatusEffectLog_HPModification)
    			responseString = "<StatusEffectLog_HPModification>";
    		
    		responseString = responseString + "<EventTurn>" + automaticEventLog.EventTurn.toString() + "</EventTurn>";
    		
    		if (automaticEventLog instanceof TurnChangeEventLog)
        		responseString = responseString + getTurnChangeEventLogAsResponseString((TurnChangeEventLog)automaticEventLog);
    		else if (automaticEventLog instanceof EffectTrialLog)
    			responseString = responseString + getEffectTrialLogAsResponseString((EffectTrialLog)automaticEventLog);
        	else if (automaticEventLog instanceof StatusEffectLog)
        		responseString = responseString + getStatusEffectLogAsResponseString((StatusEffectLog)automaticEventLog);
    	}
    	else if (_eventLog instanceof ActionLog)
    	{
    		ActionLog actionLog = (ActionLog)_eventLog;
    		
    		if (actionLog instanceof ActionLog_Move)
    			responseString = "<ActionLog_Move>";
    		else if (actionLog instanceof ActionLog_Attack)
    			responseString = "<ActionLog_Attack>";
    		else if (actionLog instanceof ActionLog_UnitTargetingSkill)
    			responseString = "<ActionLog_UnitTargetingSkill>";
    		else if (actionLog instanceof ActionLog_TileTargetingSkill)
    			responseString = "<ActionLog_TileTargetingSkill>";
    	
    		responseString = responseString + "<EventTurn>" + actionLog.EventTurn.toString() + "</EventTurn>";
    		
    		responseString = responseString + getActionLogAsResponseString(actionLog);
    	}	
    	
    	return responseString;
    }
    
    private static String getTurnChangeEventLogAsResponseString(TurnChangeEventLog _turnChangeEventLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<TurnEndingPlayerId>" + String.valueOf(_turnChangeEventLog.TurnEndingPlayerId) + "</TurnEndingPlayerId>";
	    	responseString = responseString + "<TurnInitiatingPlayerId>" + String.valueOf(_turnChangeEventLog.TurnInitiatingPlayerId) + "</TurnInitiatingPlayerId>";

	    	responseString = responseString + "</TurnChangeEventLog>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getEffectTrialLogAsResponseString(EffectTrialLog _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
			responseString = responseString + SharedGameDataContainer.animationInfoToResponseString(_effectTrialLog.AnimationInfo);
			responseString = responseString + "<IsDiffused>" + String.valueOf(_effectTrialLog.IsDiffused) + "</IsDiffused>";
			responseString = responseString + "<DidActivate>" + String.valueOf(_effectTrialLog.DidActivate) + "</DidActivate>";
	    	responseString = responseString + "<DidSucceed>" + String.valueOf(_effectTrialLog.DidSucceed) + "</DidSucceed>";
	    	
    		if (_effectTrialLog instanceof EffectTrialLog_UnitTargetingEffect)
    			responseString = responseString + getUnitTargetingEffectTrialLogAsResponseString((EffectTrialLog_UnitTargetingEffect)_effectTrialLog);
    		else if (_effectTrialLog instanceof EffectTrialLog_TileTargetingEffect)
    			responseString = responseString + getTileTargetingEffectTrialLogAsResponseString((EffectTrialLog_TileTargetingEffect)_effectTrialLog);
			
    		return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getUnitTargetingEffectTrialLogAsResponseString(EffectTrialLog_UnitTargetingEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<TargetId>" + String.valueOf(_effectTrialLog.TargetId) + "</TargetId>";
	    	responseString = responseString + "<TargetName>" + _effectTrialLog.TargetName + "</TargetName>";
	    	responseString = responseString + "<TargetNickname>" + _effectTrialLog.TargetNickname + "</TargetNickname>";
	    	responseString = responseString + "<TargetLocationTileIndex>" + String.valueOf(_effectTrialLog.TargetLocationTileIndex) + "</TargetLocationTileIndex>";
	    	
    		if (_effectTrialLog instanceof EffectTrialLog_DamageEffect)
    			responseString = responseString + getDamageEffectTrialLogAsResponseString((EffectTrialLog_DamageEffect)_effectTrialLog);
        	else if (_effectTrialLog instanceof EffectTrialLog_HealEffect)
        		responseString = responseString + getHealEffectTrialLogAsResponseString((EffectTrialLog_HealEffect)_effectTrialLog);
        	else if (_effectTrialLog instanceof EffectTrialLog_StatusEffectAttachmentEffect)
        		responseString = responseString + getStatusEffectAttachmentEffectTrialLogAsResponseString((EffectTrialLog_StatusEffectAttachmentEffect)_effectTrialLog);
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getDamageEffectTrialLogAsResponseString(EffectTrialLog_DamageEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<WasImmune>" + String.valueOf(_effectTrialLog.WasImmune) + "</WasImmune>";
	    	responseString = responseString + "<WasCritical>" + String.valueOf(_effectTrialLog.WasCritical) + "</WasCritical>";
	    	responseString = responseString + "<Effectiveness>" + _effectTrialLog.Effectiveness.toString() + "</Effectiveness>";
	    	responseString = responseString + "<Value>" + String.valueOf(_effectTrialLog.Value) + "</Value>";
	    	responseString = responseString + "<RemainingHPAfterModification>" + String.valueOf(_effectTrialLog.RemainingHPAfterModification) + "</RemainingHPAfterModification>";
	    	
    		responseString = responseString + "</EffectTrialLog_DamageEffect>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getHealEffectTrialLogAsResponseString(EffectTrialLog_HealEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<WasCritical>" + String.valueOf(_effectTrialLog.WasCritical) + "</WasCritical>";
	    	responseString = responseString + "<Value>" + String.valueOf(_effectTrialLog.Value) + "</Value>";
	    	responseString = responseString + "<RemainingHPAfterModification>" + String.valueOf(_effectTrialLog.RemainingHPAfterModification) + "</RemainingHPAfterModification>";
	    	
    		responseString = responseString + "</EffectTrialLog_HealEffect>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getStatusEffectAttachmentEffectTrialLogAsResponseString(EffectTrialLog_StatusEffectAttachmentEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<AttachedStatusEffectId>" + String.valueOf(_effectTrialLog.AttachedStatusEffectId) + "</AttachedStatusEffectId>";
	    	
    		responseString = responseString + "</EffectTrialLog_StatusEffectAttachmentEffect>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getTileTargetingEffectTrialLogAsResponseString(EffectTrialLog_TileTargetingEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<TargetCoord>";
	    	responseString = responseString + "<X>" + String.valueOf(_effectTrialLog.targetCoord.X) + "</X>";
	    	responseString = responseString + "<Y>" + String.valueOf(_effectTrialLog.targetCoord.Y) + "</Y>";
	    	responseString = responseString + "</TargetCoord>";
	    	
    		if (_effectTrialLog instanceof EffectTrialLog_MovementEffect)
    			responseString = responseString + getMovementEffectTrialLogAsResponseString((EffectTrialLog_MovementEffect)_effectTrialLog);
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getMovementEffectTrialLogAsResponseString(EffectTrialLog_MovementEffect _effectTrialLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<InitialCoord>";
	    	responseString = responseString + "<X>" + String.valueOf(_effectTrialLog.initialCoord.X) + "</X>";
	    	responseString = responseString + "<Y>" + String.valueOf(_effectTrialLog.initialCoord.Y) + "</Y>";
	    	responseString = responseString + "</InitialCoord>";
	    	
    		responseString = responseString + "</EffectTrialLog_MovementEffect>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getStatusEffectLogAsResponseString(StatusEffectLog _statusEffectLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<EffectHolderId>" + String.valueOf(_statusEffectLog.EffectHolderId) + "</EffectHolderId>";
	    	responseString = responseString + "<EffectHolderName>" + _statusEffectLog.EffectHolderName + "</EffectHolderName>";
	    	responseString = responseString + "<EffectHolderNickname>" + _statusEffectLog.EffectHolderNickname + "</EffectHolderNickname>";
	    	
    		if (_statusEffectLog instanceof StatusEffectLog_HPModification)
    			responseString = responseString + getHPModificationStatusEffectLogAsResponseString((StatusEffectLog_HPModification)_statusEffectLog);
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getHPModificationStatusEffectLogAsResponseString(StatusEffectLog_HPModification _statusEffectLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<IsPositive>" + String.valueOf(_statusEffectLog.IsPositive) + "</IsPositive>";
	    	responseString = responseString + "<Value>" + String.valueOf(_statusEffectLog.Value) + "</Value>";
	    	responseString = responseString + "<RemainingHPAfterModification>" + String.valueOf(_statusEffectLog.RemainingHPAfterModification) + "</RemainingHPAfterModification>";
	    	
    		responseString = responseString + "</StatusEffectLog_HPModification>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getActionLogAsResponseString(ActionLog _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<ActorId>" + String.valueOf(_actionLog.ActorId) + "</ActorId>";
	    	responseString = responseString + "<ActorName>" + _actionLog.ActorName + "</ActorName>";
	    	responseString = responseString + "<ActorNickname>" + _actionLog.ActorNickname + "</ActorNickname>";
	    	
    		if (_actionLog instanceof ActionLog_Move)
    			responseString = responseString + getMoveActionLogAsResponseString((ActionLog_Move)_actionLog);
    		else if (_actionLog instanceof ActionLog_Attack)
    			responseString = responseString + getAttackActionLogAsResponseString((ActionLog_Attack)_actionLog);
    		else if (_actionLog instanceof ActionLog_Skill)
    			responseString = responseString + getSkillActionLogAsResponseString((ActionLog_Skill)_actionLog);
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    private static String getMoveActionLogAsResponseString(ActionLog_Move _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
			responseString = responseString + "<InitialCoord>";
			responseString = responseString + "<X>" + String.valueOf(_actionLog.InitialCoord.X) + "</X>";
			responseString = responseString + "<Y>" + String.valueOf(_actionLog.InitialCoord.Y) + "</Y>";
			responseString = responseString + "</InitialCoord>";
			
			responseString = responseString + "<EventualCoord>";
			responseString = responseString + "<X>" + String.valueOf(_actionLog.EventualCoord.X) + "</X>";
			responseString = responseString + "<Y>" + String.valueOf(_actionLog.EventualCoord.Y) + "</Y>";
			responseString = responseString + "</EventualCoord>";
			
    		responseString = responseString + "</ActionLog_Move>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getAttackActionLogAsResponseString(ActionLog_Attack _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
			responseString = responseString + "<ActorLocationTileIndex>" + String.valueOf(_actionLog.ActorLocationTileIndex) + "</ActorLocationTileIndex>";
			responseString = responseString + "<TargetId>" + String.valueOf(_actionLog.ActorLocationTileIndex) + "</TargetId>";
			responseString = responseString + "<TargetLocationTileIndex>" + String.valueOf(_actionLog.TargetLocationTileIndex) + "</TargetLocationTileIndex>";
			
    		responseString = responseString + "</ActionLog_Attack>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getSkillActionLogAsResponseString(ActionLog_Skill _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
	    	responseString = responseString + "<SkillName>" + _actionLog.SkillName + "</SkillName>";
	    	responseString = responseString + "<ActorLocationTileIndex>" + String.valueOf(_actionLog.ActorLocationTileIndex) + "</ActorLocationTileIndex>";
	    	responseString = responseString + "<AnimationId>" + String.valueOf(_actionLog.AnimationId) + "</AnimationId>";
	    	
    		if (_actionLog instanceof ActionLog_UnitTargetingSkill)
    			responseString = responseString + getUnitTargetingSkillActionLogAsResponseString((ActionLog_UnitTargetingSkill)_actionLog);
    		else if (_actionLog instanceof ActionLog_TileTargetingSkill)
    			responseString = responseString + getTileTargetingSkillActionLogAsResponseString((ActionLog_TileTargetingSkill)_actionLog);
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    private static String getUnitTargetingSkillActionLogAsResponseString(ActionLog_UnitTargetingSkill _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
			responseString = responseString + "<Targets>";
			for (TargetInfo targetInfo : _actionLog.getTargetsName_Nickname_OwnerName())
			{
				responseString = responseString + "<Target>";
				responseString = responseString + "<Name>" + targetInfo.name + "</Name>";
				responseString = responseString + "<Nickname>" + targetInfo.nickname + "</Nickname>";
				responseString = responseString + "<OwnerName>" + targetInfo.ownerName + "</OwnerName>";
				responseString = responseString + "</Target>";
			}
			responseString = responseString + "</Targets>";
			
			responseString = responseString + "<SecondaryTargets>";
			for (TargetInfo targetInfo : _actionLog.getSecondaryTargetsName_Nickname_OwnerName())
			{
				responseString = responseString + "<Target>";
				responseString = responseString + "<Name>" + targetInfo.name + "</Name>";
				responseString = responseString + "<Nickname>" + targetInfo.nickname + "</Nickname>";
				responseString = responseString + "<OwnerName>" + targetInfo.ownerName + "</OwnerName>";
				responseString = responseString + "</Target>";
			}
			responseString = responseString + "</SecondaryTargets>";
				
    		responseString = responseString + "</ActionLog_UnitTargetingSkill>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    
    private static String getTileTargetingSkillActionLogAsResponseString(ActionLog_TileTargetingSkill _actionLog)
    {
    	String responseString = "";
    	
		try 
		{
			responseString = responseString + "<Coords>";
			for (_2DCoord coord : _actionLog.getTargetCoords())
			{
				responseString = responseString + "<Coord>";
				responseString = responseString + "<X>" + String.valueOf(coord.X) + "</X>";
				responseString = responseString + "<Y>" + String.valueOf(coord.Y) + "</Y>";
				responseString = responseString + "</Coord>";
			}
			responseString = responseString + "</Coords>";
			
			responseString = responseString + "<SecondaryCoords>";
			for (_2DCoord coord : _actionLog.getSecondaryTargetCoords())
			{
				responseString = responseString + "<Coord>";
				responseString = responseString + "<X>" + String.valueOf(coord.X) + "</X>";
				responseString = responseString + "<Y>" + String.valueOf(coord.Y) + "</Y>";
				responseString = responseString + "</Coord>";
			}
			responseString = responseString + "</SecondaryCoords>";
				
    		responseString = responseString + "</ActionLog_TileTargetingSkill>";
    		
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    public static String getMaxNumberOfTargetsAsResponseString(int _playerId, String _skillName)
    {    	
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(), 
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Get data of currently selected unit
	    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
	    	
	    	//Get skill used by the unit
	    	ActiveSkill skill = null;
	    	int maxNumOfTargets = 0;
	    	if (_skillName.equals(SharedGameDataContainer.getBasicAttack().BaseInfo.Name)) //The first skill of the list is the basic attack
	    	{
		    	skill = SharedGameDataContainer.getBasicAttack();
		    	
		    	maxNumOfTargets = Calculator.MaxNumOfTargets(unit, skill, match.GetAttackTargetableArea(unit), match);
	    	}
	    	else //If it is not the basic attack skill
	    	{
		    	skill = (ActiveSkill)(Linq.first(unit.getSkills(), x -> x.BaseInfo.Name.equals(_skillName)));
		    	
		    	maxNumOfTargets = Calculator.MaxNumOfTargets(unit, skill, match.GetSkillTargetableArea(unit, skill), match);	
	    	}
	    	
	    	responseString = String.valueOf(maxNumOfTargets);
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    public static String getMovableAndSelectableAreaAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Get data of currently selected unit
	    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
	    	
	    	responseString = responseString + "<Coords>";
	    	Map<_2DCoord, Boolean> movableAndSelectableArea = match.GetMovableAndSelectableArea(unit);
	    	for (Map.Entry<_2DCoord, Boolean> entry : movableAndSelectableArea.entrySet())
	    	{
		    	responseString = responseString + "<Coord>";
		    	responseString = responseString + "<X>" + String.valueOf(entry.getKey().X) + "</X>";
		    	responseString = responseString + "<Y>" + String.valueOf(entry.getKey().Y) + "</Y>";
		    	responseString = responseString + "<IsSelectable>" + String.valueOf(entry.getValue()) + "</IsSelectable>";
		    	responseString = responseString + "</Coord>";
	    	}
	    	responseString = responseString + "</Coords>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    public static String getAttackTargetableAndSelectableAreaAsResponseString(int _playerId)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Get data of currently selected unit
	    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
	    	
	    	responseString = responseString + "<Coords>";
	    	Map<_2DCoord, Boolean> attackTargetableAndSelectableArea = match.GetAttackTargetableAndSelectableArea(unit);
	    	for (Map.Entry<_2DCoord, Boolean> entry : attackTargetableAndSelectableArea.entrySet())
	    	{
		    	responseString = responseString + "<Coord>";
		    	responseString = responseString + "<X>" + String.valueOf(entry.getKey().X) + "</X>";
		    	responseString = responseString + "<Y>" + String.valueOf(entry.getKey().Y) + "</Y>";
		    	responseString = responseString + "<IsSelectable>" + String.valueOf(entry.getValue()) + "</IsSelectable>";
		    	responseString = responseString + "</Coord>";
	    	}
	    	responseString = responseString + "</Coords>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
    
    public static String getSkillTargetableAndSelectableAreaAsResponseString(int _playerId, String _skillName)
    {
    	String responseString = "";
    	
    	if (!MatchDataContainer.getIsInitialized())
    		return responseString;
    	
		try 
		{
			//Get match including the player
			BattleSystemCore match = Linq.first(MatchDataContainer.getMatches(),
												x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
			
	    	//Get players
	    	List<PlayerOnBoard> players = match.Field.getPlayers();
	    	
	    	//Get player data
	    	PlayerOnBoard player = Linq.first(players, z -> z.Id == _playerId);
	    	
	    	//Get data of currently selected unit
	    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
	    	
	    	//Get data of the skill that corresponds to the _skillName
	    	ActiveSkill skill = Linq.first(Linq.ofType(unit.getSkills(), ActiveSkill.class),
	    									x -> x.BaseInfo.Name.equals(_skillName));
	    	
	    	responseString = responseString + "<Coords>";
	    	Map<_2DCoord, Boolean> skillTargetableAndSelectableArea;
	    	if (skill instanceof CostRequiringSkill)
	    		skillTargetableAndSelectableArea = match.GetSkillTargetableAndSelectableArea(unit, (CostRequiringSkill)skill);
	    	else //skill instanceof UltimateSkill
	    		skillTargetableAndSelectableArea = match.GetSkillTargetableAndSelectableArea(unit, (UltimateSkill)skill);
	    		
	    	for (Map.Entry<_2DCoord, Boolean> entry : skillTargetableAndSelectableArea.entrySet())
	    	{
		    	responseString = responseString + "<Coord>";
		    	responseString = responseString + "<X>" + String.valueOf(entry.getKey().X) + "</X>";
		    	responseString = responseString + "<Y>" + String.valueOf(entry.getKey().Y) + "</Y>";
		    	responseString = responseString + "<IsSelectable>" + String.valueOf(entry.getValue()) + "</IsSelectable>";
		    	responseString = responseString + "</Coord>";
	    	}
	    	responseString = responseString + "</Coords>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

    public static String getGachaObjectsAsResponseString(eGachaClassification _gachaClassification, List<Object> _objects)
    {
    	String responseString = "<Objects>";
    	
		try 
		{
			switch (_gachaClassification)
			{
				default: //case Unit
					{
						for (Object object : _objects)
						{
							Unit unit = (Unit)object;
					    	responseString = responseString + "<Object>";
					    	responseString = responseString + "<Id>" + String.valueOf(unit.BaseInfo.Id) + "</Id>";
					    	responseString = responseString + "<UniqueId>" + String.valueOf(unit.UniqueId) + "</UniqueId>";
					    	responseString = responseString + "<AccumulatedExperience>" + String.valueOf(unit.getAccumulatedExperience()) + "</AccumulatedExperience>";
					    	responseString = responseString + "</Object>";
						}
					}
					break;
					
				case Weapon:
					{
						for (Object object : _objects)
						{
							Weapon weapon = (Weapon)object;
					    	responseString = responseString + "<Object>";
					    	responseString = responseString + "<Id>" + String.valueOf(weapon.BaseInfo.Id) + "</Id>";
					    	responseString = responseString + "<UniqueId>" + String.valueOf(weapon.UniqueId) + "</UniqueId>";
					    	int accumulatedExperience = 0;
					    	if (weapon instanceof LevelableWeapon)
					    		accumulatedExperience = ((LevelableWeapon)weapon).getAccumulatedExperience();
					    	else if (weapon instanceof LevelableTransformableWeapon)
				    			accumulatedExperience = ((LevelableTransformableWeapon)weapon).getAccumulatedExperience();
					    	responseString = responseString + "<AccumulatedExperience>" + String.valueOf(accumulatedExperience) + "</AccumulatedExperience>";
					    	responseString = responseString + "</Object>";
						}
					}
					break;
					
				case Armour:
					{
						for (Object object : _objects)
						{
							Armour armour = (Armour)object;
					    	responseString = responseString + "<Object>";
					    	responseString = responseString + "<Id>" + String.valueOf(armour.BaseInfo.Id) + "</Id>";
					    	responseString = responseString + "<UniqueId>" + String.valueOf(armour.UniqueId) + "</UniqueId>";
					    	responseString = responseString + "</Object>";
						}
					}
					break;
					
				case Accessory:
					{
						for (Object object : _objects)
						{
							Accessory accessory = (Accessory)object;
					    	responseString = responseString + "<Object>";
					    	responseString = responseString + "<Id>" + String.valueOf(accessory.BaseInfo.Id) + "</Id>";
					    	responseString = responseString + "<UniqueId>" + String.valueOf(accessory.UniqueId) + "</UniqueId>";
					    	responseString = responseString + "</Object>";
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
						for (Object object : _objects)
						{
							Item item = (Item)object;
					    	responseString = responseString + "<Object>";
					    	responseString = responseString + "<Id>" + String.valueOf(item.Id) + "</Id>";
					    	responseString = responseString + "</Object>";
						}
					}
					break;
			}	
		
	    	responseString = responseString + "</Objects>";
	    	
			return responseString;
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }
}
