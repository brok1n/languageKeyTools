package com.skywds.javafx.languagekeytools;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class App extends Application
{

	/**
	 * Application 运行时 init先被调用 之后start再被调用
	 * **/
	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		// TODO Auto-generated method stub
		onStart( primaryStage );
	}

	@Override
	public void init() throws Exception
	{
		// TODO Auto-generated method stub
		super.init();

		onInit();
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		super.stop();

		onStop();
	}

	public abstract void onInit();

	public abstract void onStart( Stage primaryStage );

	public abstract void onStop();

}
