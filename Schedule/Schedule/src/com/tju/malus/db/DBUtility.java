package com.tju.malus.db;

import java.util.ArrayList;
import java.util.List;

import com.tju.malus.entity.Classroom;

import android.database.Cursor;
import android.util.Log;


/**
 * @author Administrator
 *
 */
public class DBUtility {
	
/* 
 * Database table like this 
 * classroom_location : this is building code
 * ===================================================================================================================================================
 *  classroom_id || classroom_location || classroom_name || schedule_week || monday || tuesday || wednesday || thursday || friday || saturday || sunday
 *  ====================================================================================================================================================
 *  453 || 22 || 4¥102 || 1 || 60 || 60 || 28 || 52 || 60 || 51 || 63
 * 
 */
	/*
	 *  Database column fields
	 */
	public final static String DATABASE_NAME = "ScheduleDB";
	public final static String TABLE_NAME = "classrooms";
	public final static String COLUMN_ID = "classroom_id";
	public final static String COLUMN_NAME = "classroom_name";
	public final static String COLUMN_LOCATION = "classroom_location";
	public final static String COLUMN_WEEK = "schedule_week";
	public final static String[] COLUMN_DAYS = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
	private static final String DATABASE_FILENAME = "data/data/com.tju.malus/databases/ScheduleDB";
	
	
	private SQLiteUtil sqlUtil;
	
	private String TAG = "DBUtility";
	
	public DBUtility()
	{
		sqlUtil = SQLiteUtil.getInstance();
	}
	/*
	 * basic query function
	 */
	private List<Classroom> Query(String sql, String[] args)
	{
		try
		{
			Cursor cursor = sqlUtil.openQuery(DATABASE_FILENAME, sql);
			
			List<Classroom> classrooms = new ArrayList<Classroom>();
			if(cursor.moveToFirst())
			{
				do
				{
					Classroom classroom = new Classroom();
					classroom.setClassroomID(cursor.getInt(0));
					classroom.setBuildingCode(cursor.getString(1));
					classroom.setClassroomName(cursor.getString(2));
					classroom.setWeek(cursor.getInt(3));
	
					byte[] schedule = new byte[7];
					for(int j = 0; j < 7; j++)
					{
						schedule[j] = (byte)cursor.getShort(4 + j);
					}
					classroom.setSchedule(schedule);
					
					classrooms.add(classroom);
				}while(cursor.moveToNext());
			}
			
			sqlUtil.closeQuery(cursor);
			
			return classrooms;
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}
	
	/*
	 * find classroom by classroom id, used for debug only
	 */
	public Classroom Query(int classroomID)
	{
		try
		{			
			String querySql = "select * from " + TABLE_NAME + " "
							  + "where " + COLUMN_ID + " = " + String.valueOf(classroomID);
			
			List<Classroom> classrooms = Query(querySql, null);
			
			if(classrooms.size() > 0)
			{
				return classrooms.get(0);
			}
			else
			{
				return null;
			}
			
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}
	
	/*
	 * find all classrooms in specific building
	 */
	public List<Classroom> QueryByBuildingCode(String buildingCode, int week)
	{
		try
		{			
			String querySql = "select * from " + TABLE_NAME + " "
					          + "where " + COLUMN_LOCATION 
					          + " =\"" + buildingCode + "\"  and " 
					          + COLUMN_WEEK + " = " + String.valueOf(week);
			Log.v(TAG, querySql + " " + buildingCode + " " + String.valueOf(week));
			
			List<Classroom> classrooms = Query(querySql, null);
			
			return classrooms;
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}
	
	/*
	 * find all available classrooms in specific building
	 */
	public List<Classroom> QueryByBuildingCode(String buildingCode, int start, int end, int week, int day)
	{
		try
		{
			byte[] values = scheduleValue(start, end);
			
			String querySql = "select * from " + TABLE_NAME
							  + " where " + COLUMN_LOCATION + " = \"" + buildingCode
							  + "\" and " + COLUMN_WEEK + " = " + week + " and(";
			
			for(int i = 0; i < values.length - 1; i++)
			{
				querySql +=  COLUMN_DAYS[day - 1] + " = \"" + String.valueOf(values[i]) + "\" or ";
			}
			querySql +=  COLUMN_DAYS[day - 1] + " = \"" + String.valueOf(values[values.length - 1]) + "\")";
			
			Log.v(TAG, querySql);
			
			List<Classroom> classrooms = Query(querySql, null);
			
			return classrooms;
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}
	
	/*
	 * find all available classrooms 
	 */
	public List<Classroom> QueryAvailableClassrooms(int start, int end, int week, int day)
	{
		try
		{
			byte[] values = scheduleValue(start, end);
					
			String querySql = "select * from " + TABLE_NAME + " where "
							 + COLUMN_WEEK + " = " + week 
							 + " and (";
			
			for(int i = 0; i < values.length - 1; i++)
			{
				querySql +=  COLUMN_DAYS[day - 1] + " = \"" + String.valueOf(values[i]) + "\" or ";
			}
			querySql +=  COLUMN_DAYS[day - 1] + " = \"" + String.valueOf(values[values.length - 1]) + "\")";
			
			Log.v(TAG, querySql);
			
			List<Classroom> classrooms = Query(querySql, null);
			
			return classrooms;
							  
		}
		catch(Exception ex)
		{
			//Log.e(TAG, ex.getMessage());
			return null;
		}
		
	}
	
	/*
	 * find specific classroom schedule
	 */
	public List<Classroom> QueryByClassroomName(String buildingCode, String classroomName, int week, int day)
	{
		
		try
		{
			String querySql = "select * from " + TABLE_NAME 
							  + " where " + COLUMN_LOCATION + " = \"" + buildingCode + "\""
							  + " and " + COLUMN_WEEK + " = " + week + " and "
							  + COLUMN_NAME + " like '%" + classroomName + "%'";
			
			Log.v(TAG, querySql);
			
			List<Classroom> classrooms = Query(querySql, null);
			
			return classrooms;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Classroom> QueryAllClassrooms(int week, int day)
	{
		try
		{
			String querySql = "select * from " + TABLE_NAME 
							  + " where " + COLUMN_WEEK + " = " + week
							  + " and " + COLUMN_DAYS[day - 1] + " != 0";
			
			Log.v(TAG, querySql);
			
			List<Classroom> classrooms = Query(querySql, null);
			
			return classrooms;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	/*
	 * calculate all values match start and end
	 */
	// Get all possible combinations with start and end lesson
	private byte[] scheduleValue(int start, int end)
	{
		if(start >= 1 && start <= end && end <= 6)
		{
			int count = (int)Math.pow(2, 5 - end + start);
			
			byte[] result = new byte[count];
			
			// Generate mask
			String smask = "";
			for(int i = 1; i < 7; i++)
			{
				if( i >= start && i <= end)
				{
					smask += "1";
				}
				else
				{
					smask += "0";
				}
			}
			
			int mask = Integer.valueOf(smask, 2);
			
			int index = 0;
			for( int i = 0; i < 64; i++ )
			{
				if( mask == (i & mask))
				{
					result[index] = (byte) i;
					index++;
				}
			}
			
			
			return result;
		}
		else
		{
			return null;
		}
	}

}
