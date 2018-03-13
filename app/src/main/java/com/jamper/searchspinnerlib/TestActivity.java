package com.jamper.searchspinnerlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.jamper.searchingspinner.Logic.OnItemSelected;
import com.jamper.searchingspinner.UI.SearchingSpinner;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    SearchingSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        spinner = findViewById(R.id.me);
        spinner.addEntries(getList());
        spinner.setTitle("Search here");
        spinner.setDialogInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        spinner.setAcceptLocalEntries(true);
        spinner.setLocalEntriesAddable(true);
        spinner.setItemOnClickDismissDialog(true);

        spinner.setOnItemSelectedListener(new OnItemSelected() {
            @Override
            public void onItemSelected(String item, int itemPosition) {
                Toast.makeText(TestActivity.this, item + "\n" + itemPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(String item) {
                Toast.makeText(TestActivity.this, item + " >> ", Toast.LENGTH_SHORT).show();
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
