package com.tju.malus.utility;

public class TableCell
{
	public static final int STRING = 0;
	public static final int IMAGE = 1;
	
	private Object value;
	private int width;
	private int height;
	private int type;
	private int backgroundColor;
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public TableCell(Object value, int width, int height, int type)
	{
		this.value = value;
		this.width = width;
		this.height = height;
		this.type = type;
	}
	
	public TableCell(Object value, int width, int height, int type, int backgroundColor)
	{
		this.value = value;
		this.width = width;
		this.height = height;
		this.type = type;
		this.backgroundColor = backgroundColor;
	}
}