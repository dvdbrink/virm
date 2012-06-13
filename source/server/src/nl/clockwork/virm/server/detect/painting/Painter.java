package nl.clockwork.virm.server.detect.painting;

public class Painter {
	public long id;
	public String firstName;
	public String lastName;
	
	public Painter(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public long getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
}
