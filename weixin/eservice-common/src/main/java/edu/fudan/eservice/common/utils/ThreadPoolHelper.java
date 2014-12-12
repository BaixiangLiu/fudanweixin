package edu.fudan.eservice.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ThreadPoolHelper {

	private static ThreadPoolHelper tph=null;
	private static ScheduledExecutorService sch=null;
	
	protected ThreadPoolHelper()
	{
		
		sch=Executors.newScheduledThreadPool(20);
	}
	
	public static synchronized ThreadPoolHelper getInstance()
	{
		if(tph==null)
			tph=new ThreadPoolHelper();
		return tph;
	}
	public ScheduledExecutorService getSchPool()
	{
		return sch;
	}
	
}
