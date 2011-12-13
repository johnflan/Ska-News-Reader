package com.johnflan.sca.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
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
	InputStream responseContent;
	RTEFeedParser rssParser;
	ReaderDisplay readerDisplay;
	List<ResponseItem> responseItems;
	
	public Retriever(URI dataFeed, ReaderDisplay readerDisplay){
		this.readerDisplay = readerDisplay;
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet(dataFeed);
	}
	
	public boolean requestResource() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		httpResponse = httpClient.execute(httpGet);
		
		if (httpResponse.getEntity() != null){
			responseContent = httpResponse.getEntity().getContent();
			rssParser = new RTEFeedParser(responseContent, this);
			return true;
		}

		return false;
	}
	
	public String peek(){
		return httpResponse.getStatusLine().toString();
	}


	public List<ResponseItem> responseContent() {
		return responseItems;
	}
	
	public void setResponseItems(List<ResponseItem> items){
		responseItems = items;
		
		for (ResponseItem item : items){
			readerDisplay.debugText(item.toString());
		}
		readerDisplay.updateList();
	}

	public void debugText(String string) {
		readerDisplay.debugText(string);
		
	}

	
}
