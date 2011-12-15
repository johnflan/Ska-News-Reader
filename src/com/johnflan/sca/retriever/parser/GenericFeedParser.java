package com.johnflan.sca.retriever.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.johnflan.sca.retriever.Retriever;

public class GenericFeedParser extends AbstractFeedParser {
	
	private final static String TAG = "Feed Parser";
	
	public GenericFeedParser(InputStream content, Retriever retriever)
			throws IOException, SAXException, ParserConfigurationException {
		super(content, retriever);
		Log.i(TAG, "Using the Generic feed parser");

	}
}
