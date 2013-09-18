package com.tju.malus.utility;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TableAdapter extends BaseAdapter {

	private Context context;
	private List<TableRow> table;
	
	public TableAdapter(Context context, List<TableRow> table)
	{
		this.context = context;
		this.table = table;
	}
	
	@Override
	public int getCount() {
		return table.size();
	}

	@Override
	public Object getItem(int position) {
		
		return table.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TableRow tableRow = table.get(position);
		return new TableRowView(this.context, tableRow);
	}
	
	
	
	
	
	


}
