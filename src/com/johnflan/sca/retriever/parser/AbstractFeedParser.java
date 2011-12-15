package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jsoup.Jsoup;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

public abstract class AbstractFeedParser implements ContentHandler {
	private final static String TAG = "Abstract Feed Parser";
    protected SAXParser parser;
    protected XMLReader xmlReader;
	protected Retriever retriever;
	
	// Used to define what elements we are currently in
    protected boolean inItem = false;
    protected boolean inTitle = false;
    protected boolean inLink = false;
    protected boolean inDescription = false;
    protected boolean inPubDate = false;
	
	//list of objects to be returned
	List<NewsItem> parsedItems;
	NewsItem newsItem;
	
	public AbstractFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException{
		
		this.retriever = retriever;
		parsedItems = new ArrayList<NewsItem>();
		InputSource inputSource = new InputSource(content);
        parser = SAXParserFactory.newInstance().newSAXParser();
        xmlReader = parser.getXMLReader();
        
        xmlReader.setContentHandler(this);
        xmlReader.parse(inputSource);
	}
	
	protected Date parseDate(String chars) {
			
		SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss"); 
		Date dateObj;
		try {
			dateObj = curFormater.parse(chars);
		} catch (ParseException e) {
			dateObj = new Date();
			Log.e(TAG, "Date string parse error, string: "+ chars) ;
		} 
	
		return dateObj;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = (new String(ch).substring(start, start + length));

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
        }
	}
	
	protected String parseURL(String chars) {
		return chars;
	}

	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
				
	    if (name.trim().equals("item")){
	    	inItem = true;
	    	newsItem = new NewsItem();
	    } else if (name.trim().equals("title") )
	    	inTitle = true;
	    else if (name.trim().equals("link") )
	    	inLink = true;
	    else if (name.trim().equals("description") )
	    	inDescription = true;
	    else if (name.trim().equals("pubDate") )
	    	inPubDate = true;

	}
	
	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.trim().equals("item")){
	    	inItem = false;
	    	sanatizeStrings();
	    	parsedItems.add(newsItem);
	    	newsItem = null;
	    } else if (inTitle)
	    	inTitle = false;
	    else if (name.trim().equals("link"))
	    	inLink = false;
	    else if (name.trim().equals("description"))
	    	inDescription = false;
	    else if (name.trim().equals("pubDate"))
	    	inPubDate = false;	
	}
	
	public void endDocument() throws SAXException {
		
		retriever.setResponseItems(parsedItems);
	}
	
	private void sanatizeStrings() {
		newsItem.setTitle(Jsoup.parse(newsItem.getTitle()).text());
		newsItem.setDescription(Jsoup.parse(newsItem.getDescription()).text());
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
	
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
}
