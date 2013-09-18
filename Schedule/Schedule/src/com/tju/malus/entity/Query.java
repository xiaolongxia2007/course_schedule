package com.tju.malus.entity;

import java.io.Serializable;

public class Query implements Serializable {
	
	public static final int QUERY_TYPE_FULL = 1;  // full parameter query, with start index, end index, building code
	public static final int QUERY_TYPE_BUILDING_ONLY = 2; // only building code
	public static final int QUERY_TYPE_DAY_ONLY = 3; // only specified day of week
	public static final int QUERY_TYPE_CURRENT_AVAILABLE = 4; // current available
	public static final int QUERY_DAY_TODAY = 1;
	public static final int QUERY_DAY_TOMORROW = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int startIndex;
	private int endIndex;
	private String buildingCode;
	private String classroomName;
	private int type;
	private String message;
	private int day;
	
	public Query()
	{
		this.type = QUERY_TYPE_DAY_ONLY;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
