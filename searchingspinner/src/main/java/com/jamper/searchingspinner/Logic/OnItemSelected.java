package com.jamper.searchingspinner.Logic;

/**
 * Created by jamper on 12/11/2017.
 */

public interface OnItemSelected {

    public void onItemSelected(String item, int itemPosition);

    public void onNothingSelected(String item);
}
