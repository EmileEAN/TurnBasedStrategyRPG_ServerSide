package eean_games.tbsg._01;

import eean_games.tbsg._01.player.Player;

public class SessionInfo 
{
	public SessionInfo(Player _player, long _latestActivityTime, String _sessionId)
	{
		player = _player;
		latestActivityTime = _latestActivityTime;
		sessionId = _sessionId;
	}
	
	public Player player;
	public long latestActivityTime;
	public String sessionId;
}
