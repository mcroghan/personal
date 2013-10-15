package com.rudetheology.whatcounts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class FragmentCounters extends Fragment {
    private static final int COUNTER_MIN = 0;
    private static final int COUNTER_MAX = 20;
    
    public static final String HEADER_DATE_FORMAT = "EEE, dd MMM yyyy";
    
    private View rootView;
    private NumberPicker numPicker1;
    private NumberPicker numPicker2;
    private NumberPicker numPicker3;
    private NumberPicker numPicker4;
    
    private View dummyFocusView;
    
    public FragmentCounters() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_counters,
                container, false);
        
        final ActivityMain activity = (ActivityMain)getActivity();
        
        dummyFocusView = (View)rootView.findViewById(R.id.dummyFocusView);
        
        numPicker1 = (NumberPicker)rootView.findViewById(R.id.numberPicker1);
        numPicker1.setMinValue(COUNTER_MIN);
        numPicker1.setMaxValue(COUNTER_MAX);
        numPicker2 = (NumberPicker)rootView.findViewById(R.id.numberPicker2);
        numPicker2.setMinValue(COUNTER_MIN);
        numPicker2.setMaxValue(COUNTER_MAX);
        numPicker3 = (NumberPicker)rootView.findViewById(R.id.numberPicker3);
        numPicker3.setMinValue(COUNTER_MIN);
        numPicker3.setMaxValue(COUNTER_MAX);
        numPicker4 = (NumberPicker)rootView.findViewById(R.id.numberPicker4);
        numPicker4.setMinValue(COUNTER_MIN);
        numPicker4.setMaxValue(COUNTER_MAX);
        
        Button submit = (Button)rootView.findViewById(R.id.submitButton);
        submit.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat(ActivityMain.DATE_FORMAT, Locale.US);
                String date = sdf.format(activity.getCurrentDate());
                
                SharedPreferences prefs = FragmentCounters.this.getActivity().getSharedPreferences(date, Context.MODE_PRIVATE);
                Editor editor = prefs.edit();
                
                int currentFat = numPicker1.getValue();
                editor.putInt(ActivityMain.FAT, currentFat);
                
                int currentCarbs = numPicker2.getValue();
                editor.putInt(ActivityMain.CARBS, currentCarbs);
                
                int currentSweat = numPicker3.getValue();
                editor.putInt(ActivityMain.SWEAT, currentSweat);
                
                int currentCoin = numPicker4.getValue();
                editor.putInt(ActivityMain.COIN, currentCoin);
                
                editor.commit();
                
                activity.actionBar.setSelectedNavigationItem(ActivityMain.GRAPH_TAB);
            }
        });
        
        setDate();
        
        return rootView;
    }
    
    public void setDate() {            
        final ActivityMain activity = (ActivityMain)getActivity();
        
        TextView dateView = (TextView)rootView.findViewById(R.id.date);
        Date date = activity.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT, Locale.US);
        String headerDate = sdf.format(date);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ActivityMain.DATE_FORMAT, Locale.US);
        String canonicalDate = sdf2.format(date);
        if (canonicalDate.equals(activity.getToday())) headerDate += " (Today)";
        dateView.setText(headerDate);
        
        SharedPreferences prefs = activity.getSharedPreferences(canonicalDate, Context.MODE_PRIVATE);
        
        int currentFat = prefs.getInt(ActivityMain.FAT, 0);
        numPicker1.setValue(currentFat);
        
        int currentCarbs = prefs.getInt(ActivityMain.CARBS, 0);
        numPicker2.setValue(currentCarbs);
        
        int currentSweat = prefs.getInt(ActivityMain.SWEAT, 0);
        numPicker3.setValue(currentSweat);
        
        int currentCoin = prefs.getInt(ActivityMain.COIN, 0);
        numPicker4.setValue(currentCoin);
    }
    
    public void blurAllTheThings() {
        if (dummyFocusView != null) dummyFocusView.requestFocus();
    }
}

