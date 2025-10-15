package dat250.projects.pollapp.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Poll object
 * 
 * @author Biljan
 */
public class Poll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	private String question;
	private Instant publishedAt;
	private Instant validUntil;

	@JsonBackReference("created")
	@OneToMany
	private User createdby;

	@JsonManagedReference("options")
	@ManyToOne
	private List<VoteOption> voteoption = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public Instant getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Instant validUntil) {
		this.validUntil = validUntil;
	}

	public User getCreatedby() {
		return createdby;
	}

	public void setCreatedby(User createdby) {
		this.createdby = createdby;
	}

	public List<VoteOption> getVoteoption() {
		return voteoption;
	}

	public void setVoteoption(List<VoteOption> voteoption) {
		this.voteoption = voteoption;
	}

}
