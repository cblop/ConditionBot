package com.cblop.conditionbot;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class MainActivity extends ListActivity
{

    public static final String[] EXERCISES = new String[] {"Push-ups", "Squats", "Pull-ups", "Bridges", "Leg Raises", "Handstand Push-ups"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main,EXERCISES));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RepActivity.class);
                intent.putExtra("type", EXERCISES[position]);
                startActivity(intent);
            }
        });
    }
}
