package com.johnflan.sca.retriever;

import java.util.Date;

public class NewsItem {
	
	private String title = new String();
	private String description = new String();
	private String link = new String();
	private Date pubDate = null;
	private boolean read = false;
	
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = sanitizeText(title);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = sanitizeText(description);
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public Date getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(Date pubDate) {
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
	
	private String sanitizeText(String input){
		
		return input.replaceAll("<P>", "").replace("</P>", "");
	}
	
}
