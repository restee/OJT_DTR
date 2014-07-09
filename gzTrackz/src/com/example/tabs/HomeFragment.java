package com.example.tabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

import com.example.gztrackz.R;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
	
		TextView timeTxt = (TextView) rootView.findViewById(R.id.timeTxt);
		TextView dateTxt = (TextView) rootView.findViewById(R.id.dateTxt);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Walkway_Bold.ttf");
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "CODE Bold.otf");
	    timeTxt.setTypeface(tf);
	    dateTxt.setTypeface(tf2);
		return rootView;
	}
}
