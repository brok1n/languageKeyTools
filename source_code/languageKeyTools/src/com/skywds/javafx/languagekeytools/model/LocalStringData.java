package com.skywds.javafx.languagekeytools.model;

import javafx.beans.property.SimpleStringProperty;

public class LocalStringData
{
	
	private SimpleStringProperty key;
	private SimpleStringProperty ch;
	private SimpleStringProperty en;
	public LocalStringData( String key, String ch, String en )
	{
		super();
		this.key = new SimpleStringProperty(key);
		this.ch = new SimpleStringProperty(ch);
		this.en = new SimpleStringProperty( en );
	}
	public SimpleStringProperty getKeyProperty()
	{
		return key;
	}
	public void setKey( String key )
	{
		this.key = new SimpleStringProperty( key );
	}
	public SimpleStringProperty getChpRroperty()
	{
		return ch;
	}
	public void setCh( String ch )
	{
		this.ch = new SimpleStringProperty(ch);
	}
	public SimpleStringProperty getEnProperty()
	{
		return en;
	}
	public void setEn( String en )
	{
		this.en = new SimpleStringProperty( en );
	}

	public String getKey()
	{
		return key.get();
	}
	public void setKey( SimpleStringProperty key )
	{
		this.key = key;
	}
	public String getCh()
	{
		return ch.get();
	}
	public void setCh( SimpleStringProperty ch )
	{
		this.ch = ch;
	}
	public String getEn()
	{
		return en.get();
	}
	public void setEn( SimpleStringProperty en )
	{
		this.en = en;
	}
	@Override
	public String toString()
	{
		return "find data [key=" + key.get() + ", ch=" + ch.get() + ", en=" + en.get() + "]";
	}

	
}
