package dat250.projects.pollapp.model;

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
 * VoteOption object
 * 
 * @author Biljan
 */
public class VoteOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	private String caption;
	private int presentationOrder;

	@ManyToOne
	@JsonManagedReference("votedoption")
	private List<Vote> vote = new ArrayList<>();

	@OneToMany
	@JsonBackReference("options")
	private Poll poll;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getPresentationOrder() {
		return presentationOrder;
	}

	public void setPresentationOrder(int presentationOrder) {
		this.presentationOrder = presentationOrder;
	}

	public List<Vote> getVote() {
		return vote;
	}

	public void setVote(List<Vote> vote) {
		this.vote = vote;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

}
