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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ReaderDisplay extends Activity {
	private final static String TAG = "ReaderDisplay";
	private TextView myText = null;
	private ListView listView;
	private ImageView masthead;
	private List<NewsItem> feedItems = new ArrayList<NewsItem>();
	private Retriever retriever;
	private PopupWindow pw;

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
			
			masthead = (ImageView) findViewById(R.id.masthead);
			masthead.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {				
					listView.smoothScrollToPosition(0);					
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
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_options_menu, menu);
		return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
			try {
				retriever.update();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            	Toast.makeText(this, "Refreshing", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_preferences:
            	Intent i = new Intent(ReaderDisplay.this, Preferences.class);
                startActivity(i);
                break;
            case R.id.menu_about:
            	showAboutPopup();
            	Toast.makeText(this, "You pressed the icon and text!", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    
    private void showAboutPopup()
    {
        PopupWindow window = new PopupWindow(this);
        window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        window.setTouchable(true);
        window.setFocusable(true);

        EditText text = new EditText(this);
        text.setText("Touch it, it doesn't crash");

        window.setContentView(text);
        window.showAtLocation(text, Gravity.CENTER, 30, 30);
    }
         
   
}
