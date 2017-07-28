package com.anakarwin.apples.model;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by E460 on 25/07/2017.
 */

public class DataMigration implements RealmMigration {

	@Override
	public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
		RealmSchema schema = realm.getSchema();
		if (oldVersion == 0) {
			schema.create(DateInfo.CLASS_NAME)
				.addField(DateInfo.FIELD_ID, int.class)
				.addField(DateInfo.FIELD_DATE, Date.class)
				.addField(DateInfo.FIELD_STATUS, Date.class);
			schema.create(Student.CLASS_NAME)
				.addField(Student.FIELD_NAME, String.class)
				.addField(Student.FIELD_LEVEL, int.class);
			schema.create(Payment.CLASS_NAME)
				.addField(Payment.FIELD_ID, String.class)
				.addField(Payment.FIELD_DATE, Date.class)
				.addField(Payment.FIELD_STUDENT, Student.class);
			schema.create(Present.CLASS_NAME)
				.addField(Present.FIELD_ID, String.class)
				.addField(Present.FIELD_DATE, Date.class)
				.addField(Present.FIELD_STUDENT, Student.class);
			schema.create(Topic.CLASS_NAME)
				.addField(Topic.FIELD_ID, int.class)
				.addField(Topic.FIELD_DATE, Date.class)
				.addField(Topic.FIELD_CONTENT, String.class)
				.addField(Topic.FIELD_LEVEL, int.class);
			oldVersion++;
		}
	}
}
