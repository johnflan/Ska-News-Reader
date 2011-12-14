package com.johnflan.sca.retriever;

import java.util.Date;

public interface ResponseItem {
	
	public String getCreateDate();
	public void setCreateDate(String createDate);
	public String getTitle();
	public void setTitle(String title);
	public String getDescription();
	public void setDescription(String description);
	public String getLink();
	public void setLink(String link);
	public Date getPubDate();
	public void setPubDate(Date pubDate);
	public boolean alreadyRead();
	public void setAsRead();
}
