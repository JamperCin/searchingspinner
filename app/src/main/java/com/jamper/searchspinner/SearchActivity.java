package com.jamper.searchspinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SearchingSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.me);
        // spinner.addEntries(getList());
        spinner.setTitle("Search here");
        spinner.setAcceptLocalEntries(true);
        spinner.setLocalEntriesAddable(true);
        spinner.setItemOnClickDismissDialog(true);

        spinner.setOnItemSelectedListener(new OnItemSelected() {
            @Override
            public void onItemSelected(String item, int itemPosition) {
                Toast.makeText(SearchActivity.this, item + "\n" + itemPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(String item) {
                Toast.makeText(SearchActivity.this, item + " >> ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Balance");
        list.add("Ghana");
        list.add("Ellen");
        list.add("Forty");
        list.add("Fain Ice");
        list.add("Hello");
        list.add("Ghana");
        list.add("Ellen");
        list.add("Forty");
        return list;
    }

}
