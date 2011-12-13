package com.johnflan.sca;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import com.johnflan.sca.R;
import com.johnflan.sca.retriever.ResponseItem;
import com.johnflan.sca.retriever.Retriever;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReaderDisplay extends Activity {
	private final static String TAG = "ReaderDisplay";
	private TextView myText = null;
	private ListView listView;
	private List<ResponseItem> feedItems ;
	private Retriever retriever;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView)findViewById(R.id.newsList);
        
        
//        LinearLayout lView = new LinearLayout(this);
//
//        myText = new TextView(this);     
//        lView.addView(myText);
//        setContentView(lView);
        
        URI dataFeed = null;

        try {
			dataFeed = new URI("http://www.rte.ie/rss/news.xml");
			retriever = new Retriever(dataFeed, this);
			retriever.requestResource();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		
    }
    
    public void debugText(String text){
    	//myText.append(text);
    	Log.i(TAG, text);
    }
    
    public void updateList(){
    	feedItems = retriever.responseContent();
		Log.i(TAG, "Updating List - feedItems list contains: " + feedItems.size() + " items");
    	if(feedItems != null){
    		Log.i(TAG, "Updating List");
    		listView.setAdapter(new NewsItemAdapter(this, R.layout.newsitem, feedItems));
    	}

    	    	
    }
}