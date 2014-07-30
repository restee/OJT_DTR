package list_objects;

public class Standup {
	
	
	private String email;
	private String date;
	private String standup_y;
	private String standup_todo;
	private String problem;
	
	public Standup() {

	}
	
	
	public Standup(String email, String date, String standup_y,
			String standup_todo, String problem) {
		super();
		this.email = email;
		this.date = date;
		this.standup_y = standup_y;
		this.standup_todo = standup_todo;
		this.problem = problem;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStandup_y() {
		return standup_y;
	}
	public void setStandup_y(String standup_y) {
		this.standup_y = standup_y;
	}
	public String getStandup_todo() {
		return standup_todo;
	}
	public void setStandup_todo(String standup_todo) {
		this.standup_todo = standup_todo;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	
	
	
}
