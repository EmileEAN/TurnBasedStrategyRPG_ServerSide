package eean_games.tbsg._01.gacha;

import eean_games.tbsg._01.enumerable.eCostType;

public class DispensationOption 
{
	public DispensationOption(int _id, eCostType _costType, int _costItemId, int _costValue, int _timesToDispense, int _attemptsAllowedPerPlayer, boolean _isNumberOfAttemptsPerDay)
	{
		id = _id;
		costType = _costType;
		costItemId = _costItemId;
		costValue = _costValue;
		timesToDispense = _timesToDispense;
		attemptsAllowedPerPlayer = _attemptsAllowedPerPlayer;
		isNumberOfAttemptsPerDay = _isNumberOfAttemptsPerDay;
	}
	
	//Public Read-only Fields
	public final int id;
	public final eCostType costType;
	public final int costItemId; //Used in case costType == eCostType.Item
	public final int costValue;
	public final int timesToDispense;
	
	public final boolean isNumberOfAttemptsPerDay; //Used to identify whether the value of attemptsAllowedPerPlayer must be reset in a daily basis
	//End Public Read-only Fields
	
	//Public Fields
	public int attemptsAllowedPerPlayer; //If -1, then it is infinite
	//End Public Fields
}
