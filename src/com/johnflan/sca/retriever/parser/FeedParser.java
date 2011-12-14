package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.johnflan.sca.retriever.ResponseItem;
import com.johnflan.sca.retriever.Retriever;

public abstract class FeedParser implements ContentHandler {
    protected SAXParser parser;
    protected XMLReader xmlReader;
	protected Retriever retriever;
	
	//list of objects to be returned
	List<ResponseItem> parsedItems;
	ResponseItem currentResponseItem;
	
	public FeedParser(InputStream content, Retriever retriever) throws IOException, SAXException, ParserConfigurationException{
		
		this.retriever = retriever;
		parsedItems = new ArrayList<ResponseItem>();
		InputSource inputSource = new InputSource(content);
        parser = SAXParserFactory.newInstance().newSAXParser();
        xmlReader = parser.getXMLReader();
        
        xmlReader.setContentHandler(this);
        xmlReader.parse(inputSource);
	}
	
}
