package com.johnflan.sca.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.johnflan.sca.ReaderDisplay;
import com.johnflan.sca.retriever.database.DatabaseHelper;
import com.johnflan.sca.retriever.parser.FeedParser;
import com.johnflan.sca.retriever.parser.IndependentFeedParser;
import com.johnflan.sca.retriever.parser.RTEFeedParser;


public class Retriever {
	private final static String TAG = "Retriever";
		
	HttpClient httpClient;
	HttpGet httpGet;
	HttpResponse httpResponse;
	ReaderDisplay readerDisplay;
	List<NewsItem> feedList;
	List<NewsSource> sources;
	
	DatabaseHelper dbHelper;
	
	public Retriever(ReaderDisplay readerDisplay, List<NewsItem> feedList) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException{
		this.readerDisplay = readerDisplay;
		httpClient = new DefaultHttpClient();
		this.feedList = feedList;	
			
		openDB();
		getSources();
		requestResource();
	}
	
	

	public void requestResource() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		
		for (NewsSource source : sources){
			httpGet = new HttpGet(source.getUrl());
			httpResponse = httpClient.execute(httpGet);
			
			if (httpResponse.getEntity() != null){
				InputStream responseContent = httpResponse.getEntity().getContent();
				getRSSParser(responseContent, source.getUrl());
			}	
		}
	}
	
	private FeedParser getRSSParser(InputStream responseContent, String source) throws IOException, SAXException, ParserConfigurationException {
		if (source.contains("http://www.rte.ie") )
			return new RTEFeedParser(responseContent, this);
		if ( source.contains("http://www.independent.ie") )
			return new IndependentFeedParser(responseContent, this);
		
		return null;
	}

	public List<NewsItem> responseContent() {
		return feedList;
	}
	
	public void setResponseItems(List<NewsItem> items){
		//order list by date-newest first
		feedList.addAll(items);
		Collections.sort(feedList, new FeedComparator());
	}


	public void update() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		requestResource();		
	}
	
	private void openDB(){
		
        dbHelper = new DatabaseHelper(readerDisplay);
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
	}
	
	private void getSources() {

		sources = dbHelper.getNewsSources();
		
		//if the db has no sources use these debug sources..
		if (sources.isEmpty()){
			sources = new ArrayList<NewsSource>();
			NewsSource tmp = new NewsSource();	
			tmp.setUrl("http://www.rte.ie/rss/news.xml");
			sources.add(tmp);
			tmp = new NewsSource();	
			tmp.setUrl("http://www.independent.ie/breaking-news/national-news/rss");
			sources.add(tmp);
		}
	
		if (dbHelper != null)
			dbHelper.close();	
	}
}
