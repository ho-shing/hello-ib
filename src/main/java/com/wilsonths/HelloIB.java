package com.wilsonths;

import java.util.ArrayList;

import com.ib.controller.*;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController.IBulletinHandler;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.ApiController.ITimeHandler;
import com.ib.controller.Types.NewsType;

public class HelloIB implements IConnectionHandler {
	
	class MyLogger implements ILogger {

		@Override
		public void log(String str) {
			//System.out.print(str);
		}
	}
	
	private final ApiController controller = new ApiController(this, new MyLogger(), new MyLogger());

	static HelloIB INSTANCE = new HelloIB();
	
	public static void main(String[] args) {
		INSTANCE.run();
	}
	
	private void run() {
		System.out.println("Begin connecting...");
		this.controller.connect( "127.0.0.1", 7496, 0);
	}
	
	@Override
	public void connected() {
		System.out.println("connected");
		
		
		this.controller.reqCurrentTime( new ITimeHandler() {
			@Override public void currentTime(long time) {
				System.out.println( "Server date/time is " + Formats.fmtDate(time * 1000) );
			}
		});
		
		this.controller.reqBulletins( true, new IBulletinHandler() {
			@Override public void bulletin(int msgId, NewsType newsType, String message, String exchange) {
				String str = String.format( "Received bulletin:  type=%s  exchange=%s", newsType, exchange);
				System.out.println( str);
				System.out.println( message);
			}
		});
	}
	
	@Override
	public void disconnected() {
		System.out.println( "disconnected");
	}

	@Override
	public void accountList(ArrayList<String> list) {
		System.out.println( "Received account list");
		for (String item : list) {
			System.out.println(item);
		}
	}

	@Override
	public void show( final String str) {
		System.out.println( "Show:" + str);
	}

	@Override
	public void error(Exception e) {
		System.out.println( "Error:" + e.toString() );
	}
	
	@Override
	public void message(int id, int errorCode, String errorMsg) {
		System.out.println( "Message:" + id + " " + errorCode + " " + errorMsg);
	}
}
