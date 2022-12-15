package eean_games.tbsg._01;

import eean_games.tbsg._01.player.Player;

public class MatchingPlayerInfo
{
	public MatchingPlayerInfo(Player _player, int _teamIndex)
	{
		player = _player;
		teamIndex = _teamIndex;
	}
	
	//Public Read-only Fields
	public final Player player;
	
	public final int teamIndex;
	//End Public Read-only Fields
}
