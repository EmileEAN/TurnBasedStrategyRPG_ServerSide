package eean_games.tbsg._01.gacha;

public class GachaDispensationAvailabilityInfo 
{
	public GachaDispensationAvailabilityInfo(int _playerId, int _gachaId, int _dispensationOptionId, int _remainingAttempts)
	{
		playerId = _playerId;
		gachaId = _gachaId;
		dispensationOptionId = _dispensationOptionId;
		remainingAttempts = _remainingAttempts;
	}

	public final int playerId;
	public final int gachaId;
	public final int dispensationOptionId;
	public int remainingAttempts;
}
