package com.wilsonths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ib.controller.*;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController.IBulletinHandler;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.ApiController.ITimeHandler;
import com.ib.controller.ApiController.ITopMktDataHandler;
import com.ib.controller.Types.MktDataType;
import com.ib.controller.Types.NewsType;
import com.ib.controller.Types.SecType;

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
		
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String ticker;
		    
		    do  {
		    	ticker = bufferRead.readLine();
		    	
		    	NewContract contract = new NewContract();
				contract.conid(0);
				contract.symbol(ticker.toUpperCase());
				contract.secType(SecType.STK);
				contract.exchange("SMART");
				contract.currency("USD");
				
				this.controller.reqTopMktData(contract, "", true, new ITopMktDataHandler() {
					
					@Override
					public void tickString(NewTickType tickType, String value) {
						System.out.println("tickString - NewTickType:" + tickType.toString() + " value:" + value);
					}
					
					@Override
					public void tickSnapshotEnd() {
						System.out.println("tickSnapshotEnd");
					}
					
					@Override
					public void tickSize(NewTickType tickType, int size) {
						System.out.println("tickSize - NewTickType:" + tickType.toString() + " size:" + size);
						
					}
					
					@Override
					public void tickPrice(NewTickType tickType, double price, int canAutoExecute) {
						System.out.println("tickPrice - NewTickType:" + tickType.toString() + " price:" + price + " canAutoExecute:" + canAutoExecute);
					}
					
					@Override
					public void marketDataType(MktDataType marketDataType) {
						System.out.println("marketDataType - marketDataType:" + marketDataType.toString());
					}
				});
		    	
		    } while (!ticker.equals("quit"));
		}
		catch(IOException e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		
		this.controller.disconnect();
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
