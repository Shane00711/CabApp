package com.example.triospec.cabapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;
import android.app.Activity;

/**
 * Created by Cortex Hub on 1/16/2016.
 */
public class DriverFragment extends Fragment {

    public ToggleButton toggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.driver, container, false);
        return rootView;
    }
}


