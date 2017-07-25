package com.anakarwin.apples.model;

import android.content.Context;

import com.anakarwin.apples.plugin.ILoader;
import com.anakarwin.apples.plugin.Loader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by yusuf on 7/23/2017.
 */

public class DAO {

	private static final DAO ourInstance = new DAO();

	public static DAO getInstance() {
		return ourInstance;
	}

	private DAO() {
	}

	// ------------ singleton -------------------

	public void initRealm(Context context) {
		Realm.init(context);
		RealmConfiguration configuration = new RealmConfiguration.Builder()
			.schemaVersion(1)
			.migration(new DataMigration())
			.initialData(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					migrateData(realm);
				}
			})
			.build();
		Realm.setDefaultConfiguration(configuration);
	}

	public List<DateInfo> getDateInfos() {
		int fd = Calendar.getInstance().getActualMinimum(Calendar.DATE);
		int ld = Calendar.getInstance().getActualMaximum(Calendar.DATE);
		Calendar before = new GregorianCalendar();
		before.set(Calendar.DAY_OF_MONTH, fd);
		Calendar after = new GregorianCalendar();
		after.set(Calendar.DAY_OF_MONTH, ld);
		RealmResults<DateInfo> dateInfos = Realm.getDefaultInstance().where(DateInfo.class)
			.between(DateInfo.FIELD_DATE, before.getTime(), after.getTime())
			.findAll();
		return dateInfos;
	}

	public List<Present> getPresent(int year, int month, int dayOfMonth) {
		Date from = new Date(year, month, dayOfMonth, 0, 0, 0);
		Date to = new Date(year, month, dayOfMonth, 23, 59, 59);
		RealmResults<Present> presents = Realm.getDefaultInstance().where(Present.class)
			.between(Present.FIELD_DATE, from, to)
			.findAllSorted(Present.FIELD_STUDENT + "." + Student.FIELD_LEVEL, Sort.ASCENDING);
		return presents;
	}

	private void migrateData(Realm realm) {
		Date d15 = new Date(2017 - 1900, 7 - 1, 15);
		Date d16 = new Date(2017 - 1900, 7 - 1, 16);
		Date d22 = new Date(2017 - 1900, 7 - 1, 22);
		Date d23 = new Date(2017 - 1900, 7 - 1, 23);

		List<DateInfo> dateInfos = new ArrayList<>();
		dateInfos.add(new DateInfo(d15, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d16, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d22, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d23, DateInfo.Type.DONE));

		Student ilham = new Student("Ilham", 6);
		Student silsi = new Student("Silsi", 6);
		Student indah = new Student("Indah", 6);
		Student fitri = new Student("Fitri", 6);
		Student arum = new Student("Arum", 6);
		Student noval = new Student("Noval", 6);
		Student irfan = new Student("Irfan", 9);
		Student habib = new Student("Habib", 9);
		Student tegar = new Student("Tegar", 9);
		Student bayu = new Student("Bayu", 9);
		Student ainun = new Student("Ainun", 9);
		Student ikhsan = new Student("Ikhsan", 10);
		List<Student> students = new ArrayList<>();
		students.add(ilham);
		students.add(silsi);
		students.add(indah);
		students.add(arum);
		students.add(fitri);
		students.add(noval);
		students.add(irfan);
		students.add(habib);
		students.add(tegar);
		students.add(bayu);
		students.add(ainun);
		students.add(ikhsan);

		List<Topic> topics = new ArrayList<>();
		topics.add(new Topic(d15, 6, "Konversi Bilangan Desimal"));
		topics.add(new Topic(d16, 6, "Konversi Bilangan Desimal"));
		topics.add(new Topic(d22, 6, "Mengurutkan Bilandan Desimal"));
		topics.add(new Topic(d23, 6, "Operasi Bilangan Desimal"));
		topics.add(new Topic(d15, 9, "Operasi Bilangan Berpangkat"));
		topics.add(new Topic(d16, 9, "Bilangan Baku"));
		topics.add(new Topic(d22, 9, "Kesebangunan"));
		topics.add(new Topic(d23, 9, "Kesebangunan"));
		topics.add(new Topic(d15, 10, "Persamaan Nilai Mutlak"));
		topics.add(new Topic(d16, 10, "Persamaan Nilai Mutlak"));
		topics.add(new Topic(d22, 10, "Pertidaksamaan Nilai Mutlak"));

		List<Present> presents = new ArrayList<>();
		presents.add(new Present(d15, ilham));
		presents.add(new Present(d16, ilham));
		presents.add(new Present(d22, ilham));
		presents.add(new Present(d23, ilham));
		presents.add(new Present(d15, silsi));
		presents.add(new Present(d22, silsi));
		presents.add(new Present(d23, silsi));
		presents.add(new Present(d15, indah));
		presents.add(new Present(d16, indah));
		presents.add(new Present(d22, indah));
		presents.add(new Present(d23, indah));
		presents.add(new Present(d22, fitri));
		presents.add(new Present(d23, fitri));
		presents.add(new Present(d22, arum));
		presents.add(new Present(d23, arum));
		presents.add(new Present(d22, noval));

		presents.add(new Present(d15, irfan));
		presents.add(new Present(d16, irfan));
		presents.add(new Present(d22, irfan));
		presents.add(new Present(d15, habib));
		presents.add(new Present(d16, habib));
		presents.add(new Present(d22, habib));
		presents.add(new Present(d23, habib));
		presents.add(new Present(d15, tegar));
		presents.add(new Present(d23, tegar));
		presents.add(new Present(d15, bayu));
		presents.add(new Present(d16, bayu));
		presents.add(new Present(d22, bayu));
		presents.add(new Present(d23, bayu));
		presents.add(new Present(d22, ainun));
		presents.add(new Present(d23, ainun));

		presents.add(new Present(d15, ikhsan));
		presents.add(new Present(d16, ikhsan));
		presents.add(new Present(d22, ikhsan));

		List<Payment> payments = new ArrayList<>();
		payments.add(new Payment(d22, fitri));
		payments.add(new Payment(d22, ikhsan));

		realm.copyToRealmOrUpdate(dateInfos);
		realm.copyToRealmOrUpdate(presents);
		realm.copyToRealmOrUpdate(students);
		realm.copyToRealmOrUpdate(topics);
		realm.copyToRealmOrUpdate(payments);
	}
}
