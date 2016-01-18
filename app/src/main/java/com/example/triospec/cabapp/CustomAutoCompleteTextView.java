package com.example.triospec.cabapp;

import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.content.Context;
import android.util.AttributeSet;
import java.util.HashMap;

/**
 * Created by Cortex Hub on 1/9/2016.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
    public CustomAutoCompleteTextView(Context context,AttributeSet attrs){
        super(context,attrs);
    }
    /** Returns the places Description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem){
        /** Each item in the autocomplete suggestion list is a hashmap object */
        Log.v("selectedItem",""+selectedItem);
       HashMap<String, String>hm = (HashMap<String,String>)selectedItem;
        return hm.get("description");
       // return (CharSequence)selectedItem;
    }
}
