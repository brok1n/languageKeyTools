package com.skywds.javafx.languagekeytools;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.skywds.javafx.languagekeytools.model.GlobalData;
import com.skywds.javafx.languagekeytools.utils.Trace;
import com.skywds.javafx.languagekeytools.view.ContentLayerController;
import com.skywds.javafx.languagekeytools.view.RootLayerController;

public class AppDelegate extends App
{
	public static Stage static_stage;
	
	private Scene		m_scene;
	private BorderPane	m_rootLayer;
	private AnchorPane	m_contentLayer;
	private RootLayerController		m_rootLayerController;
	private ContentLayerController	m_contentLayerController;

	@Override
	public void onStart( Stage primaryStage )
	{
		static_stage = primaryStage;
		
		initView();

		show();
	}

	private void initView()
	{
		// TODO Auto-generated method stub
		
		initContentLayer();
		initRootLayer();
		
	}
	
	private void initContentLayer()
	{
		// TODO Auto-generated method stub
		try
		{
			FXMLLoader loader	= new FXMLLoader();
			loader.setLocation( getClass().getResource( "view/ContentLayer.fxml" ) );
			m_contentLayer		= (AnchorPane)loader.load();
			
			m_contentLayerController = (ContentLayerController)loader.getController();
			m_contentLayerController.init( this );
			
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
	}

	private void initRootLayer()
	{
		// TODO Auto-generated method stub
		try
		{
			FXMLLoader loader	= new FXMLLoader();
			loader.setLocation( getClass().getResource( "view/RootLayer.fxml" ) );
			m_rootLayer = (BorderPane)loader.load();
			
			m_rootLayer.setCenter( m_contentLayer );
			
			m_rootLayerController = (RootLayerController)loader.getController();
			m_rootLayerController.init( this );
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
	}

	private void show()
	{
		// TODO Auto-generated method stub
		m_scene = new Scene( m_rootLayer );
		static_stage.setScene( m_scene );
		static_stage.setResizable( false );
		static_stage.setTitle( Main.SOFT_NAME + " " + Main.VERSION );
		
		static_stage.getIcons().add( new Image(AppDelegate.class.getResource("languageKeyTools.png").toExternalForm()) );
		
		static_stage.show();
		
		static_stage.setOnCloseRequest( new EventHandler< WindowEvent >()
		{
			@Override
			public void handle( WindowEvent event )
			{
				//阻止窗体关闭、我们处理关闭前的操作。处理完成后再关闭
				event.consume();
				
				//用户点击了关闭 窗体按钮
				onRequestCloseApp();
				
			}
		} );
		
	}

	//用户数据未保存、弹出提示框
	public void showTipDialog()
	{
		//窗体宽度高度
		double windowWidth = 350;
		double windowHeight = 120;
		
		Stage pop	= new Stage();
		
		AnchorPane panel = getTipDialogPanel( windowWidth, windowHeight, pop );
		Scene scene = new Scene(panel, windowWidth, windowHeight);
		
		//设置窗体只有关闭按钮
		pop.initStyle( StageStyle.UTILITY );
		pop.setScene( scene );
		pop.setTitle( "警告" );
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
		
	}

	//创建 弹出的提示框界面
	private AnchorPane getTipDialogPanel( double windowWidth, double windowHeight, Stage pop )
	{
		Text tipText	= new Text( "警告: 存在未保存的数据！是否保存?" );
		tipText.setFont( Font.font( Font.getDefault().getFamily(), 20 ) );
		tipText.setLayoutX( 10 );
		tipText.setLayoutY( 30 );
		tipText.setWrappingWidth( windowWidth - 20 );
		
		Button okBtn = new Button( "保存" );
		okBtn.setFont( Font.font( Font.getDefault().getFamily(), 20 ) );
		okBtn.setLineSpacing( 20 );
		//okBtn.setLayoutX( windowWidth - 90 );
		//okBtn.setLayoutY( 70 );
		
		Button cancelBtn = new Button( "取消" );
		cancelBtn.setFont( Font.font( Font.getDefault().getFamily(), 20 ) );
		cancelBtn.setLineSpacing( 20 );
		//cancelBtn.setLayoutX( okBtn.getLayoutX() - 100 );
		//cancelBtn.setLayoutY( 70 );
		
		Button dontSaveAndExitBtn = new Button( "不保存退出" );
		dontSaveAndExitBtn.setFont( Font.font( Font.getDefault().getFamily(), 20 ) );
		dontSaveAndExitBtn.setLineSpacing( 20 );
		//dontSaveAndExitBtn.setLayoutX( cancelBtn.getLayoutX() - 150 );
		//dontSaveAndExitBtn.setLayoutY( 70 );
		
		//设置按钮触发事件
		setBtnEvent(okBtn, cancelBtn, dontSaveAndExitBtn, pop);
		
		HBox hbox = new HBox();
		hbox.getChildren().add( dontSaveAndExitBtn );
		hbox.setMargin( dontSaveAndExitBtn, new Insets( 0, 20, 0, 0 ) );
		hbox.getChildren().add( cancelBtn );
		hbox.setMargin( cancelBtn, new Insets( 0, 20, 0, 0 ) );
		hbox.getChildren().add( okBtn );
		hbox.setMargin( okBtn, new Insets( 0, 0, 0, 0 ) );
		hbox.setLayoutX( 20 );
		hbox.setLayoutY( 70 );
		
		AnchorPane panel = new AnchorPane(  );
		panel.getChildren().add( tipText );
		panel.getChildren().add( hbox );
		
		return panel;
	}
	
	//给提示界面的 按钮 添加处理事件
	private void setBtnEvent( Button okBtn, Button cancelBtn,Button dontSaveAndExitBtn, Stage pop )
	{
		// TODO Auto-generated method stub
		dontSaveAndExitBtn.setOnAction( new EventHandler< ActionEvent >()
		{
			@Override
			public void handle( ActionEvent event )
			{
				//取消按钮被点击
				Trace.i( "用户点击了 不保存 并 退出 按钮" );
				Trace.i( "用户选择关闭程序..." );
				exit();
				
			}
		} );
		
		cancelBtn.setOnAction( new EventHandler< ActionEvent >()
		{
			@Override
			public void handle( ActionEvent event )
			{
				//取消按钮被点击
				Trace.i( "用户点击了取消按钮" );
				Trace.i( "用户取消了关闭程序事件" );
				
				pop.close();
			}
		} );
		
		okBtn.setOnAction( new EventHandler< ActionEvent >()
		{
			@Override
			public void handle( ActionEvent event )
			{
				//ok按钮被点击
				Trace.i( "用户点击了 ok 按钮" );
				
				if( m_contentLayerController.saveData() )
				{
					exit();
				}
				
			}
		} );
		
	}

	public void onRequestCloseApp()
	{
		//我们需要判断是否有未保存的数据、提示用户是否保存
		if( !GlobalData.getInstance().isSaveSataus() )
		{
			Trace.i( "存在未保存的数据!" );
			//弹出提示窗口
			showTipDialog();
		}
		else
		{
			exit();
		}
	}
	
	//退出程序
	public void exit()
	{
		Platform.exit();
	}
	
	public ContentLayerController getContentLayerController()
	{
		return m_contentLayerController;
	}
	
	public RootLayerController getRootLayerController()
	{
		return m_rootLayerController;
	}
	
	@Override
	public void onInit()
	{
		// TODO Auto-generated method stub
		// 网络数据操作、线程
	}

	@Override
	public void onStop()
	{
		// TODO Auto-generated method stub

	}

}
