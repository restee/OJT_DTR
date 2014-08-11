package com.example.adapter;

import java.util.List;

import com.example.scrumgztrack.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamListAdapter extends BaseAdapter{
	private List<String> teamList;
	private Context context;
	
	public TeamListAdapter(Context context, List<String> teamList){
		this.teamList = teamList;
		this.context = context;
	}
	@Override
	public int getCount() {
		return teamList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return teamList.get(arg0);
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
					R.layout.ojtlist_item, parent, false);			
		}
		TextView teamTXT = (TextView) convertView.findViewById(R.id.tv_ojtName); 
		teamTXT.setText(teamList.get(position));
		

		return convertView;

	}
	
}
