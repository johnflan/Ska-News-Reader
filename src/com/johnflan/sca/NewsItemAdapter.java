package com.johnflan.sca;

import java.util.List;

import com.johnflan.sca.R;
import com.johnflan.sca.retriever.NewsItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
	private final static String TAG = "NewsItemAdapter";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private Context context;

	public NewsItemAdapter(Context context, int resourceId, List<NewsItem> newsItems) {
		super(context, 0, newsItems);
	    this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		//super.setNotifyOnChange(true);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		TextView textTitle;
		TextView textBody;
	
	    view = inflater.inflate(resourceId, parent, false);
	
	    try {
	    	textTitle = (TextView)view.findViewById(R.id.newstitle);
	    	textBody = (TextView)view.findViewById(R.id.newsbody);
	    } catch( ClassCastException e ) {
	    	Log.e(TAG, "Your layout must provide an image and a text view with ID's icon and text.", e);
	    	throw e;
	    }
	
		NewsItem item = getItem(position);

	    textTitle.setText(item.getTitle());
	    textBody.setText(item.getDescription());
//	    Log.i(TAG, item.toString());
		
	    return view;
	  }


}
