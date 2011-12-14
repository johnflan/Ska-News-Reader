package com.johnflan.sca.retriever;

public interface ResponseItem {
	
	public String getCreateDate();
	public void setCreateDate(String createDate);
	public String getTitle();
	public void setTitle(String title);
	public String getDescription();
	public void setDescription(String description);
	public String getLink();
	public void setLink(String link);
	public String getPubDate();
	public void setPubDate(String pubDate);
	public boolean alreadyRead();
	public void setAsRead();
}
