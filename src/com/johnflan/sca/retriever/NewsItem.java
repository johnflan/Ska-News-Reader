package com.johnflan.sca.retriever;

import java.net.URI;

public class NewsItem implements ResponseItem {
	
	private String title = null;
	private String description = null;
	private String link = null;
	private String createDate = null;
	private String pubDate = null;
	private boolean read = false;
	
	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	public String toString(){
		return title +"\n"+description+"\n"+link+"\n\n";
	}


	public boolean alreadyRead() {
		return read;
	}

	public void setAsRead() {
		this.read = true;
	}

	
}
