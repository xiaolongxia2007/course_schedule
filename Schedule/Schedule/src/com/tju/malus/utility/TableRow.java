package com.tju.malus.utility;

import java.util.ArrayList;

public class TableRow
{
	private int backgroundColor;
	private int stressColor;
	
	private ArrayList<TableCell> cell = new ArrayList<TableCell>();
	
	public TableRow(TableCell[] cell)
	{
		for(int i = 0; i < cell.length; i++)
		{
			this.cell.add(cell[i]);
		}
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getStressColor() {
		return stressColor;
	}

	public void setStressColor(int stressColor) {
		this.stressColor = stressColor;
	}

	public int getSize()
	{
		return cell.size();
	}
	
	public void addCell(TableCell cell)
	{
		this.cell.add(cell);
	}
	
	public TableCell getCellValue(int index)
	{
		if(index >= cell.size())
		{
			return null;
		}
		return cell.get(index);
	}
}