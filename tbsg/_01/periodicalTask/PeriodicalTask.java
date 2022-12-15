package eean_games.tbsg._01.periodicalTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eean_games.tbsg._01.MatchDataContainer;
import eean_games.tbsg._01.SessionManager;
import eean_games.tbsg._01.SharedGameDataContainer;
import eean_games.tbsg._01.servlet.MainServlet;

public class PeriodicalTask implements Runnable
{
	public void run()
	{
		if (!SharedGameDataContainer.getIsInitialized())
		{			
			SharedGameDataContainer.initialize();
			
			if (SharedGameDataContainer.getIsInitialized())
			{
				System.out.println("----------");
				System.out.println("---------- Game initialized successfully ----------");
				System.out.println("----------");
			}
		}
		else
		{
			//System.out.println("------------------Periodical Task Called!--------------------");
			
			//Terminate Timed-out Sessions
			int numOfTerminatedSessions = SessionManager.terminateTimedOutSessions();
			int numOfActiveSessions = SessionManager.getNumberOfSessions();
			
			if (numOfTerminatedSessions != 0)
			{
				String message = String.valueOf(numOfTerminatedSessions) + " timed-out sessions have been terminated. "
						+ String.valueOf(numOfActiveSessions) + " active session(s) left.";
				
				System.out.println(message);
			}
			
			//Remove Ended Matches
			MatchDataContainer.removeEndedMatches();
			
			//Reload Gachas
			DateFormat dateFormat_minutesAndSeconds = new SimpleDateFormat("mm:ss"); 
			Date date = new Date();
			String minutesAndSecondsString = dateFormat_minutesAndSeconds.format(date);
			if (minutesAndSecondsString.equals("00:00") || minutesAndSecondsString.equals("30:00"))
			{
				SharedGameDataContainer.reloadGachas();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				System.out.println("Reloaded gachas!\tCurrent time: " + dateFormat.format(date));
				
				MainServlet.increaseUpdateNum();
			}
		}
	}
}
