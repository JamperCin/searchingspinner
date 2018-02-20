package com.jamper.searchspinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jamper on 12/7/2017.
 */

public class SearchSpinner {
    private Context mContext;
    private ListViewAdapter mAdapter;
    private String title;
    private String spinnerItem;
    private int itemPosition;
    private boolean itemOnClickDismissDialog;
    private boolean localEntriesAddable;
    private boolean acceptLocalEntries;
    private int inputType;

    //Check if item is selected
    private boolean isItemSelected = false;


    private onItemOnClick itemOnClick;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private EditText editText;
    private ListView listView;
    private ImageView imageView;

    /**
     * ArrayList to save the List and the searchList
     **/
    private ArrayList<String> spinnerList;
    private ArrayList<String> filterList;
    private ArrayList<String> trackList;


    /**
     * The Default Constructor to be called from an activity or Fragment
     *
     * @param context, The context to which this constructor is called
     **/
    public SearchSpinner(Context context) {
        this.mContext = context;
        intArrayItemList();
        spinnerItem = null;
        itemPosition = -1;
        itemOnClickDismissDialog = false;
    }


    /**
     * Initialise the ArrayList items and set the values accordingly
     **/
    private void intArrayItemList() {
        spinnerList = new ArrayList<>();
        filterList = new ArrayList<>();
        trackList = new ArrayList<>();
    }

    /**
     * Set the title of the Dialog to be displayed
     **/
    public void setTitle(String title) {
        this.title = title;
    }


     void setAcceptLocalEntries(boolean acceptLocalEntries) {
        this.acceptLocalEntries = acceptLocalEntries;
    }

     void setLocalEntriesAddable(boolean localEntriesAddable) {
        this.localEntriesAddable = localEntriesAddable;
    }


    /**Set the IputType of the Dialog SearchPanel**/
     void setInputType(int inputType){
         this.inputType = inputType;
    }


    /**
     * Set the spinner array items
     **/
    void setSpinnerList(final ArrayList<String> spinnerList) {
        try {
            if (spinnerList != null)
                this.spinnerList.addAll(spinnerList);

            //Add the same List to the trackList in the background
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (spinnerList != null)
                        trackList.addAll(spinnerList);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


     void setItemOnClickDismissDialog(boolean status) {
        this.itemOnClickDismissDialog = status;
    }


    private String getTitle() {
        if (title == null || TextUtils.isEmpty(title))
            return "Default Title";
        return title;
    }

    /**
     * Create and Show the dialog on the screen
     **/
    public void show() {
        try {
            alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle(getTitle());
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onOkButtonOnClick(dialog);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setView(createDialogLayout());
            dialog = alertDialog.show();
            displayList();
            isItemSelected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * OnItem selected Listener to the List items
     **/
    public void onItemSelectedListener(onItemOnClick item) {
        this.itemOnClick = item;
        if (spinnerItem != null)
            if (itemOnClick != null) {
                itemOnClick.getItem(spinnerItem, itemPosition);
                if (dialog != null)
                    dialog.dismiss();

            }
    }


    /**
     * OnClick Listener to the OK Button
     **/
    private void onOkButtonOnClick(DialogInterface dialog) {
        try {
            //If We want to accept user's own entries then take the value entered in the EditText
            if (acceptLocalEntries) {
                spinnerItem = editText.getText().toString();
                if (localEntriesAddable) //If local or user's entries should be saved and added to an existing list, then cache it for later use
                    new Utils(mContext).addEntryToList(spinnerItem);
            } else {
                if (spinnerList.size() == 1)
                    spinnerItem = spinnerList.get(0);
                else
                    Toast.makeText(mContext, "No item selected", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            if (spinnerItem == null || TextUtils.isEmpty(spinnerItem)) {
                Toast.makeText(mContext, "No item selected", Toast.LENGTH_SHORT).show();
            } else {

                this.onItemSelectedListener(itemOnClick);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dynamically create the Dialog Layout and return the Layout
     *
     * @return View; the Layout created is to be returned in the form of a View
     **/
    private View createDialogLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout lin = new LinearLayout(mContext);
        lin.setOrientation(LinearLayout.VERTICAL);
        lin.setPadding(10, 10, 10, 10);

        float scale = mContext.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16 * scale + 0.5f);
        params.setMargins(0, 30, 20, dpAsPixels);
        params.gravity = Gravity.CENTER_VERTICAL;

        lin.setLayoutParams(params);

        lin.addView(getSearchPlate());
        lin.addView(createListView());

        return lin;
    }

    /**
     * Dynamically create the Dialog Search Layout and return the Layout
     *
     * @return View; the Layout created is to be returned in the form of a View
     **/
    private View getSearchPlate() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout rel = new LinearLayout(mContext);
        rel.setOrientation(LinearLayout.HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rel.setBackground(mContext.getResources().getDrawable(R.drawable.box_bg));
        }

        float scale = mContext.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16 * scale + 0.5f);
        params.setMargins(10, 20, 10, dpAsPixels);
        params.gravity = Gravity.CENTER_VERTICAL;

        rel.setLayoutParams(params);
        rel.addView(createSearchIcon());
        rel.addView(createEdtText());

        return rel;
    }

    /**
     * Dynamically create the Dialog Search Icon and return the ImageView
     *
     * @return ImageView; the ImageView created is to be returned
     **/
    private ImageView createSearchIcon() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40, 7f);
        params.gravity = Gravity.CENTER_VERTICAL;

        imageView = new ImageView(mContext);
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_search));
        return imageView;
    }


    /**
     * Dynamically create the Dialog Search EditText and return the EditText
     *
     * @return EditText; the EditText created is to be returned
     **/
    private EditText createEdtText() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);

        params.setMargins(0, 0, 15, 0);
        params.gravity = Gravity.CENTER_VERTICAL;

        editText = new EditText(mContext);
        editText.setHint("Search...");
        editText.setInputType(this.inputType);
        //  editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
        editText.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        editText.setTextSize((float) 15);
        editText.setSingleLine(true);
        editText.setHintTextColor(mContext.getResources().getColor(R.color.transparent_black_hex_3));
        editText.setTextColor(mContext.getResources().getColor(R.color.black));
        editText.setLayoutParams(params);
        setListener(editText);
        return editText;
    }


    /**
     * Dynamically create the Dialog ListView
     *
     * @return ListView; the ListView created is to be returned
     **/
    private ListView createListView() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        float scale = mContext.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16 * scale + 0.5f);
        params.setMargins(0, 10, 0, dpAsPixels);
        params.gravity = Gravity.CENTER_VERTICAL;

        listView = new ListView(mContext);
        listView.setLayoutParams(params);
        listView.setFadingEdgeLength(200);
        listView.setHorizontalFadingEdgeEnabled(true);
        listView.setVerticalFadingEdgeEnabled(true);
        listView.setScrollbarFadingEnabled(true);

        onListViewItemOnClick(listView);

        return listView;
    }


    /**
     * Add Local or user entered values
     **/
    private void addLocalEntries() {
        try {
            //If user or local entries should be added to the existing list, then load them and add them to the new List
            if (acceptLocalEntries && localEntriesAddable)
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (spinnerList != null) {
                                ArrayList<String> tempList = new Utils(mContext).getCachedStringList();
                                if (tempList != null && tempList.size() > 0) {
                                    for (int i = 0; i < tempList.size(); i++) {
                                        if (!trackList.contains(tempList.get(i)))
                                            trackList.add(tempList.get(i));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

    /**
     * Display the List in the ListView
     **/
    private void displayList() {
        try {
            //Add local user entered values
            addLocalEntries();
            mAdapter = new ListViewAdapter(mContext, 0, spinnerList);
            listView.setAdapter(mAdapter);
            listView.setItemsCanFocus(true);
            listView.setTextFilterEnabled(true);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen to any text change when ever user starts typing in the search EditText provided
     **/
    private void setListener(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });
    }

    /**
     * Function to perform searching functionality on the Full List
     **/
    private void performSearch(String entry) {
        try {
            filterList = new ArrayList<>();
            entry = entry.toLowerCase();
            for (int i = 0; i < trackList.size(); i++) {
                if (trackList.get(i).toLowerCase().contains(entry)) {
                    filterList.add(trackList.get(i));
                }
            }

            spinnerList.clear();
            spinnerList.addAll(filterList);

            if (!isItemSelected) {
                spinnerItem = null;
                itemPosition = -1;
            }

            displayList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * OnclickListener to the ListView
     **/
    private void onListViewItemOnClick(ListView listView) {
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        isItemSelected = true;
                        spinnerItem = spinnerList.get(position);
                        itemPosition = position;
                        editText.setText(spinnerItem);

                        if (itemOnClickDismissDialog)
                            SearchSpinner.this.onItemSelectedListener(itemOnClick);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface onItemOnClick {
        void getItem(String item, int itemPosition);
    }

}
