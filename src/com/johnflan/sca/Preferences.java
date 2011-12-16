package com.johnflan.sca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.johnflan.sca.retriever.Retriever;
import com.johnflan.sca.retriever.database.DatabaseHelper;
import com.johnflan.sca.retriever.database.SourceRegion;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class Preferences extends Activity {
	private final static String TAG = "Preferences";
	private ListView regionsListView;
	private List<SourceRegion> sourceItems = new ArrayList<SourceRegion>();
	private DatabaseHelper dbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.preferences);
	    
	    regionsListView = (ListView)findViewById(R.id.sourcesList);

	
	    regionsListView.setAdapter(new RegionsItemAdapter(this, R.layout.sourceitem, sourceItems));
	    regionsListView.setClickable(true);
	    getListOfRegions();
	 }
	
	private void getListOfRegions(){
		dbHelper = new DatabaseHelper(this);
        try {
        	dbHelper.createDataBase();
	 	} catch (IOException ioe) {
	 		Log.e(TAG, "Unable to create database");
	 		throw new Error("Unable to create database");	 		
	 	}
        
	 	try {
	 		dbHelper.openDataBase();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
	 	
	 	sourceItems.addAll(dbHelper.getAllRegions());
	 	
	 	if (dbHelper != null)
			dbHelper.close();
		
	}

}
