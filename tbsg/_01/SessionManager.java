package eean_games.tbsg._01;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.catalina.util.StandardSessionIdGenerator;

import eean_games.main.Linq;
import eean_games.tbsg._01.player.Player;

public class SessionManager 
{
	//Public Constant Fields
	public static final long ALLOWED_INACTIVITY_TIME = 1000 * 60 * 1; //Milliseconds
	//End Public Constant Fields
	
	//Getters
	public static int getNumberOfSessions() { return sessions.size(); }
	
	public static int getPlayerId(String _sessionId)
	{
		SessionInfo session = Linq.firstOrDefault(sessions, x -> x.sessionId.equals(_sessionId));
		if (session != null)
			return session.player.Id;
		else
			return -1;
	}
	//End Getters
	
	//Private Static Fields
	private static List<SessionInfo> sessions = new CopyOnWriteArrayList<SessionInfo>();
	//End Private Static Fields
	
	/**
	 * Initiates a new session if no session exists for the account.<br>
	 * Returns a unique session ID if a new session has been initiated. Else, "" will be returned. 
	 */
	public static String initiateSession(Player _player)
	{
		if (_player != null && isLoggedIn(_player))
			return "";
		
		long currentTime = System.currentTimeMillis();
		
		StandardSessionIdGenerator ssig = new StandardSessionIdGenerator();
		String sessionId = ssig.generateSessionId();
		
		sessions.add(new SessionInfo(_player, currentTime, sessionId));
		return sessionId;
	}
	
	public static boolean updateConnectionStatus(String _sessionId)
	{	
		long currentTime = System.currentTimeMillis();
		SessionInfo session = Linq.firstOrDefault(sessions, x->x.sessionId.equals(_sessionId));
		if (session == null)
			return false;
		
		session.latestActivityTime = currentTime;
		
		return true;
	}
	
	/**
	 * Terminates all sessions that have been timed out.<br>
	 * Returns the number of sessions that have been terminated.
	 */
	public static int terminateTimedOutSessions()
	{
		long currentTime = System.currentTimeMillis();
		
		int numberOfSessionsTerminated= 0;
		
		for (SessionInfo session : sessions)
		{
			long timeElapsedSinceLastActivity = currentTime - session.latestActivityTime;
			
			if (timeElapsedSinceLastActivity > ALLOWED_INACTIVITY_TIME)
			{
				terminateSession(session);
				MatchDataContainer.removePlayerFromWaitingList(session.player);
				numberOfSessionsTerminated++;
			}
		}
		
		return numberOfSessionsTerminated;
	}
	
	private static void terminateSession(SessionInfo _session)
	{
		if (sessions.contains(_session))
			sessions.remove(_session);
	}
	
	public static boolean isLoggedIn(Player _player) { return Linq.any(sessions, x->x.player == _player); }
}
