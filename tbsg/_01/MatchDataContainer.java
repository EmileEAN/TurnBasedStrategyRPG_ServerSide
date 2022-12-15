package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eean_games.main.Linq;
import eean_games.main.MTRandom;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public class MatchDataContainer 
{
    //Getters
	public static List<BattleSystemCore> getMatches() { return Collections.unmodifiableList(matches); }
	
    public static boolean getIsInitialized() { return isInitialized; }
    //End Getters
    
    //Private Static Fields
	private static List<BattleSystemCore> matches;
    private static List<MatchingPlayerInfo> playersWaitingForMatching;
    
    private static boolean isInitialized = false;
    //End Private Static Fields
    
    //Public Methods
    public static void changeUnit(int _playerId, int _unitIndex)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	match.ChangeSelectedUnit(player, _unitIndex);
    }
    
    public static void changeTurn(int _playerId)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	match.ChangeTurn(player);
    }
    
    public static void concede(int _playerId)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	match.EndMatch(player);
    }
    
    public static void moveUnit(int _playerId, _2DCoord _destination)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	//Get data of the unit currently selected by the player
    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
    	
    	match.MoveUnit(unit, _destination);
    }
    
    public static void attack(int _playerId, List<_2DCoord> _targetCoords)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	//Get data of the unit currently selected by the player
    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
    	
    	match.RequestAttack(unit, _targetCoords);
    }
    
    public static void useSkill(int _playerId, String _skillName, List<_2DCoord> _targetCoords, List<_2DCoord> _secondaryTargetCoords)
    {
    	//Get the match that contains the player with the given id
    	BattleSystemCore match = Linq.first(matches,
    										x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));
    	
    	//Get data of the corresponding player
    	PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
    	
    	//Get data of the unit currently selected by the player
    	UnitInstance unit = player.AlliedUnits.get(player.SelectedUnitIndex);
    	
    	//Get data of the skill that corresponds to the _skillName
    	ActiveSkill skill = Linq.first(Linq.ofType(unit.getSkills(), ActiveSkill.class),
    									x -> x.BaseInfo.Name.equals(_skillName));
    	
    	match.RequestSkillUse(unit, skill, _targetCoords, _secondaryTargetCoords);
    }
    
    public static boolean hasPlayerBeenAssignedToMatch(Player _player) { return Linq.any(matches, x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _player.Id)); }
    
    public static String assignPlayerToMatch(Player _player, int _teamIndex)
    {
    	if (!isInitialized)
    		initialize();
    	if (!isInitialized)
    		return "error";
    	
    	try
    	{
        	//Check whether the given _player is already assigned to a match
        	boolean isPlayerInMatch = hasPlayerBeenAssignedToMatch(_player);
        	
        	//Check whether the given _player is already assigned to the waiting list
        	boolean isPlayerInWaitingList = Linq.any(playersWaitingForMatching, x -> x.player == _player);
        			
        	if (isPlayerInMatch) 
        		return "alreadyInMatch";
        	else if (isPlayerInWaitingList)
        		return "alreadyWaiting";
        	else //The given _player is not in match nor in the waiting list
        		playersWaitingForMatching.add(new MatchingPlayerInfo(_player, _teamIndex));
        	
        	assignPlayersInWaitingListToMatch();
        	
        	System.out.println("Matches Running: " + String.valueOf(matches.size()));
        	System.out.println("Players in Waiting List: " + String.valueOf(playersWaitingForMatching.size()));
        	
        	return "matching";
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		playersWaitingForMatching.remove(Linq.first(playersWaitingForMatching, x -> x.player == _player));
    		return "error";
    	}
    }
    
    public static String cancelPlayerMatching(Player _player)
    {
    	if (!isInitialized)
    		initialize();
    	if (!isInitialized)
    		return "error";
    	
    	try
    	{
        	//Check whether the given _player is already assigned to a match
        	boolean isPlayerInMatch = hasPlayerBeenAssignedToMatch(_player);
        	
        	//Check whether the given _player is already assigned to the waiting list
        	boolean isPlayerInWaitingList = Linq.any(playersWaitingForMatching, x -> x.player == _player);
        			
        	if (isPlayerInMatch) 
        		return "alreadyInMatch";
        	else if (isPlayerInWaitingList)
        		removePlayerFromWaitingList(_player);
        	//else The given _player is not in match nor in the waiting list
        	
        	System.out.println("Matching Cancelled by " + _player.Name);
        	System.out.println("Matches Running: " + String.valueOf(matches.size()));
        	System.out.println("Players in Waiting List: " + String.valueOf(playersWaitingForMatching.size()));
    	
        	return "cancelled";
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		playersWaitingForMatching.remove(Linq.first(playersWaitingForMatching, x -> x.player == _player));
    		return "error";
    	}
    }
    
    public static void removePlayerFromWaitingList(Player _player) 
    {
    	if (!isInitialized)
    		return;
    	
    	MatchingPlayerInfo matchingPlayerInfo = Linq.firstOrDefault(playersWaitingForMatching, x -> x.player == _player);
    	if (matchingPlayerInfo != null)
    		playersWaitingForMatching.remove(matchingPlayerInfo);
    }
    
    public static void makePlayerLose(Player _player) 
    {
    	BattleSystemCore match = Linq.firstOrDefault(matches, 
    												 x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _player.Id));
    	
    	if (match != null)
    	{
    		PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _player.Id);
    		match.EndMatch(player);
    		match.confirmEndMatchStatusAcquisition(player);
    	}
    }
    
    public static void confirmEndMatchStatusAcquisition(int _playerId)
    {
    	BattleSystemCore match = Linq.firstOrDefault(matches, 
				 x -> Linq.any(x.Field.getPlayers(), y -> y.Id == _playerId));

    	if (match != null)
    	{
    		PlayerOnBoard player = Linq.first(match.Field.getPlayers(), x -> x.Id == _playerId);
	    	match.confirmEndMatchStatusAcquisition(player);
    	}
    }
    
    public static void removeEndedMatches()
    {
    	if (!isInitialized)
    		return;
    	
    	List<BattleSystemCore> endedMatches = Linq.where(matches, x -> x.getCanBattleSystemInstanceBeDeleted() == true);
    	for (BattleSystemCore endedMatch : endedMatches)
    	{
    		matches.remove(endedMatch);
    		List<PlayerOnBoard> players = endedMatch.Field.getPlayers();
    		System.out.println("Removed ended match for " + players.get(0).Name + "(Player 1) and " + players.get(1).Name +"(Player 2)!");
    	}	
    }
    //End Public Methods
    
    //Private Methods
    private static void initialize()
    {
    	if (isInitialized)
    		return;
    	
    	if (matches == null)
    		matches = Collections.synchronizedList(new ArrayList<BattleSystemCore>());
    	
    	if (playersWaitingForMatching == null)
    		playersWaitingForMatching = Collections.synchronizedList(new ArrayList<MatchingPlayerInfo>());
    	
    	isInitialized = true;
    }
    
    private static void assignPlayersInWaitingListToMatch()
    {
    	if (playersWaitingForMatching.size() < 2)
    		return;
    		
    	//Get information of players to assign to a new match
    	MatchingPlayerInfo matchingPlayerInfo1 = playersWaitingForMatching.get(0);
    	MatchingPlayerInfo matchingPlayerInfo2 = playersWaitingForMatching.get(1);
    	
    	//Set the order in which each player will start playing
		MTRandom.randInit();
		int randNum = MTRandom.getRand(1, 10);
		if (randNum > 5)
		{
			MatchingPlayerInfo tmp = matchingPlayerInfo1;
			matchingPlayerInfo1 = matchingPlayerInfo2;
			matchingPlayerInfo2 = tmp;
		}
		
		//Get players' data required to create a new battle field
    	Player player1 = matchingPlayerInfo1.player;
    	int teamIndexForPlayer1 = matchingPlayerInfo1.teamIndex;
    	
    	Player player2 = matchingPlayerInfo2.player;
    	int teamIndexForPlayer2 = matchingPlayerInfo2.teamIndex;
    	
    	//Load the tile set that will be used for the match
    	TileSet tileSet = SharedGameDataContainer.getTileSets().get(0);
    	
    	//Prepare new field for the match
    	Field field = Field.NewField(player1, teamIndexForPlayer1, player2, teamIndexForPlayer2, tileSet.getTileTypes());
    
    	//Instantiate the battle system for the match
    	BattleSystemCore match = new BattleSystemCore(field);
    	
    	matches.add(match);
    	
    	//Remove players from the waiting list
    	playersWaitingForMatching.remove(matchingPlayerInfo1);
    	playersWaitingForMatching.remove(matchingPlayerInfo2);
    	
    	assignPlayersInWaitingListToMatch(); // Loop until the waiting list contains less than two players
    }
    //End Private Methods
}
