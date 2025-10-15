package dat250.projects.pollapp.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Vote object
 * 
 * @author Biljan
 */
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	private Instant publishedAt;

	@JsonBackReference("voted")
	@OneToMany
	private User user;

	@JsonBackReference("votedoption")
	@OneToMany
	private VoteOption votedOn;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VoteOption getVotedOn() {
		return votedOn;
	}

	public void setVotedOn(VoteOption votedOn) {
		this.votedOn = votedOn;
	}

}
