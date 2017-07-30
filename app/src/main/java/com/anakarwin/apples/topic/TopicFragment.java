package com.anakarwin.apples.topic;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class TopicFragment extends Fragment {

	private RecyclerView listContainer;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private List<Topic> topics;

	private Dialog addDialog, delDialog;
	private TextInputLayout levelLayout, contentLayout, dateLayout, delLevelLayout, delDateLayout;
	private TextInputEditText levelET, dateET, contentET, delLevelET, delDateET;
	private Button doneBtn, delDoneBtn;

	public TopicFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		initData();
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_topic, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		setupAdapter();
		setupDialog();
		setupDeleteDialog();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_topic, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_add_topic:
				addDialog.show();
				break;
			case R.id.action_delete_topic:
				delDialog.show();
				break;
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (addDialog != null) {
			addDialog.dismiss();
		}
		if (delDialog != null) {
			delDialog.dismiss();
		}
	}

	private void setupDeleteDialog() {
		delDialog = new Dialog(getContext());
		delDialog.setCancelable(true);
		delDialog.setContentView(R.layout.dialog_delete_topic);
		delDialog.setCanceledOnTouchOutside(true);
		delDialog.setTitle(R.string.topic_add_title);
		delLevelLayout = (TextInputLayout) delDialog.findViewById(R.id.levelLayout);
		delDateLayout = (TextInputLayout) delDialog.findViewById(R.id.dateLayout);
		delLevelET = (TextInputEditText) delDialog.findViewById(R.id.levelET);
		delDateET = (TextInputEditText) delDialog.findViewById(R.id.dateET);
		delDoneBtn = (Button) delDialog.findViewById(R.id.doneBtn);
		delDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String dateString = delDateET.getText().toString();
				Date date;
				try {
					date = DAO.dateFormatter.parse(dateString);
					delDateLayout.setError(null);
				} catch (ParseException e) {
					delDateLayout.setError(getContext().getString(R.string.topic_error_input));
					return;
				}
				int level;
				try {
					String levelString = delLevelET.getText().toString();
					level = Integer.parseInt(levelString);
					delLevelLayout.setError(null);
				} catch (Exception e) {
					delLevelLayout.setError(getContext().getString(R.string.topic_error_input));
					return;
				}
				Topic topic = DAO.getInstance().getTopic(date, level);
				if (topic == null) {
					delDateLayout.setError(getContext().getString(R.string.topic_error_not_exists));
					delLevelLayout.setError(getContext().getString(R.string.topic_error_not_exists));
				} else if (date != null && level > 0) {
					DAO.getInstance().deleteTopic(topic);
					initData();
					setupAdapter();
					delDateLayout.setError(null);
					delLevelLayout.setError(null);
					delDialog.dismiss();
				}
			}
		});
	}

	private void setupDialog() {
		addDialog = new Dialog(getContext());
		addDialog.setCancelable(true);
		addDialog.setContentView(R.layout.dialog_add_topic);
		addDialog.setCanceledOnTouchOutside(true);
		addDialog.setTitle(R.string.topic_add_title);
		levelLayout = (TextInputLayout) addDialog.findViewById(R.id.levelLayout);
		dateLayout = (TextInputLayout) addDialog.findViewById(R.id.dateLayout);
		contentLayout = (TextInputLayout) addDialog.findViewById(R.id.contentLayout);
		levelET = (TextInputEditText) addDialog.findViewById(R.id.levelET);
		dateET = (TextInputEditText) addDialog.findViewById(R.id.dateET);
		contentET = (TextInputEditText) addDialog.findViewById(R.id.contentET);
		doneBtn = (Button) addDialog.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String dateString = dateET.getText().toString();
				Date date;
				try {
					date = DAO.dateFormatter.parse(dateString);
					dateLayout.setError(null);
				} catch (ParseException e) {
					dateLayout.setError(getContext().getString(R.string.topic_error_input));
					return;
				}
				int level;
				try {
					String levelString = levelET.getText().toString();
					level = Integer.parseInt(levelString);
					levelLayout.setError(null);
				} catch (Exception e) {
					levelLayout.setError(getContext().getString(R.string.topic_error_input));
					return;
				}
				if (contentET.getText().toString().trim().isEmpty()) {
					contentLayout.setError(getContext().getString(R.string.topic_error_input));
					return;
				}
				if (date != null && level > 0) {
					Topic topic = DAO.getInstance().getTopic(date, level);
					if (topic != null) {
						dateLayout.setError(getContext().getString(R.string.topic_error_already_exists));
						levelLayout.setError(getContext().getString(R.string.topic_error_already_exists));
					} else {
						DAO.getInstance().addTopic(new Topic(date, level, contentET.getText().toString()));
						initData();
						setupAdapter();
						levelLayout.setError(null);
						dateLayout.setError(null);
						contentLayout.setError(null);
						addDialog.dismiss();
					}

				}

			}
		});
	}

	private void setupAdapter() {
		adapter = new TopicAdapter(topics);
		listContainer.setAdapter(adapter);
	}


	private void initData() {
		topics = DAO.getInstance().getTopics();
		if (topics == null) {
			topics = new ArrayList<>();
		}
	}
}
