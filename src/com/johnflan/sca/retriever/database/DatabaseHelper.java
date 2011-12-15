package com.johnflan.sca.retriever.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.johnflan.sca.retriever.NewsSource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	private final static String TAG = "Database helper";
    //The Android's default system path of your application database.
    private static String DB_PATH = Environment.getDataDirectory() + "/data/com.johnflan.sca/databases/";

    private static String DB_NAME = "daily_sca.db";
    
	private final static String DB_SOURCES = "sources";
	private final static String DB_SOURCES_URL = "url";
	private final static String DB_SOURCES_REGION = "region";
	private final static String DB_SOURCES_NAME = "name";
	private final static String DB_SOURCES_ID = "_id";
	private final static String DB_SOURCES_PARSER = "parser";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	//TODO: always dump the phones database
    	dbExist = false;
    	
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		Log.i(TAG, "DB_PATH + DB_NAME: " + DB_PATH + DB_NAME);
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		Log.w(TAG, "Database doesn't exist yet");
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
    	
    	//if the database directory does not exist initially
    	File f = new File(DB_PATH);
    	if (!f.exists()) {
    		f.mkdir();
    	}
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
	public void printTables(){
		//debug
		ArrayList<Object> tableList = new ArrayList<Object>();
        String SQL_GET_ALL_TABLES = "SELECT name FROM " + 
        "sqlite_master WHERE type='table' ORDER BY name"; 
        Cursor cursor = myDataBase.rawQuery(SQL_GET_ALL_TABLES, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                tableList.add(cursor.getString(0));
                Log.i(TAG, "Tables in DB: " + cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
	}
	
	public List<NewsSource> getNewsSources(){
		List<NewsSource> newsSources = new ArrayList<NewsSource>();
		Cursor mCursor = myDataBase.query(DB_SOURCES, new String[]{ "name", "url", "region", "parser" },
		null, null, null, null, null, null);
		NewsSource tempSource;
		if (mCursor != null && mCursor.getCount() > 0){
			Log.i(TAG, "got a database cursor");
			mCursor.moveToFirst();
			String[] colNames = mCursor.getColumnNames();
			int url = mCursor.getColumnIndex(DB_SOURCES_URL);
			int region = mCursor.getColumnIndex(DB_SOURCES_REGION); 
			int name = mCursor.getColumnIndex(DB_SOURCES_NAME);
			int parser = mCursor.getColumnIndex(DB_SOURCES_PARSER);
						
			if (url == -1 || region == -1 || name == -1 || parser == -1)
				Log.e(TAG, "did not parse the col indexes from the sources table");
			
			while (!mCursor.isAfterLast()){
				tempSource = new NewsSource();
				tempSource.setName(mCursor.getString(name));
				tempSource.setUrl(mCursor.getString(url));
				tempSource.setParser(mCursor.getString(parser));
				tempSource.setRegion(mCursor.getString(region));
				
				Log.i(TAG, "Loaded source from DB: " + tempSource);
				
				newsSources.add(tempSource);
				mCursor.moveToNext();
			}
			
		} 
		
		return newsSources;
	}
}
