package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.johnflan.sca.retriever.NewsItem;
import com.johnflan.sca.retriever.Retriever;

import android.util.Log;

public class IndependentFeedParser extends AbstractFeedParser {
	
	private final static String TAG = "Independent Feed Parser";

	public IndependentFeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException {
		super(content, retriever);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = (new String(ch).substring(start, start + length));

        // If not in item, then title/link refers to feed
        if (inItem) {
            if (inLink)           	
                newsItem.setLink(newsItem.getLink() + chars);
            if (inTitle)
                newsItem.setTitle(newsItem.getTitle() + chars);
            if (inDescription)
            	newsItem.setDescription(newsItem.getDescription() + chars);
            if (inPubDate)
            	newsItem.setPubDate(parseDate(chars));
        }
	}

}
