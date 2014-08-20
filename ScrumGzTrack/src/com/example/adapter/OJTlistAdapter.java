package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrumgztrack.CreateTeamDialog;
import com.example.scrumgztrack.Person;
import com.example.scrumgztrack.R;
import com.example.tabs.OJTlistFragment;
import com.example.tabs.TeamListFragment;

public class OJTlistAdapter extends BaseAdapter {
	private List<Person> resultList;
	private Context context;
	private String email;

	public OJTlistAdapter(Context context, List<Person> resultList,String email) {
		this.resultList = resultList;
		this.context = context;
		this.email=email;
	}
	
	@Override
	public int getCount() {
		return resultList.size();
	}


	@Override
	public Object getItem(int arg0) {

		return resultList.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int personAtCurrentPosition, View convertView,
			ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			convertView = ((LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.ojtlist_item, parent, false);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_nameTxt.setText(resultList.get(personAtCurrentPosition)
				.getFirstName()
				+ " "
				+ resultList.get(personAtCurrentPosition).getLastName());
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "Nexa Light.otf");
		holder.tv_nameTxt.setTypeface(tf);

		holder.iv_rowSelected.setOnClickListener(new rowClickListener(resultList.get(personAtCurrentPosition).getEmail(),resultList.get(personAtCurrentPosition).getTeamName(),personAtCurrentPosition));

		return convertView;

	}

	private class ViewHolder {

		ImageView iv_rowSelected;
		TextView tv_nameTxt;

		public ViewHolder(View convertView) {

			iv_rowSelected = (ImageView) convertView
					.findViewById(R.id.iv_rowSelected);
			tv_nameTxt = (TextView) convertView.findViewById(R.id.tv_ojtName);

		}
	}

	private class rowClickListener implements View.OnClickListener {
		String ojtEmail,team;
		int position;
		public rowClickListener(String ojtEmail,String team,int position) {
			this.ojtEmail = ojtEmail;
			this.team=team;
			this.position = position;
		}
		
		@Override
		public void onClick(View selectedView) {
			PopupMenu popup = new PopupMenu(context, selectedView);
			MenuInflater inflater = popup.getMenuInflater();
			if(team.compareToIgnoreCase("null")!=0){				
				inflater.inflate(R.menu.remove_from_team, popup.getMenu());
			}else			
				inflater.inflate(R.menu.add_to_team, popup.getMenu());
			
			popup.show();
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {			
				public boolean onMenuItemClick(MenuItem item) {					
					if(item.toString().compareToIgnoreCase("Create Team")==0){
						Intent i = new Intent();
						i.putExtra("email",email);
						i.setAction(OJTlistFragment.CREATE_TEAM_BROADCAST);
						context.sendBroadcast(i);
					}else if(item.toString().compareToIgnoreCase("Add to team")==0){
						Intent i = new Intent();
						i.putExtra("action","add");
						i.putExtra("position",position);
						i.putExtra("ojtEmail",ojtEmail);
						i.setAction(TeamListFragment.ADD_TO_TEAM_BROADCAST);
						context.sendBroadcast(i);
					}else if(item.toString().compareToIgnoreCase("Remove from team")==0){
						Intent i = new Intent();
						i.putExtra("action","remove");
						i.putExtra("position",position);
						i.putExtra("ojtEmail",ojtEmail);
						i.setAction(TeamListFragment.ADD_TO_TEAM_BROADCAST);
						context.sendBroadcast(i);
					}
					return false;
				}
			});
		}
	}

}
