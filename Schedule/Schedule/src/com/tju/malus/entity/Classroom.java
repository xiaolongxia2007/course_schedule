package com.tju.malus.entity;

import java.io.Serializable;

public class Classroom implements Serializable {

	private static final long serialVersionUID = 1L;

	private int classroomID;
	
	private String classroomName;
	
	private String buildingCode;
	
	private int week;
	
	private byte[] schedule = new byte[7];

	public int getClassroomID() {
		return classroomID;
	}

	public void setClassroomID(int classroomID) {
		this.classroomID = classroomID;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public byte[] getSchedule() {
		return schedule;
	}

	public void setSchedule(byte[] schedule) {
		this.schedule = schedule;
	}

}
