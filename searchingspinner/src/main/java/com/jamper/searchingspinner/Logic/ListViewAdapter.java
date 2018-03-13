package com.jamper.searchingspinner.Logic;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamper.searchingspinner.R;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> originalList;
    Context mContext;
    TextView textView;

    public ListViewAdapter(Context context, int resId, ArrayList<String> mainList) {
        super(context, resId, mainList);
        this.mContext = context;
        originalList = mainList;
    }


    public class ViewHolder {
        TextView item;
    }


    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;

        if (v == null) {
            holder = new ViewHolder();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            textView = new TextView(mContext);
            textView.setTextSize(15f);
            textView.setTextColor(mContext.getResources().getColor(R.color.black));

            params.setMargins(10, 10, 10, 10);
            params.gravity = Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(params);


            LinearLayout rel = new LinearLayout(mContext);
            rel.setOrientation(LinearLayout.HORIZONTAL);
            rel.addView(textView);
            rel.setPadding(10, 16, 10, 16);

            v = rel;
            holder.item = textView;

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        try {
            holder.item.setText(originalList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }



}
