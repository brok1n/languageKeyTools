package com.skywds.javafx.languagekeytools.view;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.skywds.javafx.languagekeytools.AppDelegate;
import com.skywds.javafx.languagekeytools.Main;
import com.skywds.javafx.languagekeytools.model.GlobalData;
import com.skywds.javafx.languagekeytools.model.JsonData;
import com.skywds.javafx.languagekeytools.utils.Trace;

public class RootLayerController
{

	@FXML
	private MenuItem closeItem;
	@FXML
	private MenuItem openItem;
	@FXML
	private MenuItem saveItem;
	@FXML
	private MenuItem aboutItem;
	@FXML
	private RadioMenuItem autoCleanInputFieldItem;

	private AppDelegate app; 
	
	@FXML
	private void handleSaveMenuItem()
	{
		System.out.println("save menu ");
		
		app.getContentLayerController().saveData();
		
	}
	
	@FXML
	private void handleOpenMenuItem()
	{
		System.out.println(" open menu ");
		
		openLocalDataFile();
		
	}
	
	//打开本地数据文件
	private void openLocalDataFile()
	{
		//检测是否有未保存的数据
		checkPreOldSaveData();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory( new File( "." ) );
		fileChooser.getExtensionFilters().add( JsonData.getInstance().getExtFilter() );  
		fileChooser.setTitle("打开");
		
		Stage s = new Stage();  
		File file = fileChooser.showOpenDialog( s );
		
		if( file == null )
		{
			Trace.i(" 用户未选择要打开的文件！ ");
			return;
		}
		
		String path = file.getAbsolutePath();
		
		Trace.i(" 用户选择了文件: " + path);
		
		GlobalData.getInstance().setFilePath(path);
		
		//开始读取并解析数据
		boolean readStatus = JsonData.getInstance().readDataFromFile();
		if( readStatus )
		{
			//读取成功。可以刷新界面数据
			app.getContentLayerController().flushTableView();
			Trace.i( "文件打开成功!" );
		}
		
		
	}

	private boolean checkPreOldSaveData()
	{
		if( !GlobalData.getInstance().isSaveSataus() )
		{
			Trace.e( "存在未保存数据" );
			return false;
		}
		
		return true;
	}

	@FXML
	private void handleCloseMenuItem()
	{
		System.out.println(" close menu item");
		
		app.onRequestCloseApp();
	}
	
	@FXML
	private void handleAutoCleanInputFieldItem()
	{
		System.out.println( " handleAutoCleanInputFieldItem " );
		
		if( autoCleanInputFieldItem.isSelected() )
		{
			GlobalData.getInstance().setAutoCleanInputField( true );
			System.out.println("auto clean inputfield change to true");
		}
		else
		{
			GlobalData.getInstance().setAutoCleanInputField( false );
			System.out.println("auto clean inputfield change to false");
		}
		
	}
	
	@FXML
	private void handleAboutMenuItem()
	{
		System.out.println(" about menu item ");

		Text versionText	= new Text( "Version:" + Main.VERSION );
		Text tipText	= new Text( "作者: brok1n  email: 452700765@qq.com" );
		
		AnchorPane panel = new AnchorPane( versionText, tipText );
		Scene scene = new Scene(panel, 350, 60);
		
		versionText.setLayoutX( 10 );
		versionText.setLayoutY( 20 );
		
		tipText.setLayoutX( 10 );
		double versionY	= versionText.getLayoutY();
		double versionH	= versionText.getLayoutBounds().getHeight();
		tipText.setY( versionY + versionH + 10 );
		Stage pop	= new Stage();
		//设置窗体只有关闭按钮
		pop.initStyle( StageStyle.UTILITY );
		pop.setScene( scene );
		pop.setTitle( "关于" );
		//禁止改变大小
		pop.setResizable( false );
		//设置窗体为模态窗体
		pop.initModality( Modality.APPLICATION_MODAL );
		pop.show();
		
		pop.setOnHidden( new EventHandler< WindowEvent >()
		{
			@Override
			public void handle( WindowEvent event )
			{
				// TODO Auto-generated method stub
				System.out.println(" on hidden");
				AppDelegate.static_stage.requestFocus();
			}
		} );
		
		System.out.println("show about dialog ");
	}

	public void init(AppDelegate app ) 
	{
		this.app = app;
		
		autoCleanInputFieldItem.setSelected( true );
		
		handleAutoCleanInputFieldItem();
		
	}
	
}
