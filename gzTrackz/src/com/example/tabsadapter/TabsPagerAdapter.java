package com.example.tabsadapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tabs.HomeFragment;
import com.example.tabs.StandupsFragment;
import com.example.tabs.TimestampsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new TimestampsFragment();
		case 1:
			// Games fragment activity			
			return new HomeFragment();
		case 2:
			// Movies fragment activity
			return new StandupsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
