package com.cblop.conditionbot;


import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class RepActivity extends ListActivity {
    private LogDataSource datasource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reps);

        Bundle extras = getIntent().getExtras();
        String type = "";

        if (extras != null) {
            type = extras.getString("type");
        }

        datasource = new LogDataSource(this);
        datasource.open();

        //List<Log> values = datasource.getAllLogs();
        List<Log> values = datasource.getLogsByType(type);

        // Use the SimpleCursorAdapter to show the elements in a ListView
        ArrayAdapter<Log> adapter = new ArrayAdapter<Log>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml

    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Log> adapter = (ArrayAdapter<Log>) getListAdapter();
        Log log = null;
        switch (view.getId()) {
            case R.id.add:
                String[] names = new String[] { "Name A", "Name B", "Name C"};
                String[] types = new String[] {"Push-ups", "Squats", "Pull-ups", "Bridges", "Leg Raises", "Handstand Push-ups"};
                int nextInt = new Random().nextInt(3);
                int nextInt2 = new Random().nextInt(6);
                // Save to DB
                log = datasource.createLog(names[nextInt], types[nextInt2], "Yes", 25);
                adapter.add(log);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    log = (Log) getListAdapter().getItem(0);
                    datasource.deleteLog(log);
                    adapter.remove(log);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }


}
