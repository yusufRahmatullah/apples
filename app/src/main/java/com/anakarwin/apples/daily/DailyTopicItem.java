package com.anakarwin.apples.daily;

/**
 * Created by E460 on 27/07/2017.
 */

public class DailyTopicItem {

	private int level;
	private String content;

	public DailyTopicItem() {
	}

	public DailyTopicItem(int level, String content) {
		this.level = level;
		this.content = content;
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
