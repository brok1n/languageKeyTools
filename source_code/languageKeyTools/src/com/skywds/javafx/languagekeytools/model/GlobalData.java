package com.skywds.javafx.languagekeytools.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skywds.javafx.languagekeytools.utils.Trace;


public class GlobalData
{
	//数据key的key
	public static final String JSON_DATA_KEYLIST_KEY = "key";
	
	//数据value的key
	public static final String JSON_DATA_DATALIST_KEY = "data";
	
	//中文key
	public static final String JSON_DATA_CH_KEY = "chinese";
	
	//英文key
	public static final String JSON_DATA_EN_KEY = "english";
	
	//添加完是否清除输入框
	private boolean autoCleanInputField = false;
	
	//表数据
	private ObservableList< LocalStringData > tableData = FXCollections.observableArrayList();
	
	//log数据
	private ObservableList< String > logData	= FXCollections.observableArrayList();
	
	//Key List array
	private JSONArray jsonKeyListArr = new JSONArray();
	
	//value Object
	private JSONObject jsonValueObj = new JSONObject();
	
	//保存文件的路径
	private String filePath;
	
	//是否有未存储的数据
	private boolean saveSataus = false;
	
	
	
	//刷新 预保存 数据
	//初始化将要保存的数据
	public void flushPreSaveData()
	{
		//清空已经存在的 预保存 数据
		GlobalData.getInstance().clearJsonData();
		
		ObservableList< LocalStringData > list  = GlobalData.getInstance().getTableData();
		int size = list.size();
		System.out.println(" flush data : " + size );
		for( int x = 0; x < size; x ++ )
		{
			//取得一条数据
			LocalStringData data = list.get( x );
			
			//添加到  预保存 数据
			boolean addStatus = JsonData.getInstance().addDataToJson( data.getKey(), data.getCh(), data.getEn() );
			if( !addStatus )
			{
				Trace.i( "添加 预保存 数据失败!" );
			}
		}
	}
	
	
	//清除存在的数据
	public void clearJsonData()
	{
		jsonKeyListArr 	= new JSONArray();
		jsonValueObj	= new JSONObject();
	}
	
	public boolean isSaveSataus()
	{
		return saveSataus;
	}
	public void setSaveSataus( boolean saveSataus )
	{
		this.saveSataus = saveSataus;
	}
	public JSONArray getJsonKeyListArr() {
		return jsonKeyListArr;
	}
	public void setJsonKeyListArr(JSONArray jsonKeyListArr) {
		this.jsonKeyListArr = jsonKeyListArr;
	}
	public JSONObject getJsonValueObj() {
		return jsonValueObj;
	}
	public void setJsonValueObj(JSONObject jsonValueObj) {
		this.jsonValueObj = jsonValueObj;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public ObservableList< String > getLogData()
	{
		return logData;
	}
	public void setLogData( ObservableList< String > logData )
	{
		this.logData = logData;
	}
	public ObservableList< LocalStringData > getTableData()
	{
		return tableData;
	}
	public void setTableData( ObservableList< LocalStringData > tableData )
	{
		this.tableData = tableData;
	}
	public boolean isAutoCleanInputField()
	{
		return autoCleanInputField;
	}
	public void setAutoCleanInputField( boolean autoCleanInputField )
	{
		this.autoCleanInputField = autoCleanInputField;
	}
	
	private static GlobalData instance;
	private GlobalData()
	{
		
	};
	public static GlobalData getInstance()
	{
		if( instance == null )
		{
			instance = new GlobalData();
		}
		return instance;
	}
}
