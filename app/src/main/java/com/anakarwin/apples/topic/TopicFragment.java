package com.anakarwin.apples.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Topic;

import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class TopicFragment extends Fragment {

	private RecyclerView listContainer;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private List<Topic> topics;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		initData();
		View view = inflater.inflate(R.layout.fragment_topic, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		if (topics != null) {
			adapter = new TopicAdapter(topics);
		}
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setAdapter(adapter);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		return view;
	}

	private void initData() {
		topics = DAO.getInstance().getTopics();
	}
}
