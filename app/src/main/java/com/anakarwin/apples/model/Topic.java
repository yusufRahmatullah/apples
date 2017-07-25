package com.anakarwin.apples.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by yusuf on 7/24/2017.
 */

public class Topic extends RealmObject {

	public static final String CLASS_NAME = "Topic";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_LEVEL = "level";
	public static final String FIELD_CONTENT = "content";

	private Date date;
	private int level;
	private String content;

	public Topic() {
	}

	public Topic(Date date, int level, String content) {
		this.date = date;
		this.level = level;
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
