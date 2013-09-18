package com.tju.malus.utility;

import java.util.Date;
import org.joda.time.DateTime;

import android.graphics.Point;
import android.util.Log;

public class DateUtility {
	
	// shift start week of semester ahead 8 weeks, changed by year
	private final static int startWeek = 8;
	
	private final static String TAG = "DateUtility";
	
	/*
	 * Using Schedule of Tianjin University
	 */
	private final static Point[] points = { 
											new Point(8, 0),
											new Point(9, 35),
											new Point(11, 30),
											new Point(14, 0),
											new Point(15, 35),
											new Point(17, 30),
											new Point(19, 0),
											new Point(20, 35),
											new Point(22,30)
										  };
	
	public static int getWeekOfSemester()
	{
		DateTime now = new DateTime();
		
		Log.v(TAG, "week of the year is " + now.getWeekOfWeekyear());
		
		return now.getWeekOfWeekyear() - startWeek;
	}
	
	public static int getDayOfWeek()
	{
		DateTime now = new DateTime();
		
		Log.v(TAG, "day of week is " + now.getDayOfWeek());
		
		return now.getDayOfWeek();
	}
	
	// get lesson of day at current time
	public static int getLessonOfDay()
	{
		int result = -1;
		for(int i = 0; i < points.length - 1; i++)
		{
			if(between(points[i], points[i+1]))
			{
				result = i + 1;
				break;
			}
		}
		
		result = formatResult(result);
		
		Log.v(TAG, "lesson of day is " + result);
		
		return result;
	}
	
	private static int formatResult(int result)
	{
		switch(result)
		{
		case 1:
		case 2:
			return result;
		case 3:
			return 7;
		case 4:
		case 5:
			return result - 1;
		case 6:
			return 8;
		case 7:
		case 8:
			return result - 2;
		default:
			return 0;
		}
	}
	
	private static boolean between(Point start, Point end)
	{
		Date now = new Date();
		
		Date st = new Date(now.getYear(), now.getMonth(), now.getDate(), start.x, start.y);
		Date ed = new Date(now.getYear(), now.getMonth(), now.getDate(), end.x, end.y);
		
		if(now.compareTo(st) * now.compareTo(ed) == -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	

}
