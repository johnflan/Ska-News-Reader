package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

import android.util.Log;

public class IndependentFeedParser extends FeedParser {
	
	private boolean inItem = false;
	private boolean inTitle = false;
	private boolean inLink = false;
	private boolean inDescription = false;
	private boolean inPubDate = false;

	public IndependentFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException {
		super(content, retriever);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = (new String(ch).substring(start, start + length));

		try {
            // If not in item, then title/link refers to feed
            if (inItem) {
                if (inLink)           	
                    currentResponseItem.setLink(currentResponseItem.getLink() + chars);
                if (inTitle)
                    currentResponseItem.setTitle(currentResponseItem.getTitle() + chars);
                if (inDescription)
                	currentResponseItem.setDescription(currentResponseItem.getDescription() + chars);
                if (inPubDate)
                	currentResponseItem.setPubDate(chars);
            }
	    } catch (Exception e) {
	            Log.e("", e.toString());
	    }

	}

	public void endDocument() throws SAXException {
		retriever.setResponseItems(parsedItems);
	}

	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.trim().equals("item")){
	    	inItem = false;   	
	    	parsedItems.add(currentResponseItem);
	    	currentResponseItem = null;
	    } else if (inTitle)
	    	inTitle = false;
	    else if (name.trim().equals("link"))
	    	inLink = false;
	    else if (name.trim().equals("description"))
	    	inDescription = false;
	    else if (name.trim().equals("pubDate"))
	    	inPubDate = false;	
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
//			<title>Half of acute hospitals may be axed</title>
//			<guid>http://www.independent.ie/breaking-news/national-news/half-of-acute-hospitals-may-be-axed-2962076.html</guid>
//			<link>http://www.independent.ie/breaking-news/national-news/half-of-acute-hospitals-may-be-axed-2962076.html</link>
//			<description><P>A radical healthovide more treatment outside the region's wards.</P></description>
//			<category domain="http://www.independent.ie/breaking-news/national-news/">National News</category>
//			<pubDate>Tue, 13 Dec 2011 11:02:47 +0000</pubDate>
//		</item>
		
	    if (name.trim().equals("item")){
	    	inItem = true;
	    	currentResponseItem = new NewsItem();
	    } else if (name.trim().equals("title") )
	    	inTitle = true;
	    else if (name.trim().equals("link") )
	    	inLink = true;
	    else if (name.trim().equals("description") )
	    	inDescription = true;
	    else if (name.trim().equals("pubDate") )
	    	inPubDate = true;

	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

}
