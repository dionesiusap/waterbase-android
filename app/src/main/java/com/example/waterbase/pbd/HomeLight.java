package com.example.waterbase.pbd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.LinkedList;

import static android.content.Context.ALARM_SERVICE;


public class HomeLight extends Fragment {
    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private LightbulbListAdapter mAdapter;
    public HomeLight() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        mWordList.addLast("Light in living room");
        mWordList.addLast("Light in bedroom");
        mWordList.addLast("Light in bathroom");
        mWordList.addLast("Light in garage");
        mWordList.addLast("A/C in living room");

        return inflater.inflate(R.layout.fragment_home_light, container, false);
        // Put initial data into the word list.
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Button button = (Button) view.findViewById(R.id.alarmToggle);
        mRecyclerView = view.findViewById(R.id.recyclerview);
// Create an adapter and supply the data to be displayed.
        mAdapter = new LightbulbListAdapter(getContext(), mWordList);
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
