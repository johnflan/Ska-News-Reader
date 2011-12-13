package com.johnflan.sca.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
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

import android.util.Log;

public class RTEFeedParser extends FeedParser {
	
	// Used to define what elements we are currently in
    private boolean inItem = false;
    private boolean inTitle = false;
    private boolean inLink = false;
    private boolean inDescription = false;
    private boolean inPubDate = false;
    private boolean inCreateDate = false;
    	
	RTEFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException{
		super(content, retriever);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars = (new String(ch).substring(start, start + length));
		
		try {
            // If not in item, then title/link refers to feed
            if (!inItem) {
                if (inTitle)
                	currentResponseItem.setTitle(chars);
            } else {
                if (inLink)
                    currentResponseItem.setLink(chars);
                if (inTitle)
                    currentResponseItem.setTitle(chars);
                if (inDescription)
                	currentResponseItem.setDescription(chars);
                if (inPubDate)
                	currentResponseItem.setPubDate(chars);
                if (inCreateDate)
                	currentResponseItem.setCreateDate(chars);
            }
            
	    } catch (Exception e) {
	            Log.e("", e.toString());
	    }
	}

	public void endDocument() throws SAXException {
		retriever.setResponseItems(parsedItems);
	}

	public void endElement(String uri, String name, String qName)
			throws SAXException {

		if (name.trim().equals("item")){
	    	inItem = false;   	
	    	parsedItems.add(currentResponseItem);
	    	currentResponseItem = null;
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

	public void startElement(String uri, String name, String qName,
			Attributes atts) throws SAXException {
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
	    	currentResponseItem = new NewsItem();
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
