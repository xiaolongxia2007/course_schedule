package com.tju.malus;

import java.util.ArrayList;

import com.tju.malus.entity.Classroom;

public interface IQueryActivity {
	
	abstract void updateView(int msg, ArrayList<Classroom> classrooms);

}
