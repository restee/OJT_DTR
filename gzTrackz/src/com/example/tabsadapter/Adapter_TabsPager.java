package com.example.tabsadapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tabs.Fragment_Home;
import com.example.tabs.Fragment_Standups;
import com.example.tabs.Fragment_Timestamps;

public class Adapter_TabsPager extends FragmentPagerAdapter {

	public Adapter_TabsPager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Fragment_Home();
		case 1:
			// Games fragment activity
			return new Fragment_Timestamps();
		case 2:
			// Movies fragment activity
			return new Fragment_Standups();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
