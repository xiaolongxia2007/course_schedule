package com.tju.malus;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tju.malus.entity.Classroom;
import com.tju.malus.entity.Query;
import com.tju.malus.utility.TableAdapter;
import com.tju.malus.utility.TableRow;
import com.tju.malus.QueryResultHandler;

public class SearchResultActivity extends Activity implements IQueryActivity {
	private Query query;
	
	private ArrayList<Classroom> classrooms;
	
	private QueryResultHandler handler;
	
	private final String TAG = "SearchResultActivity";
	
	private FrameLayout mainFrame;
	
	private ListView resultList;
	private TextView textView;
	private TextView searchingText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresultlayout);
		
		resultList = (ListView)this.findViewById(R.id.searchResultListView);
		textView = (TextView)this.findViewById(R.id.searchResultTextView);
		searchingText = (TextView)this.findViewById(R.id.current_action);
		mainFrame = (FrameLayout) this.findViewById(R.id.mainFrameLayout);
		
		query = (Query)getIntent().getSerializableExtra(SearchActivity.QUERY_KEY);
		
		handler = new QueryResultHandler(this);
		
		if(query.getType() == Query.QUERY_TYPE_CURRENT_AVAILABLE)
		{
			searchingText.setText(query.getMessage());
		}
		
		processQuery(query);
	}
	
	private void processQuery(Query query)
	{
		
		new Thread(new QueryThread(handler, query)).start();
	}
	

	private void updateViewWithResults()
	{
		
		ArrayList<TableRow> resultTable = ResultDisplay.makeTableAdapter(classrooms, this.getWindowManager().getDefaultDisplay().getWidth(), query);
		
		TableAdapter tableAdapter = new TableAdapter(this, resultTable);
        resultList.setAdapter(tableAdapter);
        resultList.setOnItemClickListener(new ItemClickEvent());
        
        removeLoadingView();
	}

	
	private void updateViewWithNoResults()
	{
		removeLoadingView();
		textView.setText("未找到相关结果");
	}
	
	private void removeLoadingView()
	{
		mainFrame.removeView(mainFrame.findViewById(R.id.searchResultLoading));
	}
	
	private void updateViewWithError()
	{
		
	}
	
    class ItemClickEvent implements OnItemClickListener {

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    	}
    }

	@Override
	public void updateView(int msg, ArrayList<Classroom> classrooms) 
	{
		Log.v(TAG, "updateView has been called");
		if(msg == QueryResultHandler.QUERY_SUCCESS)
		{
			this.classrooms = classrooms;
			updateViewWithResults();
		}
		else if(msg == QueryResultHandler.QUERY_NO_RESULT)
		{
			updateViewWithNoResults();
		}
		else
		{
			updateViewWithError();
		}
		
	}
}
