package com.rudetheology.whatcounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentGraph extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final int DAYS_TO_DISPLAY = 14;
    
    private static final int BAR_MULTIPLIER = 30;

    private static final int FAT_MULTIPLIER = 8;
    private static final int CARB_MULTIPLIER = 1;
    private static final int SWEAT_MULTIPLIER = 100;
    private static final int COIN_MULTIPLIER = 5;
    
    private static final String FAT_UNIT = "g";
    private static final String CARB_UNIT = "s";
    private static final String SWEAT_UNIT = "cal";
    private static final String COIN_UNIT = "$";
    
    private static final String DATE_LABEL_FORMAT = "dd\nMMM";

    
    private ListView list;
    private ListAdapter adapter;
    private LinkedHashMap<Date, ArrayList<Integer>> data;
    private ArrayList<Date> dataPositions;

    public FragmentGraph() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);
        list = (ListView)rootView.findViewById(R.id.graph_list_view);
        
        updateData();
        
        adapter = new ListAdapter() {
            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
            }
            
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {                    
            }
            
            @Override
            public boolean isEmpty() {
                return false;
            }
            
            @Override
            public boolean hasStableIds() {
                return true;
            }
            
            @Override
            public int getViewTypeCount() {
                return 1;
            }
            
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final ActivityMain activity = (ActivityMain)getActivity();
                
                ViewGroup listItem = (ViewGroup)inflater.inflate(R.layout.graph_list_item, null);
                TextView dateView = (TextView)listItem.findViewById(R.id.text_date);
                ImageView fatView = (ImageView)listItem.findViewById(R.id.bar_fat);
                TextView fatNumView = (TextView)listItem.findViewById(R.id.num_fat);
                ImageView carbView = (ImageView)listItem.findViewById(R.id.bar_carb);
                TextView carbNumView = (TextView)listItem.findViewById(R.id.num_carb);
                ImageView sweatView = (ImageView)listItem.findViewById(R.id.bar_sweat);
                TextView sweatNumView = (TextView)listItem.findViewById(R.id.num_sweat);
                ImageView coinView = (ImageView)listItem.findViewById(R.id.bar_coin);
                TextView coinNumView = (TextView)listItem.findViewById(R.id.num_coin);
                
                Date date = dataPositions.get(position);
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_LABEL_FORMAT, Locale.US);
                String dateLabelString = sdf.format(date);
                
                dateView.setText(dateLabelString);
                
                Resources res = getResources();
                int fatNum = (int)data.get(date).get(0);
                fatView.setLayoutParams(new LinearLayout.LayoutParams(fatNum * BAR_MULTIPLIER, (int)res.getDimension(R.dimen.bar_height)));
                fatNumView.setText(fatNum * FAT_MULTIPLIER + FAT_UNIT + " " + ActivityMain.FAT);
                int carbNum = (int)data.get(date).get(1);
                carbView.setLayoutParams(new LinearLayout.LayoutParams(carbNum * BAR_MULTIPLIER, (int)res.getDimension(R.dimen.bar_height)));
                carbNumView.setText(carbNum * CARB_MULTIPLIER + CARB_UNIT + " " + ActivityMain.CARBS);
                int sweatNum = (int)data.get(date).get(2);
                sweatView.setLayoutParams(new LinearLayout.LayoutParams(sweatNum * BAR_MULTIPLIER, (int)res.getDimension(R.dimen.bar_height)));
                sweatNumView.setText(sweatNum * SWEAT_MULTIPLIER + SWEAT_UNIT + " " + ActivityMain.SWEAT);
                int coinNum = (int)data.get(date).get(3);
                coinView.setLayoutParams(new LinearLayout.LayoutParams(coinNum * BAR_MULTIPLIER, (int)res.getDimension(R.dimen.bar_height)));
                coinNumView.setText(COIN_UNIT + coinNum * COIN_MULTIPLIER + " " + ActivityMain.COIN);
                
                listItem.setTag(date);
                listItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.setCurrentDate((Date)v.getTag());
                        activity.actionBar.setSelectedNavigationItem(ActivityMain.COUNTERS_TAB);
                    }
                });
                return listItem;
            }
            
            @Override
            public int getItemViewType(int position) {
                return 0;
            }
            
            @Override
            public long getItemId(int position) {
                return position;
            }
            
            @Override
            public Object getItem(int position) {
                return null;
            }
            
            @Override
            public int getCount() {
                return data.size();
            }
            
            @Override
            public boolean isEnabled(int position) {
                return true;
            }
            
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }
        };
        list.setAdapter(adapter);
        
        return rootView;
    }
    
    private void updateData() {
        data = new LinkedHashMap<Date, ArrayList<Integer>>();
        dataPositions = new ArrayList<Date>();
        
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DATE, -DAYS_TO_DISPLAY);
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
                    
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            SimpleDateFormat sdf = new SimpleDateFormat(ActivityMain.DATE_FORMAT, Locale.US);
            String dateString = sdf.format(date);
            SharedPreferences prefs = getActivity().getSharedPreferences(dateString, Context.MODE_PRIVATE);

            ArrayList<Integer> dataRow = new ArrayList<Integer>();
            dataRow.add(prefs.getInt(ActivityMain.FAT, 0));
            dataRow.add(prefs.getInt(ActivityMain.CARBS, 0));
            dataRow.add(prefs.getInt(ActivityMain.SWEAT, 0));
            dataRow.add(prefs.getInt(ActivityMain.COIN, 0));
            
            data.put(date, dataRow);
            dataPositions.add(date);
        }
    }
    
    public void refresh() {
        updateData();
        if (list != null) {
            list.invalidateViews();
            list.smoothScrollToPosition(list.getCount() - 1);
        }

    }
}
