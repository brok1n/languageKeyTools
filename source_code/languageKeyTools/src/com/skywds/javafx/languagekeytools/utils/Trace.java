package com.skywds.javafx.languagekeytools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.stage.Stage;

import com.skywds.javafx.languagekeytools.model.GlobalData;
import com.skywds.javafx.languagekeytools.view.ContentLayerController;

public class Trace
{
	static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	public static void i( String msg )
	{
		String log = msg + "  (" + format.format( new Date() ) + ")";
		
		GlobalData.getInstance().getLogData().add( log );
		
		showTrace();
		
		System.out.println("trace: " + log);
	}
	
	public static void e( String msg )
	{
		int lastId = GlobalData.getInstance().getLogData().size();
		
		String log = "ERROR:  " + msg + "  (" + format.format( new Date() ) + ")";
		GlobalData.getInstance().getLogData().add( log );
		
		showTrace( lastId );
		
		System.err.println("error: " + log );
	}
	
	private static void showTrace()
	{
		ContentLayerController.getLogListView().setItems( GlobalData.getInstance().getLogData() );
		
		ContentLayerController.getLogListView().scrollTo( ContentLayerController.getLogListView().getItems().size() );
		
		
	}
	
	private static void showTrace( int index )
	{
		ContentLayerController.getLogListView().setItems( GlobalData.getInstance().getLogData() );
		
		ContentLayerController.getLogListView().scrollTo( ContentLayerController.getLogListView().getItems().size() );
	}
}
