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

public class RTEFeedParser extends FeedParser {
	private final static String TAG = "RTE Feed Parser";
	
	// Used to define what elements we are currently in
    private boolean inItem = false;
    private boolean inTitle = false;
    private boolean inLink = false;
    private boolean inDescription = false;
    private boolean inPubDate = false;
    private boolean inCreateDate = false;
    	
	public RTEFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException{
		super(content, retriever);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = (new String(ch).substring(start, start + length));
		
		try {
            // If not in item, then title/link refers to feed
            if (inItem) {
            	if (inTitle)
                	newsItem.setTitle( newsItem.getTitle() + chars );
            	else if (inLink)               	
                    newsItem.setLink(parseURL(chars));
            	else if (inDescription)
                	newsItem.setDescription(newsItem.getDescription() + chars );
            	else if (inPubDate)
                	newsItem.setPubDate(parseDate(chars));
            	else if (inCreateDate)
                	newsItem.setCreateDate(chars);
            }
            
	    } catch (Exception e) {
	            Log.e("", e.toString());
	    }
	}

	private Date parseDate(String chars) {
		//Thu, 08 Dec 2011 17:32:02+
		Log.i(TAG, "date text: " + chars);
		
		SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss"); 
		Date dateObj;
		try {
			dateObj = curFormater.parse(chars);
		} catch (ParseException e) {
			dateObj = new Date();
			Log.e(TAG, "Date string parse error");
		} 
		
		Log.i(TAG, "parsed date: " + dateObj.toString());
		
		return dateObj;
	}

	private String parseURL(String chars) {
		//http://www.rte.ie/news/2011/1213/education.html
		Log.i(TAG, "Creating mobile url for: " + chars);
		return chars.substring(0, 7) + "m" + chars.substring(10);
	}

	public void endDocument() throws SAXException {
		retriever.setResponseItems(parsedItems);
	}

	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.trim().equals("item")){
	    	inItem = false;   	
	    	parsedItems.add(newsItem);
	    	newsItem = null;
	    } else if (inTitle)
	    	inTitle = false;
	    else if (name.trim().equals("guid"))
	    	inLink = false;
	    else if (name.trim().equals("description"))
	    	inDescription = false;
	    else if (name.trim().equals("pubDate"))
	    	inPubDate = false;
	    else if (name.trim().equals("createDate"))
	    	inCreateDate = false;
		
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
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
	    else if (name.trim().equals("createDate") )
	    	inCreateDate = true;		
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

}
