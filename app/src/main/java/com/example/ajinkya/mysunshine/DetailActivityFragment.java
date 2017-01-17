package com.example.ajinkya.mysunshine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Intent intent = getActivity().getIntent(); // get the activity (DetailActivity) that launched the fragment (DAFrag) and then get the intent
        // possible to just use getIntent directly inside DetailActivity but in its fragment, this is how to do it
        // http://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if ( intent != null && intent.hasExtra(Intent.EXTRA_TEXT) )
        {
            String forecast_detail = intent.getStringExtra(Intent.EXTRA_TEXT);
            ( (TextView) rootView.findViewById(R.id.detail_text) ).setText(forecast_detail);
        }

        return rootView;
    }

}
