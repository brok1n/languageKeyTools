package com.skywds.javafx.languagekeytools.view;

import com.skywds.javafx.languagekeytools.utils.Trace;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OkCancelDialogContoller
{
	@FXML
	private Label msgLabel;
	
	@FXML
	private Button	okBtn;
	
	@FXML
	private Button	cancelBtn;

	private OnDialogBtnClickListener	listener;
	
	@FXML
	public void handleOkBtn()
	{
		Trace.i( " 确定 按钮被点击" );
		if( listener != null )
		{
			listener.onOkBtnClickListener( this );
		}
	}
	
	@FXML
	public void handleCancelBtn()
	{
		Trace.i( "  取消按钮被点击" );
		if( listener != null )
		{
			listener.onCancelBtnClickListener( this );
		}
	}
	
	public void setTipMessage( String msg )
	{
		msgLabel.setText( "" + msg );
	}
	
	public void setOnDialogBtnClickListener( OnDialogBtnClickListener listener )
	{
		this.listener = listener;
	}
	
	public static abstract class OnDialogBtnClickListener
	{
		public abstract void onOkBtnClickListener( OkCancelDialogContoller control );
		
		public abstract void onCancelBtnClickListener( OkCancelDialogContoller control );
	}
}
