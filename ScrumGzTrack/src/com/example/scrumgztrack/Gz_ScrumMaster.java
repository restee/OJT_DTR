package com.example.scrumgztrack;

public class Gz_ScrumMaster {
	String teamName;

	public Gz_ScrumMaster(String teamName) {
		this.teamName = teamName;
	}
	
	
	public void addToTeam(String email) {
		
		final String addToTeamUrl = "http://gz123.site90.net/add_to_team/default.php";

		String[] addToTeamParams = new String[] { addToTeamUrl, email, teamName };

		new Task_AddRemoveOJT().execute(addToTeamParams);

	}

	public void removeFromTeam(String email) {
		
		final String removeFromTeamUrl = "http://gz123.site90.net/remove_to_team/default.php";
		
		String[] removeFromTeamParams = new String[] { removeFromTeamUrl, email, teamName };

		new Task_AddRemoveOJT().execute(removeFromTeamParams);
	}

}
