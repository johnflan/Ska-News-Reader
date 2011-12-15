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
import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReaderDisplay extends Activity {
	private final static String TAG = "ReaderDisplay";
	private TextView myText = null;
	private ListView listView;
	private List<NewsItem> feedItems = new ArrayList<NewsItem>();
	private Retriever retriever;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView)findViewById(R.id.newsList);

        try {
			retriever = new Retriever(this, feedItems);
			
			listView.setAdapter(new NewsItemAdapter(this, R.layout.newsitem, feedItems));
			listView.setClickable(true);
			
			listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
					NewsItem currentItem = (NewsItem) adapterView.getItemAtPosition(position);
					if (currentItem.getLink() != null && !currentItem.getLink().equals("")){
						Log.i(TAG, "Opening URL: " + currentItem.getLink());
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData( Uri.parse( currentItem.getLink() ) );
						currentItem.setAsRead();
						startActivity(i);
					}
				}
			});
			
			
			listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					Intent shareIntent=new Intent(android.content.Intent.ACTION_SEND);
					NewsItem currentItem = (NewsItem) adapterView.getItemAtPosition(position);
					
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentItem.getTitle());
					shareIntent.putExtra(Intent.EXTRA_TEXT, currentItem.getLink());
					v.vibrate(75);
					startActivity(Intent.createChooser(shareIntent, "Share news story"));
					return false;
				}
				
			});
			
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
      
   
}
