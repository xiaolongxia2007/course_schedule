package com.tju.malus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.joda.time.DateTime;

import android.graphics.Color;
import android.view.WindowManager.LayoutParams;

import com.tju.malus.entity.Classroom;
import com.tju.malus.entity.Query;
import com.tju.malus.utility.DateUtility;
import com.tju.malus.utility.TableCell;
import com.tju.malus.utility.TableRow;

/*
 * Ugly, need to rewrite someday
 */
public class ResultDisplay {
	
	private final static String[] DAYS = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};	
	private static int classroomNameWidth;	
	private static int sequenceWidth;
	private final static int oddColor = 0xAA000000;
	private final static int evenColor = 0xFF000000;
	private final static int stressColor = 0x88000000 ;
	
	public static ArrayList<TableRow> makeTableAdapter(ArrayList<Classroom> classrooms, int screenWidth, Query query)
	{
		classroomNameWidth = screenWidth / 4;
		
		sequenceWidth = classroomNameWidth / 2;
		
		ArrayList<TableRow> resultTable = new ArrayList<TableRow>();
		
		
		int dayOfWeek = DateUtility.getDayOfWeek();
		int weekOfSemester = DateUtility.getWeekOfSemester();
		
		dayOfWeek = (dayOfWeek + query.getDay()) % 8;
		if(dayOfWeek == 0)
		{
			dayOfWeek++;
			weekOfSemester++;
		}
		
		// First row of table 
		TableCell[] date = new TableCell[1];
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		DateTime today = new DateTime();
		today = today.plusDays(query.getDay());
		String mainTitle = sdf.format(today.toDate()) + " 第 " + weekOfSemester + " 周  " + DAYS[dayOfWeek - 1];
		date[0] = new TableCell(mainTitle, screenWidth, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		
		resultTable.add(new TableRow(date));
		
		// Second row of table
		int titleWidth = (screenWidth - classroomNameWidth - sequenceWidth) / 3;
		TableCell[] titles = new TableCell[5];
		titles[0] = new TableCell("序号", sequenceWidth, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		titles[1] = new TableCell("教室", classroomNameWidth, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		titles[2] = new TableCell("上午", titleWidth - 1, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		titles[3] = new TableCell("下午", titleWidth - 1, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		titles[4] = new TableCell("晚上", titleWidth - 1, LayoutParams.FILL_PARENT, TableCell.STRING, Color.BLACK);
		resultTable.add(new TableRow(titles));
		
		// body of table
		//int dayOfWeek = DateUtility.getDayOfWeek();
		int cellColor = oddColor;
		for(int i = 0; i < classrooms.size(); i++)
		{
			Classroom classroom = classrooms.get(i);
			
			if(i % 2 != 0)
			{
				cellColor = oddColor;
			}
			else
			{
				cellColor = evenColor;
			}
			
			TableCell[] classroomRow = new TableCell[8];
			classroomRow[0] = new TableCell(String.valueOf(i + 1), sequenceWidth, LayoutParams.FILL_PARENT, TableCell.STRING, cellColor);
			classroomRow[1] = new TableCell(classroom.getClassroomName(), classroomNameWidth, LayoutParams.FILL_PARENT, TableCell.STRING, cellColor);
			boolean[] schedule = split(classroom.getSchedule()[dayOfWeek - 1]);
			for(int j = 0; j < schedule.length; j++)
			{
				if(schedule[j])
				{
					classroomRow[j + 2] = new TableCell("无", titleWidth / 2 - 1, LayoutParams.FILL_PARENT, TableCell.STRING, stressColor);
				}
				else
				{
					classroomRow[j + 2] = new TableCell("有", titleWidth / 2 - 1, LayoutParams.FILL_PARENT, TableCell.STRING, cellColor);
				}
				
			}
			
			resultTable.add(new TableRow(classroomRow));
		}
		
		return resultTable;
	}
	
	private static boolean[] split(byte num)
	{
		boolean[] result = new boolean[6];
		String full = "000000";
		String str = Integer.toBinaryString(num);
		str = full.substring(0, 6 - str.length()) + str;
		
		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) == '0')
			{
				result[i] = false;
			}
			else
			{
				result[i] = true;
			}
		}
		
		return result;
	}

}
