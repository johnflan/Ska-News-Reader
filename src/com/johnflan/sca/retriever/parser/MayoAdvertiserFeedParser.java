package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

public class MayoAdvertiserFeedParser extends AbstractFeedParser {
	private final static String TAG = "Mayo Advertiser Feed Parser";
	
	public MayoAdvertiserFeedParser(InputStream content, Retriever retriever)
			throws IOException, SAXException, ParserConfigurationException {
		super(content, retriever);
		// TODO Auto-generated constructor stub
	}
	
	//<item rdf:about="http://www.advertiser.ie/mayo/article/47699">
    //<title>Education is the future at Anne Tobin Beauty College</title>
    //<link>http://feedproxy.google.com/~r/MayoAdvertiser/~3/WSi_s5Gfu7Y/47699</link>
    //<description>If you are thinking of upskilling and f</description>
    //<dc:subject>News</dc:subject>
    //<dc:format>text/html</dc:format>
    //<dc:date>2011-12-16T00:00:00+00:00</dc:date>
	//<feedburner:origLink>http://www.advertiser.ie/mayo/article/47699</feedburner:origLink>
	//</item>

	@Override
	protected Date parseDate(String chars) {
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); 
		Date dateObj;
		try {
			dateObj = curFormater.parse(chars);
		} catch (ParseException e) {
			dateObj = new Date();
			Log.e(TAG, "Date string parse error, string: "+ chars) ;
		} 
	
		return dateObj;
	}
	
	@Override
	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
		
		
		//Log.d(TAG, "Start element name: " + name + ", " + qName + ", " + atts);
	    if (name.trim().equals("item")){
	    	inItem = true;
	    	newsItem = new NewsItem();
	    } else if (name.trim().equals("title") )
	    	inTitle = true;
	    else if (name.trim().equals("origLink") )
	    	inLink = true;
	    else if (name.trim().equals("description") )
	    	inDescription = true;
	    else if (qName.trim().equals("dc:date") )
	    	inPubDate = true;
	    	
	}
	
	@Override
	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.trim().equals("item")){
	    	inItem = false;
	    	if (newsItem.getLink() != null && newsItem.getTitle() != null && !pubDate.equals("")) {
	    		newsItem.setPubDate(parseDate(pubDate));
	    		sanatizeStrings();
	    		parsedItems.add(newsItem);
	    	} else {
	    		Log.w(TAG, "Dropped news item: " + newsItem.getTitle() + ", " + newsItem.getLink() + ", " + newsItem.getPubDate());;
	    	}
	    	newsItem = null;
	    } else if (inTitle)
	    	inTitle = false;
	    else if (name.trim().equals("origLink"))
	    	inLink = false;
	    else if (name.trim().equals("description"))
	    	inDescription = false;
	    else if (qName.trim().equals("dc:date"))
	    	inPubDate = false;	
	}

}
