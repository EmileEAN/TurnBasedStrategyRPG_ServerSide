package eean_games.tbsg._01.servlet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eean_games.main.WarningHider;
import eean_games.tbsg._01.periodicalTask.PeriodicalTask;

@WebListener
public class MainContextListener implements ServletContextListener
{
	//Private Fields
	private ScheduledExecutorService service;
	//End Private Fields
	
	@Override
	public void contextInitialized(ServletContextEvent _servletContextEvent) 
	{
		System.out.println("Starting server!");
		
		WarningHider.hideIlleagalAccessWarning();
		
		service = Executors.newSingleThreadScheduledExecutor(); //Tasks below only need one thread
		service.scheduleWithFixedDelay(new PeriodicalTask(), 0, 1, TimeUnit.SECONDS);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent _servletContextEvent) 
	{        
        service.shutdown();
        
        System.out.println("Shutting down!");
    }
}
