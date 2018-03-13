package com.jamper.searchingspinner.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.jamper.searchingspinner.Logic.CallBacks;
import com.jamper.searchingspinner.Logic.OnItemSelected;
import com.jamper.searchingspinner.R;

import java.util.ArrayList;

/**
 * Created by jamper on 12/8/2017.
 */

public class SearchingSpinner extends android.support.v7.widget.AppCompatEditText implements CallBacks {
    private ArrayList<String> arrayList;
    private AttributeSet attrs;
    private int defStyleAttr;
    private OnItemSelected onItemSelected;
    private int itemPosition;
    private InputType inputType;

    /**
     * Retrieve the Attributes
     **/
    private CharSequence[] entries;
    private String title;
    private boolean itemOnClickDismissDialog;
    private boolean localEntriesAddable;
    private boolean acceptLocalEntries;
    private int dialogInputType;


    public SearchingSpinner(Context context) {
        super(context);
        customiseView();
    }


    public SearchingSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        customiseView();
    }


    public SearchingSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        customiseView();
    }


    /**
     * Set OnClickListener to our customised EditText
     **/
    private void setOnClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Set OnLongClickListener to our customised EditText
     **/
    private void setOnLongClickListener() {
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog();
                return false;
            }
        });
    }


    /**
     * Customise the View from here
     **/
    private void customiseView() {
        arrayList = new ArrayList<>();
        itemPosition = -1;
        this.dialogInputType = InputType.TYPE_CLASS_TEXT;

        this.setFocusable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_default_holo_light));
        } else
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.spinner_default_holo_light, 0);

        this.setClickable(true);
        this.setPadding(10, 10, 10, 10);
        this.setTextSize(14f);
        getAttributes();
        setOnLongClickListener();
        setOnClickListener();
    }


    /**
     * Get the attributes specified in attrs.xml using the names we included
     */
    private void getAttributes() {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SearchingSpinner, defStyleAttr, 0);
        try {
            title = a.getString(R.styleable.SearchingSpinner_title); //Get the Title to the Spinner Dialog
            entries = a.getTextArray(R.styleable.SearchingSpinner_android_entries); //Get the arrayList to be set to the Spinner
            localEntriesAddable = a.getBoolean(R.styleable.SearchingSpinner_localEntriesAddable, false); //Should own values entered be saved and added to existing List
            acceptLocalEntries = a.getBoolean(R.styleable.SearchingSpinner_acceptLocalEntries, true); //Can User enter his own values
            itemOnClickDismissDialog = a.getBoolean(R.styleable.SearchingSpinner_itemOnClickDismissDialog,
                    true); //Get the boolean value to indicate whether to dismiss the Dialog or not
            // when user selects item from the list
            dialogInputType = a.getInteger(R.styleable.SearchingSpinner_dialogInputType,InputType.TYPE_CLASS_TEXT);
            addEntries(null);
            setDefaultItem(null);
            setDialogInputType(dialogInputType);

        } finally {
            a.recycle();
        }
    }


    /**
     * Add the Entries which is the String-Array from the XML or by JAVA programmatically to the Customised Spinner
     **/
    public void addEntries(final ArrayList<String> list) {
        arrayList = new ArrayList<>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (entries != null) {
                        for (CharSequence sq : entries) {
                            arrayList.add(sq.toString());
                        }
                    }

                    if (list != null) {
                        arrayList = new ArrayList<>();
                        setDefaultItem(list);
                        arrayList.addAll(list);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Set a default item to the customised Spinner
     **/
    private void setDefaultItem(ArrayList<String> list) {
        if (entries != null) {
            if (entries.length > 0)
                this.setText(entries[0].toString());
        }

        if (list != null) {
            if (list.size() > 0)
                this.setText(list.get(0));
        }
    }


    public void setDialogInputType(int inputType){
      this.dialogInputType = inputType;
    }


    /**
     * Show the Dialog when user clicks on the customised Spinner
     **/
    private void showDialog() {
        SearchSpinner sp = new SearchSpinner(getContext());
        sp.setTitle(getTitle());
        sp.setSpinnerList(arrayList);
        sp.setItemOnClickDismissDialog(itemOnClickDismissDialog);
        sp.setAcceptLocalEntries(acceptLocalEntries);
        sp.setLocalEntriesAddable(localEntriesAddable);
        sp.setInputType(dialogInputType);
        sp.onItemSelectedListener(new SearchSpinner.onItemOnClick() {
            @Override
            public void getItem(String item, int itemPosition) {
                SearchingSpinner.this.setText(item);
                SearchingSpinner.this.itemPosition = itemPosition;
                setOnItemSelectedListener(onItemSelected);
            }
        });
        sp.show();
    }


    /**
     * Set the title of the search dialog
     *
     * @param title of the dialog to be set
     **/
    public void setTitle(String title) {
        if (title != null)
            this.title = title;
    }

    /**
     * Return the title of the dialog
     *
     * @return title of the dialog
     **/
    private String getTitle() {
        if (title == null || TextUtils.isEmpty(title))
            return "";
        return title;
    }


    /**
     * Dismiss the dialog when an item is selected from the list
     *
     * @param itemOnClickDismissDialog This value should be either TRUE or FALSE
     *                                 <p>TRUE indicates dismissal of dialog</p>
     *                                 <p>FALSE indicates Otherwise</p>
     **/
    public void setItemOnClickDismissDialog(boolean itemOnClickDismissDialog) {
        this.itemOnClickDismissDialog = itemOnClickDismissDialog;
    }


    /**
     * Dismiss the dialog when an item is selected from the list
     *
     * @param localEntriesAddable This value should be either TRUE or FALSE
     *                            <p>TRUE indicates adding user's entries to the already existing list</p>
     *                            <p>FALSE indicates Otherwise</p>
     **/
    public void setLocalEntriesAddable(boolean localEntriesAddable) {
        this.localEntriesAddable = localEntriesAddable;
    }

    /**
     * Dismiss the dialog when an item is selected from the list
     *
     * @param acceptLocalEntries This value should be either TRUE or FALSE
     *                           <p>TRUE indicates allowing user's entries to be accepted
     *                           as a selected item even if it is not found in the list</p>
     *                           <p>FALSE indicates Otherwise</p>
     **/
    public void setAcceptLocalEntries(boolean acceptLocalEntries) {
        this.acceptLocalEntries = acceptLocalEntries;
    }


    /**
     * Return the current item selected and set to the spinner
     *
     * @return selecetedItem
     **/
    public String getSelectedItem() {
        return this.getText().toString();
    }

    /**
     * Return the current item position selected and set to the spinner
     *
     * @return selecetedItemPosition
     **/
    public int getItemPosition() {
        return itemPosition;
    }


    /**
     * set the onItemSelectedListener to the spinner
     *
     * @param onItemSelected a callBack to register the itemSelectedListener
     **/
    @Override
    public void setOnItemSelectedListener(OnItemSelected onItemSelected) {
        //Check if No item is selected by using the itemPosition value(Thus when its equal to ==  -1)
        if (onItemSelected != null) {
            SearchingSpinner.this.onItemSelected = onItemSelected;
            if (getItemPosition() != -1) {
                onItemSelected.onItemSelected(getSelectedItem(), getItemPosition());
            }
        }
    }


}
