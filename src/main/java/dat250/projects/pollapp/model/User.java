package dat250.projects.pollapp.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * User object
 * 
 * @author Biljan
 */
@Entity
@Table(name = "app_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	
private String  username;
private String email;
private String password;


@OneToMany( mappedBy = "createdby",orphanRemoval = true)
@JsonManagedReference("created")
private  List<Poll> poll = new ArrayList<>();;

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference("voted")
private  List<Vote> vote =new ArrayList<>();;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPassword() { return password; }

public void setPassword(String password) { this.password = password; }

public List<Poll> getPoll() {
	return poll;
}

public void setPoll(List<Poll> poll) {
	this.poll = poll;
}

public List<Vote> getVote() {
	return vote;
}

public void setVote(List<Vote> vote) {
	this.vote = vote;
}


}
