package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

import android.util.Log;

public class RTEFeedParser extends AbstractFeedParser {
	private final static String TAG = "RTE Feed Parser";
	    	
	public RTEFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException{
		super(content, retriever);
	}


	@Override
	protected String parseURL(String chars) {
		//http://www.rte.ie/news/2011/1213/education.html
		Log.i(TAG, "Creating mobile url for: " + chars);
		if (chars.contains("www.rte"))
			return chars.substring(0, 7) + "m" + chars.substring(10);
		return chars;
	}

	
	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.trim().equals("item")){
			if (newsItem.getLink() != null && newsItem.getTitle() != null && !pubDate.equals("")) {
	    		newsItem.setPubDate(parseDate(pubDate));
		    	inItem = false;   	
		    	parsedItems.add(newsItem);
			}
	    	newsItem = null;
	    } else if (inTitle)
	    	inTitle = false;
	    else if (name.trim().equals("guid"))
	    	inLink = false;
	    else if (name.trim().equals("description"))
	    	inDescription = false;
	    else if (name.trim().equals("pubDate"))
	    	inPubDate = false;
	}


	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
//		<item>
//	      <title>Tánaiste concedes Ireland ma</title>
//	      <description>Tánaiste Eamon Gilmore</description>
//	      <pubDate>Thu, 08 Dec 2011 17:32:02+</pubDate>
//	      <createDate>Thu, 08 Dec 2011 00:00:</createDate>
//	      <link>http://www.rte.ie/news/2011/1</link>
//	      <guid isPermaLink="true">http://www.rte.ie/news/2011/1208/eurozone.html</guid>
//	    </item>
		
	    if (name.trim().equals("item")){
	    	inItem = true;
	    	newsItem = new NewsItem();
	    } else if (name.trim().equals("title") )
	    	inTitle = true;
	    else if (name.trim().equals("guid") )
	    	inLink = true;
	    else if (name.trim().equals("description") )
	    	inDescription = true;
	    else if (name.trim().equals("pubDate") )
	    	inPubDate = true;	
	}
}
