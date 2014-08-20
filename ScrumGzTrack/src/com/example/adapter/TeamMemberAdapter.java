package com.example.adapter;

import java.util.List;

import com.example.scrumgztrack.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamMemberAdapter extends BaseAdapter {
	private Context context;
	private List<String> teamMembers;
	
	public TeamMemberAdapter(Context context, List<String> teamMembers){
		this.context = context;
		this.teamMembers = teamMembers;		
	}
	
	
	@Override
	public int getCount() {
		return teamMembers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return teamMembers.get(arg0);
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
					R.layout.team_members_item, parent, false);			
		}
		TextView teamTXT = (TextView) convertView.findViewById(R.id.txtTeamMember); 
		teamTXT.setText(teamMembers.get(position));
		
		
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "Nexa Bold.otf");
		teamTXT.setTypeface(tf);
		
		return convertView;

	}
}
