package com.anakarwin.apples.model;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
	public static final SimpleDateFormat dayDateFormatter = new SimpleDateFormat("EEE, dd-MM-yyy", Locale.getDefault());

	private static final int DB_VERSION = 1;

	public void initRealm(Context context) {
		Realm.init(context);
		RealmConfiguration configuration = new RealmConfiguration.Builder()
			.schemaVersion(DB_VERSION)
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

	//region present
	public List<Present> getPresent(int year, int month, int dayOfMonth) {
		Date from = new Date(year, month, dayOfMonth, 0, 0, 0);
		Date to = new Date(year, month, dayOfMonth, 23, 59, 59);
		RealmResults<Present> presents = Realm.getDefaultInstance().where(Present.class)
			.between(Present.FIELD_DATE, from, to)
			.findAllSorted(Present.FIELD_STUDENT + "." + Student.FIELD_LEVEL, Sort.ASCENDING);
		return presents;
	}

	public List<Present> getStudentPresents (String name) {
		return Realm.getDefaultInstance().where(Present.class)
			.equalTo(Present.FIELD_STUDENT + "." + Student.FIELD_NAME, name)
			.findAllSorted(Present.FIELD_DATE, Sort.DESCENDING);
	}

	public Present getStudentPresent (String name, Date date) {
		return Realm.getDefaultInstance().where(Present.class)
			.equalTo(Present.FIELD_ID, Present.generatePresentId(name, date))
			.findFirst();
	}


	public void savePresents(final List<Present> presents) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealmOrUpdate(presents);
			}
		});
	}

	public void addPresent(final Present present) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealmOrUpdate(present);
			}
		});
	}

	public void deletePresent(Present present) {
		final Present first = Realm.getDefaultInstance().where(Present.class)
			.equalTo(Present.FIELD_ID, present.getId())
			.findFirst();
		if (first != null) {
			Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					first.deleteFromRealm();
				}
			});
		}
	}
	//endregion

	//region student
	public List<Student> getStudents() {
		return Realm.getDefaultInstance().where(Student.class)
			.findAllSorted(Student.FIELD_LEVEL);
	}

	public Student getStudent(String name) {
		return Realm.getDefaultInstance().where(Student.class)
			.equalTo(Student.FIELD_NAME, name)
			.findFirst();
	}

	public void addStudent(final Student student) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealm(student);
			}
		});
	}

	public void deleteStudent(Student student) {
		final Student recorded = Realm.getDefaultInstance().where(Student.class)
			.equalTo(Student.FIELD_NAME, student.getName())
			.findFirst();
		if (recorded != null) {
			Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					recorded.deleteFromRealm();
				}
			});
		}
	}
	//endregion

	//region payment
	public List<Payment> getPayments() {
		return Realm.getDefaultInstance().where(Payment.class)
			.findAllSorted(Payment.FIELD_DATE, Sort.DESCENDING);
	}

	public List<Payment> getPayments(int year, int month, int dayOfMonth) {
		Date from = new Date(year, month, dayOfMonth, 0, 0, 0);
		Date to = new Date(year, month, dayOfMonth, 23, 59, 59);
		return Realm.getDefaultInstance().where(Payment.class)
			.between(Payment.FIELD_DATE, from, to)
			.findAllSorted(Payment.FIELD_STUDENT + "." + Student.FIELD_LEVEL);
	}

	public void addPayment(final Payment payment) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealm(payment);
			}
		});
	}

	public void deletePayment(Payment payment) {
		final Payment first = Realm.getDefaultInstance().where(Payment.class)
			.equalTo(Payment.FIELD_ID, payment.getId())
			.findFirst();
		if (first != null) {
			Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					first.deleteFromRealm();
				}
			});
		}
	}

	public Payment getStudentPayment(String name, Date date) {
		RealmResults<Payment> payments = Realm.getDefaultInstance().where(Payment.class)
			.equalTo(Payment.FIELD_STUDENT + "." + Student.FIELD_NAME, name)
			.findAll();
		for (Payment payment : payments) {
			Date recDate = payment.getDate();
			if (recDate.getYear() == date.getYear() &&
				recDate.getMonth() == date.getMonth() &&
				recDate.getDate() == date.getDate()) {
				return payment;
			}
		}
		return null;
	}

	public Date getStudentLastPayment(String name) {
		RealmResults<Payment> payments = Realm.getDefaultInstance().where(Payment.class)
			.equalTo(Payment.FIELD_STUDENT + "." + Student.FIELD_NAME, name)
			.findAllSorted(Payment.FIELD_DATE, Sort.DESCENDING);
		if (payments != null && payments.size() > 0) {
			return payments.get(0).getDate();
		}
		return null;
	}

	public int getStudentPaymentsCount(String name) {
		RealmResults<Payment> payments = Realm.getDefaultInstance().where(Payment.class)
			.equalTo(Payment.FIELD_STUDENT + "." + Student.FIELD_NAME, name)
			.findAll();
		if (payments != null && payments.size() > 0) {
			return payments.size();
		}
		return 0;
	}
	//endregion

	//region topics
	public List<Topic> getTopics () {
		return Realm.getDefaultInstance().where(Topic.class)
			.findAllSorted(Topic.FIELD_DATE, Sort.DESCENDING);
	}

	public List<Topic> getTopics(int year, int month, int dayOfMonth) {
		Date from = new Date(year, month, dayOfMonth, 0, 0, 0);
		Date to = new Date(year, month, dayOfMonth, 23, 59, 59);
		return Realm.getDefaultInstance().where(Topic.class)
			.between(Topic.FIELD_DATE, from, to)
			.findAllSorted(Topic.FIELD_LEVEL);
	}

	public Topic getTopic(Date date, int level) {
		return Realm.getDefaultInstance().where(Topic.class)
			.equalTo(Topic.FIELD_ID, Topic.generateTopicId(date, level))
			.findFirst();
	}

	public void deleteTopic(Topic topic) {
		final Topic first = Realm.getDefaultInstance().where(Topic.class)
			.equalTo(Topic.FIELD_ID, topic.getId())
			.findFirst();
		if (first != null) {
			Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					first.deleteFromRealm();
				}
			});
		}
	}

	public void addTopic(final Topic topic) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealmOrUpdate(topic);
			}
		});
	}

	public void saveTopics(final List<Topic> topics) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealmOrUpdate(topics);
			}
		});
	}
	//endregion

	//region date info
	public List<DateInfo> getDateInfos(int year, int month, int dayOfMonth) {
		Calendar before = new GregorianCalendar(year, month, dayOfMonth);
		int fd = before.getActualMinimum(Calendar.DATE);
		int ld = before.getActualMaximum(Calendar.DATE);
		before.set(Calendar.DAY_OF_MONTH, fd);
		Calendar after = new GregorianCalendar(year, month, dayOfMonth);
		after.set(Calendar.DAY_OF_MONTH, ld);
		return Realm.getDefaultInstance().where(DateInfo.class)
			.between(DateInfo.FIELD_DATE, before.getTime(), after.getTime())
			.findAll();
	}

	public DateInfo getDateInfo(Date date) {
		return Realm.getDefaultInstance().where(DateInfo.class)
			.equalTo(DateInfo.FIELD_ID, generateId(date))
			.findFirst();
	}

	public void saveDateInfo(final DateInfo dateInfo) {
		Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				Realm.getDefaultInstance().copyToRealmOrUpdate(dateInfo);
			}
		});
	}
	//endregion

	public int generateId(Date date) {
		int year = date.getYear() * 10000;
		int month = date.getMonth() * 100;
		int dayOfMonth = date.getDate();
		return year + month + dayOfMonth;
	}

	private void migrateData(Realm realm) {
		Date d15 = new Date(2017 - 1900, 7 - 1, 15);
		Date d16 = new Date(2017 - 1900, 7 - 1, 16);
		Date d22 = new Date(2017 - 1900, 7 - 1, 22);
		Date d23 = new Date(2017 - 1900, 7 - 1, 23);
		Date d29 = new Date(2017 - 1900, 7 - 1, 29);

		List<DateInfo> dateInfos = new ArrayList<>();
		dateInfos.add(new DateInfo(d15, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d16, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d22, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d23, DateInfo.Type.DONE));
		dateInfos.add(new DateInfo(d29, DateInfo.Type.DONE));

		Student ilham = new Student("Ilham", 6);
		Student silsi = new Student("Silsi", 6);
		Student indah = new Student("Indah", 6);
		Student fitri = new Student("Fitri", 6);
		Student arum = new Student("Arum", 6);
		Student noval = new Student("Noval", 6);
		Student favian = new Student("Favian", 6);
		Student andin = new Student("Andin", 6);
		Student sindi = new Student("Sindi", 6);
		Student velisa = new Student("Velisa", 6);
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
		students.add(favian);
		students.add(andin);
		students.add(sindi);
		students.add(velisa);
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
		topics.add(new Topic(d29, 6, "Bilangan Pangkat 3"));
		topics.add(new Topic(d15, 9, "Operasi Bilangan Berpangkat"));
		topics.add(new Topic(d16, 9, "Bilangan Baku"));
		topics.add(new Topic(d22, 9, "Kesebangunan"));
		topics.add(new Topic(d23, 9, "Kesebangunan"));
		topics.add(new Topic(d29, 9, "Kekongruenan"));
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
		presents.add(new Present(d29, ilham));
		presents.add(new Present(d29, fitri));
		presents.add(new Present(d29, arum));
		presents.add(new Present(d29, noval));
		presents.add(new Present(d29, favian));
		presents.add(new Present(d29, andin));
		presents.add(new Present(d29, silsi));
		presents.add(new Present(d29, indah));
		presents.add(new Present(d29, sindi));
		presents.add(new Present(d29, velisa));

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
		presents.add(new Present(d29, irfan));
		presents.add(new Present(d29, habib));
		presents.add(new Present(d29, tegar));
		presents.add(new Present(d29, bayu));
		presents.add(new Present(d29, ainun));

		presents.add(new Present(d15, ikhsan));
		presents.add(new Present(d16, ikhsan));
		presents.add(new Present(d22, ikhsan));

		List<Payment> payments = new ArrayList<>();
		payments.add(new Payment(d22, fitri));
		payments.add(new Payment(d22, ikhsan));
		payments.add(new Payment(d29, favian));
		payments.add(new Payment(d29, arum));

		realm.copyToRealmOrUpdate(dateInfos);
		realm.copyToRealmOrUpdate(presents);
		realm.copyToRealmOrUpdate(students);
		realm.copyToRealmOrUpdate(topics);
		realm.copyToRealmOrUpdate(payments);
	}


}
