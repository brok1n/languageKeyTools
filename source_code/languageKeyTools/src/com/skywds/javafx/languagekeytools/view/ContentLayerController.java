package com.skywds.javafx.languagekeytools.view;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import com.skywds.javafx.languagekeytools.AppDelegate;
import com.skywds.javafx.languagekeytools.model.GlobalData;
import com.skywds.javafx.languagekeytools.model.JsonData;
import com.skywds.javafx.languagekeytools.model.LocalStringData;
import com.skywds.javafx.languagekeytools.utils.Trace;

public class ContentLayerController
{

	@FXML
	private TextField 	inputKey;
	@FXML
	private TextField 	inputChinese;
	@FXML
	private TextField 	inputEnglish;
	
	@FXML
	private Button		addBtn;
	@FXML
	private Button		deleteBtn;
	@FXML
	private Button		saveBtn;
	@FXML
	private Button		clearLogBtn;
	
	@FXML
	private TableColumn< LocalStringData, String >	keyColumn;
	@FXML
	private TableColumn< LocalStringData, String >	chColumn;
	@FXML
	private TableColumn< LocalStringData, String > 	enColumn;
	@FXML
	private TableView< LocalStringData >			tableId;
	
	@FXML
	private ListView< String >		listId;
	private static ListView< String >		logListView;
	
	private String inputKeyStr;
	private String inputChStr;
	private String inputEnStr;
	
	private AppDelegate app;
	
	@FXML
	private void handleAddBtn()
	{
		System.out.println(" handleAddBtn ");
		Trace.i( "添加按钮被触发" );
		//读取数据
		readInputField();
		
		//检测数据
		if( !checkInputStr() )
		{
			System.out.println("输入错误");
			return ;
		}
		
		Trace.i( "输入正确、开始添加数据..." );
		
		//添加一条数据
		addOneItem();
		
		//清除输入框
		autoCleanInputField();
	}
	

	//检测输入数据是否正确
	private boolean checkInputStr()
	{
		Trace.i( "开始检测输入数据..." );
		// TODO Auto-generated method stub
		if( inputKey == null || inputKeyStr.length() < 1 )
		{
			Trace.e( "key 输入错误  key长度必须  > 1" );
			return false;
		}
		else if( inputChStr == null || inputChStr.length() < 1 )
		{
			Trace.e( "中文 输入错误 中文必须 > 1" );
			return false;
		}
		
		return true;
	}

	//读取输入数据
	private void readInputField()
	{
		Trace.i( "开始获取输入数据" );
		inputKeyStr	= inputKey.getText().toString().trim();
		inputChStr	= inputChinese.getText().toString().trim();
		inputEnStr	= inputEnglish.getText().toString().trim();
		Trace.i( "输入数据获取完成" );
	}

	@FXML
	private void handleDeleteBtn()
	{
		System.out.println( "handleDeleteBtn" );
		Trace.i( "删除按钮被触发" );
		deleteSelectedDataItem();
	}
	
	//删除选中的数据
	private void deleteSelectedDataItem()
	{
		LocalStringData selData = tableId.getSelectionModel().getSelectedItem();
		System.out.println(" selData:" + selData.toString());
		//确定是否删除这条数据
		boolean confirmDeleteItem = confirmDeleteThisItem( selData );
//		if( confirmDeleteItem )
//		{
//			//用户确定了删除
//			GlobalData.getInstance().getLogData().remove( selData );
//			flushTableView();
//			Trace.i( "数据删除成功" );
//		}
		
		
	}
	
	
	private boolean confirmDeleteThisItem( LocalStringData data )
	{
		BorderPane panel = null;
		
		final Stage dialog = new Stage();
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation( getClass().getResource( "OkCancelDialog.fxml" ) );
			panel = loader.load();
			
			OkCancelDialogContoller dialogContoller = loader.getController();
			dialogContoller.setTipMessage( "将要删除: " + data.toString() );
			
			dialogContoller.setOnDialogBtnClickListener( new OkCancelDialogContoller.OnDialogBtnClickListener()
			{
				
				@Override
				public void onOkBtnClickListener( OkCancelDialogContoller control )
				{
					//用户确定了删除
					GlobalData.getInstance().getTableData().remove( data );
					flushTableView();
					Trace.i( "数据删除成功:" + data.toString() );
					dialog.close();
					
					//数据发生了改变
					GlobalData.getInstance().setSaveSataus( false );
				}
				
				@Override
				public void onCancelBtnClickListener( OkCancelDialogContoller control )
				{
					Trace.i( "用户取消了删除数据请求" );
					dialog.close();
				}
			} );
			
		}
		catch ( Exception e )
		{
			// TODO: handle exception
			Trace.i( "程序异常" );
			return false;
		}
		
		Scene scene = new Scene( panel );
		
		
		dialog.initStyle( StageStyle.UTILITY );
		dialog.setScene( scene );
		dialog.setTitle( "提示" );
		//禁止改变大小
		dialog.setResizable( false );
		//设置窗体为模态窗体
		dialog.initModality( Modality.APPLICATION_MODAL );
		dialog.show();
		
		return true;
	}


	@FXML
	private void handleSaveBtn()
	{
		System.out.println( "handleSaveBtn" );
		Trace.i( "保存按钮被触发" );
		saveData();
	}
	
	@FXML
	private void clearLogBtn()
	{
		System.out.println( "clearLogBtn" );
		Trace.i( "清除日志按钮被触发" );
		GlobalData.getInstance().getLogData().clear();
		Trace.i( "日志被清空..." );
	}
	
	//添加一条数据
	private void addOneItem()
	{
		//添加数据到数据集合
		GlobalData.getInstance().getTableData().add( new LocalStringData( inputKeyStr, inputChStr, inputEnStr ) );
		
		//刷新界面
		flushTableView();
		
		Trace.i( "添加了一条数据: key:" + inputKeyStr + "  ch:" + inputChStr + "  en:" + inputEnStr );
		
		//添加了新数据、新数据没有保存、保存状态就变为 未保存 状态
		GlobalData.getInstance().setSaveSataus( false );
		
	}
	
	//保存数据
	public boolean saveData()
	{
		//是否有 未保存 的数据
		if( GlobalData.getInstance().isSaveSataus() )
		{
			Trace.i("无新数据!");
			return true;
		}
		
		//更新预存储数据
		GlobalData.getInstance().flushPreSaveData();
		
		//保存到本地
		if( !saveDataToFile() )
		{
			GlobalData.getInstance().setSaveSataus( false );
			Trace.e(" 数据未保存! ");
			return false;
		}
		GlobalData.getInstance().setSaveSataus( true );

		Trace.i( "保存数据完成! " + GlobalData.getInstance().getFilePath() );
		
		return true;
	}
	
	//将数据保存到文件中
	private boolean saveDataToFile() 
	{
		//是否有文件路径。如果有、就直接保存、如果没有就弹出文件选择对话框
		if( GlobalData.getInstance().getFilePath() == null || GlobalData.getInstance().getFilePath().length() < 5 )
		{
			//没有选择文件、没有保存过
			if( !initSaveFilePath() )
			{
				Trace.e(" 用户未选择保存文件位置!  ");
				return false;
			}
			
			return JsonData.getInstance().saveDataToFile();
		}
		else
		{
			//已经选择好了保存路径、已经有保存路径了.直接保存
			return JsonData.getInstance().saveDataToFile();
		}
		
	}

	private boolean initSaveFilePath()
	{
		String path = null;
		//得到用户导出的文件路径  
		FileChooser fileChooser = new FileChooser();  
		fileChooser.getExtensionFilters().add( JsonData.getInstance().getExtFilter() );  
		fileChooser.setInitialDirectory( new File(".") );
		fileChooser.setTitle("保存");
		
		Stage s = new Stage();  
		File file = fileChooser.showSaveDialog(s);  
		
		if( file == null )
		{
			Trace.i(" 保存操作被取消！ ");
			return false;
		}
		
		path = file.getAbsolutePath();
		
		Trace.i(" 用户选择了保存位置: " + path);
		
		GlobalData.getInstance().setFilePath(path);
		
		return true;
		
	}

	//是否清除输入框
	private void autoCleanInputField()
	{
		//是否清除输入框
		if( GlobalData.getInstance().isAutoCleanInputField() )
		{
			inputKey.setText( "" );
			inputEnglish.setText( "" );
			inputChinese.setText( "" );
			Trace.i( "输入框被清空" );
		}
	}
	
	//刷新 tableView 的显示数据
	public void flushTableView()
	{
		tableId.setItems( GlobalData.getInstance().getTableData() );
		Trace.i( "TableView数据被刷新" );
	}
	
	//初始化
	public void init( AppDelegate app )
	{
		this.app = app;
		logListView = listId;
		
		//初始化Table Column factory
		keyColumn.setCellValueFactory( new PropertyValueFactory< LocalStringData, String >( "key" ) );
		chColumn.setCellValueFactory( new PropertyValueFactory< LocalStringData, String >( "ch" ) );
		enColumn.setCellValueFactory( new PropertyValueFactory< LocalStringData, String >( "en" ) );
		
		
		Callback<TableColumn<LocalStringData, String>, TableCell<LocalStringData, String>> cellFactory = new Callback<TableColumn<LocalStringData, String>, TableCell<LocalStringData, String>>() {
            @Override
            public TableCell<LocalStringData, String> call(TableColumn<LocalStringData, String> p) {
                return new EditingCell();
            }
        };
        
        chColumn.setCellFactory( cellFactory);
		enColumn.setCellFactory(cellFactory);
        
		chColumn.setOnEditCommit(new EventHandler<CellEditEvent< LocalStringData, String >>() {
            @Override
            public void handle(CellEditEvent< LocalStringData, String > t) {
                ((LocalStringData) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCh( t.getNewValue());
                GlobalData.getInstance().setSaveSataus( false );
            }
        });
		
		enColumn.setOnEditCommit(new EventHandler<CellEditEvent< LocalStringData, String >>() {
            @Override
            public void handle(CellEditEvent< LocalStringData, String > t) {
                ((LocalStringData) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEn( t.getNewValue());
                GlobalData.getInstance().setSaveSataus( false );
            }
        });
		
		//重要！！！ 否则TableView 无法编辑
		chColumn.setEditable( true );
		enColumn.setEditable( true );
		tableId.setEditable( true );
        
		
		//刷新数据到UI
		flushTableView();
		
		//
		listId.setItems( GlobalData.getInstance().getLogData() );
		
		//启动时。没有新数据 ，所以不用保存
		GlobalData.getInstance().setSaveSataus( true );
	}
	
	public static ListView< String > getLogListView()
	{
		return logListView;
	}
}
