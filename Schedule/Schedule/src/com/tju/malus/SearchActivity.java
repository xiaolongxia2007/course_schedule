package com.tju.malus;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.tju.malus.entity.Query;
import com.tju.malus.utility.DateUtility;

public class SearchActivity extends Activity implements SensorEventListener {
	
	private Spinner spin_startIndex;
	private Spinner spin_endIndex;
	private Spinner spin_building;
	private Spinner spin_day;
	private Button btn_search;
	private Button btn_searchBuildingOnly;
	private boolean shaked = false;
	
	private SensorManager sensorManager = null;
	private Vibrator vibrator = null;
	
	private String[] buildingIds;
	
	public final static String QUERY_KEY = "query";
	
	private final String TAG = "SearchActivity"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.searchlayout);
		
		loadView();
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
	}
	
	private void loadView()
	{
		buildingIds = getResources().getStringArray(R.array.building_id);
		
		spin_startIndex = (Spinner)this.findViewById(R.id.spin_startIndex);
		ArrayAdapter<String> adapter = loadStringArray(R.array.class_selector);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_startIndex.setAdapter(adapter);
		spin_startIndex.setPrompt("开始节数");
		spin_startIndex.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ArrayAdapter<String> adapter = loadStringArray(R.array.class_selector);
				adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				for(int i = 0; i < arg2; i++)
				{
					adapter.remove(adapter.getItem(0));
				}
				spin_endIndex.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		// load view for end index spinner
		spin_endIndex = (Spinner)this.findViewById(R.id.spin_endIndex);
		spin_endIndex.setAdapter(adapter);
		spin_endIndex.setPrompt("结束节数");
		
		spin_day = (Spinner) this.findViewById(R.id.spin_day);
		ArrayAdapter<String> dayAdapter = loadStringArray(R.array.day_selector);
		dayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_day.setAdapter(dayAdapter);
		spin_day.setPrompt("选择哪天");
		
		// load view for building spinner
		ArrayAdapter<String> buildingAdapter = loadStringArray(R.array.building_selector);
		spin_building = (Spinner)this.findViewById(R.id.spin_building);
		buildingAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_building.setAdapter(buildingAdapter);
		spin_building.setPrompt("请选择教学楼");
		
		btn_search = (Button)this.findViewById(R.id.btn_search);
		
		btn_search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				submitQuery(Query.QUERY_TYPE_FULL);
			}
		});
		
		btn_searchBuildingOnly = (Button)this.findViewById(R.id.btn_searchBuildingOnly);
		
		btn_searchBuildingOnly.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				submitQuery(Query.QUERY_TYPE_BUILDING_ONLY);
				
			}
			
		});
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		shaked = false;
	}

	private void submitQuery(int type)
	{
		Query query = makeQuery(type);
		
		Intent searchResultIntent = new Intent(getApplicationContext(), SearchResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(QUERY_KEY, query);
		searchResultIntent.putExtras(bundle);
		
		startActivity(searchResultIntent);
		
		Log.v(TAG, "Submit query...");
	}
	
	private Query makeQuery(int type)
	{
		Query query = new Query();
		query.setType(type);
		if(type == Query.QUERY_TYPE_CURRENT_AVAILABLE)
		{
			int currentLesson = DateUtility.getLessonOfDay();
			if(currentLesson == 0)
			{
				query.setType(Query.QUERY_TYPE_DAY_ONLY);
				query.setMessage(this.getString(R.string.nightSearch));
			}
			else if(currentLesson == 7)
			{
				query.setStartIndex(3);
			}
			else if(currentLesson == 8)
			{
				query.setStartIndex(5);
			}
			else
			{
				query.setStartIndex(currentLesson);
			}
			query.setEndIndex(query.getStartIndex());
		}
		else
		{
			int startIndex = spin_startIndex.getSelectedItemPosition() + 1;
			int endIndex = spin_endIndex.getSelectedItemPosition() + startIndex;
			String buildingCode = buildingIds[spin_building.getSelectedItemPosition()];
			
			query.setStartIndex(startIndex);
			query.setEndIndex(endIndex);
			query.setBuildingCode(buildingCode);
		}
		
		int dayIndex = spin_day.getSelectedItemPosition();
		query.setDay(dayIndex);
		
		return query;
	}
	
	
	private ArrayAdapter<String> loadStringArray(int resId)
	{
		ArrayList<String> list = new ArrayList<String>();
		ArrayAdapter<String> adapter;
		String[] stringArray;
		
		stringArray = getResources().getStringArray(resId);
		for(int i = 0; i < stringArray.length; i++)
		{
			list.add(stringArray[i]);
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		
		return adapter;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	/*
	 * bugs here
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		
		float[] values = event.values;
		
		if(sensorType == Sensor.TYPE_ACCELEROMETER)
		{
			if(!shaked && (Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math.abs(values[2]) > 17))
			{
				shaked = true;
				submitQuery(Query.QUERY_TYPE_CURRENT_AVAILABLE);
				vibrator.vibrate(100);
			}
		}
		
	}	
}
