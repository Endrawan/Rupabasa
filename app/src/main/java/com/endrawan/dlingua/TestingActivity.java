package com.endrawan.dlingua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 0));
        entries.add(new Entry(6f, 0));
        entries.add(new Entry(12f, 0));
        entries.add(new Entry(18f, 0));
        entries.add(new Entry(9f, 0));

        //PieDataSet dataSet = new PieDataSet(entries, "Calls");
    }
}
