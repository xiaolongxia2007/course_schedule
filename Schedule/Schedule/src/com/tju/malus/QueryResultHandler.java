package com.tju.malus;

import java.util.ArrayList;

import com.tju.malus.entity.Classroom;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class QueryResultHandler extends Handler {
	
	public final static int QUERY_SUCCESS = 0x01;
	
	public final static int QUERY_ERROR = 0x02;
	
	public final static int QUERY_NO_RESULT = 0x03;
	
	private final String TAG = "QueryResultHanlder";
	
	private IQueryActivity queryActivity;
	

	public IQueryActivity getQueryActivity() {
		return queryActivity;
	}


	public void setQueryActivity(IQueryActivity queryActivity) {
		this.queryActivity = queryActivity;
	}
	
	public QueryResultHandler(IQueryActivity queryActivity)
	{
		this.queryActivity = queryActivity;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		
		switch(msg.what)
		{
		case QUERY_SUCCESS:
			ArrayList<Classroom> classrooms = (ArrayList<Classroom>) msg.getData().getSerializable("Result");
			Log.v(TAG, "Result length " + String.valueOf(classrooms.size()));
			Log.v(TAG, "success");
			queryActivity.updateView(QUERY_SUCCESS, classrooms);
			break;
		case QUERY_NO_RESULT:
			Log.v(TAG, "no result");
			queryActivity.updateView(QUERY_NO_RESULT, null);
			 break;
		case QUERY_ERROR:
			Log.v(TAG, "error");
			queryActivity.updateView(QUERY_ERROR, null);
			break;
		}
		
		super.handleMessage(msg);
	}

}
