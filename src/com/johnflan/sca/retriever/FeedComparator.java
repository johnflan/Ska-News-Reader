package com.johnflan.sca.retriever;

import java.util.Comparator;
import java.util.Date;

public class FeedComparator implements Comparator<NewsItem> {
// The compare method compares its two arguments, returning a negative integer,
// 0, or a positive integer depending on whether the first argument is less than,
// equal to, or greater than the second.
//	if(empAge1>empAge2) return 1;
//	else if(empAge1<empAge2) return -1;
//	else return 0;
	
	public int compare(NewsItem item1, NewsItem item2) {
		Date item1Date = item1.getPubDate();
		Date item2Date = item2.getPubDate();
		
		return item1Date.compareTo(item2Date);
	}

}
