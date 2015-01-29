package com.skywds.javafx.languagekeytools.model;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skywds.javafx.languagekeytools.utils.Trace;

public class JsonData 
{
	//读取文件缓冲大小
	public static final int READ_DATA_CACHE_SIZE = 4096;
	
	//编码
	public static final String ENCODING = "UTF-8";
	
	//文件类型过滤器
	private FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("conf file (*.conf)", "*.conf");  
	
	public JSONObject parseToJson( String data ) throws JSONException
	{
		Trace.i("开始解析数据...");
		System.out.println("data:" + data.trim());
		if( data == null || data.length() < 2)
		{
			Trace.e("数据解析失败...");
			return null;
		}
		return new JSONObject(data.trim());
	}
	
	//添加数据到  预保存 数据中
	public boolean addDataToJson( String key, String chvalue, String envalue ) 
	{
		JSONArray keyArr = GlobalData.getInstance().getJsonKeyListArr();
		keyArr.put(key);
		
		JSONObject valueObj = GlobalData.getInstance().getJsonValueObj();
		JSONObject tmpObj = getDataObj( chvalue, envalue );
		try {
			valueObj.put( key, tmpObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("数据添加失败!");
			return false;
		}
		return true;
	}

	//创建并返回一个填好数据的一条数据json对象
	public JSONObject getDataObj( String chvalue, String envalue )
	{
		JSONObject tmpObj = new JSONObject();
		try {
			tmpObj.put( GlobalData.JSON_DATA_CH_KEY, chvalue);
			tmpObj.put( GlobalData.JSON_DATA_EN_KEY, envalue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpObj;
	}
	
	//检测是否有重复数据  true存在重复数据  false不存在重复数据
	public boolean checkRepetiton( String key )
	{
		ObservableList< LocalStringData > list  = GlobalData.getInstance().getTableData();
		int size = list.size();
		for( int x = 0; x < size; x ++ )
		{
			//取得一条数据
			LocalStringData data = list.get( x );
			if( data.getKey().equals( key ) )
			{
				return true;
			}
			
		}
		return false;
	}
	
	//将JsonData保存到文件中
	public boolean saveDataToFile()
	{
		File dataFile = new File( GlobalData.getInstance().getFilePath() );
		if( !dataFile.getParentFile().exists() || !dataFile.exists() )
		{
			dataFile.getParentFile().mkdirs();
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Trace.e("文件创建失败!");
				return false;
			}
		}
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dataFile), ENCODING )  );
			
			JSONObject tmpObj = new JSONObject();
			
			tmpObj.put( GlobalData.JSON_DATA_DATALIST_KEY, GlobalData.getInstance().getJsonValueObj());
			tmpObj.put(GlobalData.JSON_DATA_KEYLIST_KEY, GlobalData.getInstance().getJsonKeyListArr());
			
			String dataStr = tmpObj.toString();
			
			out.write(dataStr);
			
			out.flush();
			
			return true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("保存文件失败，文件不存在!");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("保存文件失败， 编码错误!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("保存文件失败， 读写错误!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("保存文件失败, 数据组装错误!");
		}
		finally
		{
			try {
				
				if( out != null )
				{
					out.flush();
					out.close();
				}
				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return false;
	}
	
	//读取文件的数据、初始化JsonData
	public boolean readDataFromFile()
	{
		File dataFile = new File( GlobalData.getInstance().getFilePath() );
		if( dataFile == null || !dataFile.exists() || !dataFile.isFile() )
		{
			Trace.e("读取配置文件失败...");
			return false;
		}
		
		BufferedInputStream inp = null;
		try {

			inp = new BufferedInputStream( new FileInputStream(dataFile) );
			int contenLen = inp.available();
			byte[] byteCache = new byte[contenLen];
//			String contentStr = "";
//			while ( (inp.read(byteCache, 0, READ_DATA_CACHE_SIZE) ) > 0) 
//			{
//				contentStr += new String( byteCache,  ENCODING);
//			}
			inp.read( byteCache );
			
			//加密解密处理
			byteCache = byteCache;
			
			String contentStr = new String( byteCache,  ENCODING );
			
			if( contentStr.length() < 1 )
			{
				Trace.e("配置文件无内容!");
				return false;
			}
			
			JSONObject dataObj = parseToJson( contentStr );
			if( dataObj == null )
			{
				Trace.e("配置文件解析失败!");
				return false;
			}
			
			//初始化Json数据
			JSONArray jsonArr = dataObj.optJSONArray( GlobalData.JSON_DATA_KEYLIST_KEY );
			JSONObject jsonObj = dataObj.optJSONObject( GlobalData.JSON_DATA_DATALIST_KEY );
			GlobalData.getInstance().setJsonKeyListArr(jsonArr);
			GlobalData.getInstance().setJsonValueObj(jsonObj);
			
			//初始化界面数据
			int size = jsonArr.length();
			for( int x = 0 ;x < size; x ++ )
			{
				String key	= jsonArr.getString( x );
				JSONObject tmpDataObj = jsonObj.getJSONObject( key );
				String ch = tmpDataObj.optString( GlobalData.JSON_DATA_CH_KEY );
				String en = tmpDataObj.optString( GlobalData.JSON_DATA_EN_KEY );
				
				LocalStringData tmpData = new LocalStringData( key, ch, en );
				GlobalData.getInstance().getTableData().add( tmpData );
				Trace.i( "" + tmpData.toString() );
				
			}
			
			Trace.i("配置文件读取完成...");
			
			return true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("读取配置文件失败！ 文件不存在！ ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("读取配置文件失败，文件读取错误!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Trace.e("读取配置文件失败，数据解析失败!");
		}
		finally
		{
			try {
				
				if( inp != null )
				{
					inp.close();
				}
				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return false;
		
	}
	
	
	
	
	public FileChooser.ExtensionFilter getExtFilter()
	{
		return extFilter;
	}

	private static JsonData instance;
	private JsonData(){};
	public static JsonData getInstance()
	{
		if( instance == null ) 
		{
			instance = new JsonData();	
		}
		return instance;
	}
}
