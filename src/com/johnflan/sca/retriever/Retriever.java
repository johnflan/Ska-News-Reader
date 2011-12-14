package com.johnflan.sca.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.johnflan.sca.ReaderDisplay;


public class Retriever {
	HttpClient httpClient;
	HttpGet httpGet;
	HttpResponse httpResponse;
	ReaderDisplay readerDisplay;
	List<ResponseItem> feedList;
	List<String> sources;
	
	public Retriever(ReaderDisplay readerDisplay, List<ResponseItem> feedList){
		this.readerDisplay = readerDisplay;
		httpClient = new DefaultHttpClient();
		this.feedList = feedList;
		
		sources = new ArrayList<String>();
		sources.add("http://www.rte.ie/rss/news.xml");
		sources.add("http://www.independent.ie/breaking-news/national-news/rss");
	}
	
	public void requestResource() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		
		for (String source : sources){
			httpGet = new HttpGet(source);
			httpResponse = httpClient.execute(httpGet);
			
			if (httpResponse.getEntity() != null){
				InputStream responseContent = httpResponse.getEntity().getContent();
				getRSSParser(responseContent, source);
			}	
		}
	}
	
	private FeedParser getRSSParser(InputStream responseContent, String source) throws IOException, SAXException, ParserConfigurationException {
		if (source.contains("http://www.rte.ie") )
			return new RTEFeedParser(responseContent, this);
		if ( source.contains("http://www.independent.ie") )
			return new IndependentFeedParser(responseContent, this);
		
		return null;
	}

	public List<ResponseItem> responseContent() {
		return feedList;
	}
	
	public void setResponseItems(List<ResponseItem> items){
		//order list by date-newest first
		feedList.addAll(items);
		//Collections.sort(feedList, new FeedComparator());
	}
	
}
