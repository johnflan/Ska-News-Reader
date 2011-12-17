package com.johnflan.sca.retriever;

import java.util.Comparator;
import java.util.Date;

import android.util.Log;

public class FeedComparator implements Comparator<NewsItem> {
	private final static String TAG = "Feed Comparator";
// The compare method compares its two arguments, returning a negative integer,
// 0, or a positive integer depending on whether the first argument is less than,
// equal to, or greater than the second.
//	if(empAge1>empAge2) return 1;
//	else if(empAge1<empAge2) return -1;
//	else return 0;
	
	public int compare(NewsItem item1, NewsItem item2) {
		
		if (item1.getPubDate() == null || item2.getPubDate() == null){
			if (item1.getPubDate() == null)
				Log.i(TAG, "Date error: " + item1.getTitle().substring(0, 20) + " " + item1.getPubDate() +", link:"+ item1.getLink());
			else
				Log.i(TAG, "Date error: " + item2.getTitle().substring(0, 20) + " " + item2.getPubDate() + " " + item1.getPubDate() +", link:"+ item2.getLink());
		}
		
		Date item1Date = item1.getPubDate();
		Date item2Date = item2.getPubDate();
		
		return item1Date.compareTo(item2Date);
	}

}
