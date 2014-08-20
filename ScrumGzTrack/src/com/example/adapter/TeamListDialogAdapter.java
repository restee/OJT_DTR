package com.example.adapter;

import com.example.scrumgztrack.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamListDialogAdapter extends BaseAdapter {
	private String[] teamList;
	private Context context;
	
	public TeamListDialogAdapter(Context context,String[] teamList){
		this.context = context;
		this.teamList = teamList;
	}
	@Override
	public int getCount() {
		return teamList.length;
	}

	@Override
	public Object getItem(int arg0) {
		return teamList[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {

		

		if (convertView == null) {
			convertView = ((LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.dialog_teamlist_item, parent, false);			
		}
		TextView teamTXT = (TextView) convertView.findViewById(R.id.txtTeamName); 
		teamTXT.setText(teamList[position]);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "Nexa Light.otf");
		teamTXT.setTypeface(tf);
		
		return convertView;

	}
}
