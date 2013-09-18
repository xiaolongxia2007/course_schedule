package com.tju.malus;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tju.malus.db.DBUtility;
import com.tju.malus.entity.Classroom;
import com.tju.malus.entity.Query;
import com.tju.malus.utility.DateUtility;

public class QueryThread implements Runnable {
	
	private Handler handler;
	
	private DBUtility dbUtility;
	
	private Query query;
	
	public QueryThread(Handler handler, Query query)
	{
		this.handler = handler;
		this.query = query;
		dbUtility = new DBUtility();
	}

	@Override
	public void run() {
		
		int dayOfWeek = DateUtility.getDayOfWeek();
		int weekOfSemester = DateUtility.getWeekOfSemester();
		
		ArrayList<Classroom> classrooms = null ;
		
		dayOfWeek = (dayOfWeek + query.getDay()) % 8;
		if(dayOfWeek == 0)
		{
			dayOfWeek++;
			weekOfSemester++;
		}
		
		switch(query.getType())
		{
			case Query.QUERY_TYPE_FULL:
				classrooms = (ArrayList<Classroom>) dbUtility.QueryByBuildingCode(query.getBuildingCode(), query.getStartIndex(), query.getEndIndex(), weekOfSemester, dayOfWeek);
				break;
			case Query.QUERY_TYPE_BUILDING_ONLY:
				classrooms = (ArrayList<Classroom>) dbUtility.QueryByBuildingCode(query.getBuildingCode(), weekOfSemester);
				break;
			case Query.QUERY_TYPE_DAY_ONLY:
				classrooms = (ArrayList<Classroom>) dbUtility.QueryAllClassrooms(weekOfSemester, dayOfWeek);
				break;
			case Query.QUERY_TYPE_CURRENT_AVAILABLE:
				classrooms = (ArrayList<Classroom>) dbUtility.QueryAvailableClassrooms(query.getStartIndex(), query.getEndIndex(), weekOfSemester, dayOfWeek);
				break;
			default:
				break;
		}
		
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Result", classrooms);
		msg.setData(bundle);
		
		if(classrooms != null)
		{
			msg.what = QueryResultHandler.QUERY_SUCCESS;
		}
		else
		{
			msg.what = QueryResultHandler.QUERY_NO_RESULT;
		}
		handler.sendMessage(msg);
	}

}
