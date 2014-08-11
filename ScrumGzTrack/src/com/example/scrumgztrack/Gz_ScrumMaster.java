package com.example.scrumgztrack;

import android.content.Context;
import android.util.Log;

public class Gz_ScrumMaster {
	String teamName;
	Context context;
	
	public Gz_ScrumMaster(String teamName,Context context) {
		this.teamName = teamName;
		this.context = context;
	}
	
	
	public void addToTeam(String email,int position) {
		
		final String addToTeamUrl = "http://gz123.site90.net/add_to_team/default.php";

		String[] addToTeamParams = new String[] { addToTeamUrl, email, teamName };

		new Task_AddRemoveOJT(context,position).execute(addToTeamParams);

		Log.d("POSITION!!!",Integer.toString(position));
	}

	public void removeFromTeam(String email,int position) {
		
		final String removeFromTeamUrl = "http://gz123.site90.net/remove_to_team/default.php";
		
		String[] removeFromTeamParams = new String[] { removeFromTeamUrl, email};

		new Task_AddRemoveOJT(context,position).execute(removeFromTeamParams);
		Log.d("POSITION!!!",Integer.toString(position));
	}

}
