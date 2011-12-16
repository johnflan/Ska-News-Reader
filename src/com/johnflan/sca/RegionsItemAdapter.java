package com.johnflan.sca;

import java.util.List;

import com.johnflan.sca.R;
import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.database.SourceRegion;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class RegionsItemAdapter extends ArrayAdapter<SourceRegion> {
	private final static String TAG = "RegionItemAdapter";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private Context context;

	public RegionsItemAdapter(Context context, int resourceId, List<SourceRegion> sourceItems) {
		super(context, 0, sourceItems);
	    this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		//super.setNotifyOnChange(true);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		TextView textRegion;
		CheckBox checkBox;
	
	    view = inflater.inflate(resourceId, parent, false);
	
	    try {
	    	textRegion = (TextView) view.findViewById(R.id.sourceRegion);
	    	checkBox = (CheckBox) view.findViewById(R.id.sourceSelected);
	    } catch( ClassCastException e ) {
	    	Log.e(TAG, "Your layout must provide an image and a text view with ID's icon and text.", e);
	    	throw e;
	    }
	
		SourceRegion region = getItem(position);

		textRegion.setText(region.getRegionName());
	    checkBox.setChecked(region.isSelected());

		
	    return view;
	  }


}
