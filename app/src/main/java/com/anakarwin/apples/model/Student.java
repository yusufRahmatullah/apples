package com.anakarwin.apples.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yusuf on 7/24/2017.
 */

public class Student extends RealmObject {

	public static final String CLASS_NAME = "Student";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_LEVEL = "level";

	@PrimaryKey
	private String name;
	private int level;

	public Student() {
	}

	public Student(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
