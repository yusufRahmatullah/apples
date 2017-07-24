package com.anakarwin.apples.model;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

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

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	private Realm realm;

	private List<Payment> payments;
	private List<Student> students;
	private List<Present> presents;
	private List<Topic> topics;

	public void initData(Context context) {
		Realm.init(context);
		realm = Realm.getDefaultInstance();
		presents = realm.where(Present.class).findAll();
		payments = realm.where(Payment.class).findAll();
		students = realm.where(Student.class).findAll();
		topics = realm.where(Topic.class).findAll();
		if (presents.size() == 0 || payments.size() == 0
				|| students.size() == 0 || topics.size() == 0) {
			migrateData();
		}
	}

	private void migrateData() {
		Date d15 = new Date(117, 6, 15);
		Date d16 = new Date(117, 6, 16);
		Date d22 = new Date(117, 6, 22);
		Date d23 = new Date(117, 6, 23);
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
		students = new ArrayList<>();
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

		topics = new ArrayList<>();
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

		presents = new ArrayList<>();
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
		presents.add(new Present(d23, ikhsan));

		payments = new ArrayList<>();
		payments.add(new Payment(d22, fitri));
		payments.add(new Payment(d22, ikhsan));

		realm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				realm.copyToRealmOrUpdate(presents);
				realm.copyToRealmOrUpdate(students);
				realm.copyToRealmOrUpdate(topics);
				realm.copyToRealmOrUpdate(payments);
			}
		});
	}

	private void debug() {
		for (Present present : presents) {
			Log.e("present", "date: " + DateFormat.getInstance().format(present.getDate()));
			Log.e("present", "student: " + present.getStudent().getName());
			Log.e("present", "===========================");
		}

		for (Student student : students) {
			Log.e("student", "name: " + student.getName());
			Log.e("student", "class: " + student.getLevel());
			Log.e("student", "=================================");
		}

		for (Topic topic : topics) {
			Log.e("topic", "date: " + DateFormat.getInstance().format(topic.getDate()));
			Log.e("topic", "level: " + topic.getLevel());
			Log.e("topic", "content: " + topic.getContent());
			Log.e("topic", "========================================");
		}

		for (Payment payment : payments) {
			Log.e("payment", "date: " + DateFormat.getInstance().format(payment.getDate()));
			Log.e("payment", "student: " + payment.getStudent().getName());
			Log.e("payment", "========================================");
		}
	}
}
