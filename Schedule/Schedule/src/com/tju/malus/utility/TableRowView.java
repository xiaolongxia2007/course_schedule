package com.tju.malus.utility;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableRowView extends LinearLayout
{
	public TableRowView(Context context) {
		super(context);
	}
	
	public TableRowView(Context context, TableRow tableRow)
	{
		super(context);
		
		this.setOrientation(LinearLayout.HORIZONTAL);
		for(int i = 0; i < tableRow.getSize(); i++)
		{
			TableCell tableCell = tableRow.getCellValue(i);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.getWidth(), tableCell.getHeight());
			layoutParams.setMargins(0, 0, 1, 1);
			if(tableCell.getType() == TableCell.STRING)
			{
				TextView textCell = new TextView(context);
				textCell.setLines(1);
				textCell.setGravity(Gravity.CENTER);
				textCell.setBackgroundColor(tableCell.getBackgroundColor());
				textCell.setText(String.valueOf(tableCell.getValue()));
				
				addView(textCell, layoutParams);
			}
			else if(tableCell.getType() == TableCell.IMAGE)
			{
				ImageView imgCell = new ImageView(context);
				imgCell.setBackgroundColor(tableCell.getBackgroundColor());
				imgCell.setImageResource((Integer)tableCell.getValue());
				addView(imgCell, layoutParams);
			}
		}
		this.setBackgroundColor(0xAAFFFFFF);
	}
	
}