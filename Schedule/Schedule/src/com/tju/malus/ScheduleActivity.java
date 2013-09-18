package com.tju.malus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;


public class ScheduleActivity extends TabActivity {
	
    private TabHost tabHost;
    private RadioGroup tabGroup;
    
    private final String SEARCH_TAB_ID = "tab_search";
    private final String SEARCH_TAB_INDICATOR = "搜索";
    
    //private final String AVAILABLE_CLASSROOMS_TAB_ID = "tab_available_classrooms";
    //private final String AVAILABLE_CLASSROOMS_TAB_INDICATOR = "可用自习室";
    
    private final String ABOUT_TAB_ID = "tab_about";
    private final String ABOUT_TAB_INDICATOR = "关于";
    
    private final String TAG = "ScheduleActivity";
    
    private final String PREFS_NAME = "com.tju.malus";
    private final String FIRST_RUN = "first_time_run";
    private SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tabHost = getTabHost();
        tabGroup = (RadioGroup)this.findViewById(R.id.tab_group);
        
        Intent searchTab = new Intent(this, SearchActivity.class);
        //Intent classroomsTab = new Intent(this, AvailableClassroomsActivity.class);
        Intent aboutTab = new Intent(this, AboutActivity.class);
        
        tabHost.addTab(tabHost.newTabSpec(SEARCH_TAB_ID).setIndicator(SEARCH_TAB_INDICATOR).setContent(searchTab));
        Log.v(TAG, "Search Tab added");
    	//tabHost.addTab(tabHost.newTabSpec(AVAILABLE_CLASSROOMS_TAB_ID).setIndicator(AVAILABLE_CLASSROOMS_TAB_INDICATOR).setContent(classroomsTab));
    	tabHost.addTab(tabHost.newTabSpec(ABOUT_TAB_ID).setIndicator(ABOUT_TAB_INDICATOR).setContent(aboutTab));
    	
    	tabHost.setCurrentTabByTag(SEARCH_TAB_ID);
    	
    	tabGroup.setOnCheckedChangeListener(new OnTabChangedListener());
    	
    	if(firstTimeRun())
    	{
    		try
    		{
    			copyDatabase();
    		}
    		catch(Exception ex)
    		{
    			Log.e(TAG, ex.getMessage());
    		}
    	}
    }
    
    private class OnTabChangedListener implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId)
			{
			case R.id.search:
				tabHost.setCurrentTabByTag(SEARCH_TAB_ID);
				break;
			//case R.id.avaiable_classrooms:
				//tabHost.setCurrentTabByTag(AVAILABLE_CLASSROOMS_TAB_ID);
				//break;
			case R.id.about:
				tabHost.setCurrentTabByTag(ABOUT_TAB_ID);
				break;
			}
			
		}
    	
    }
    
    private boolean firstTimeRun()
    {
    	settings = getSharedPreferences(PREFS_NAME, 0);
    	boolean result = settings.getBoolean(FIRST_RUN, true);
    	if(result)
    	{
    		Editor prefEditor = settings.edit();
    		prefEditor.putBoolean(FIRST_RUN, false);
    		Log.v(TAG, "First time run");
    	}
    	return result;
    }
 
    private void copyDatabase() throws IOException
    {
    	InputStream assetsDB = getApplicationContext().getAssets().open("ScheduleDB");
    	OutputStream dbOut = new FileOutputStream("/data/data/com.tju.malus/databases/ScheduleDB");
    	
    	byte[] buffer = new byte[1024];
    	
    	int length;
    	while((length = assetsDB.read(buffer)) > 0)
    	{
    		dbOut.write(buffer, 0, length);
    	}
    	
    	dbOut.flush();
    	dbOut.close();
    	assetsDB.close();
    }
}