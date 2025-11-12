package dat250.projects.pollapp.controller;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dat250.projects.pollapp.model.Poll;
import dat250.projects.pollapp.model.User;
import dat250.projects.pollapp.model.Vote;
import dat250.projects.pollapp.model.VoteOption;
import dat250.projects.pollapp.service.PollService;

/**
 * Controller class for REST API
 * 
 * @author Biljan
 */
@RestController
@CrossOrigin
public class PollController {

	@Autowired
	private PollService pollservice;

	
	 private final String instanceName;

	    public PollController() {
	        // Get the container/host name (works in Docker)
	        this.instanceName = System.getenv().getOrDefault("HOSTNAME", "unknown-instance");
	    }

	    @GetMapping("/test")
	    public String getInstanceName() {
	        return "Hello from instance: " + instanceName;
	    }
	
	
	
	
	
	
	/**
	 * Gets all users
	 *
	 * 
	 *
	 * @return User list
	 * 
	 */
	@GetMapping("/users")
	public Collection<User> listUsers() {
		return pollservice.listUsers();
	}

	/**
	 * Gets all polls
	 *
	 * 
	 * @return Poll list
	 * 
	 */
	@GetMapping("/polls")
	public Collection<Poll> listPolls() {
		return pollservice.listPolls();
	}

	/**
	 * Creates a user with service
	 *
	 * @param username
	 * @param email
	 * @return User
	 * 
	 */
	@PostMapping("/user")
	public User createUser(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password) {
		return pollservice.createUser(username, email, password);
	}

	/**
	 * Gets votes from a user
	 *
	 * @param username
	 * @return List of votes from user
	 * 
	 */
	@GetMapping("/votes")
	public Collection<Vote> listVotes(@RequestParam("username") String username) {
		User user = pollservice.listUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst()
				.orElseThrow();
		return pollservice.listVotes(user);
	}

	/**
	 * Creates a poll with service
	 *
	 * @param username
	 * @param question
	 * @param validUntil
	 * @param options
	 * @return Poll
	 * 
	 */
	@PostMapping("/polls")
	public Poll createPoll(@RequestParam("username") String username, @RequestParam("question") String question,
			@RequestParam("validuntil") long validUntil, @RequestBody List<String> options) {

		User user = pollservice.listUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst()
				.orElseThrow();
		return pollservice.createPoll(user, question, Instant.ofEpochMilli(validUntil), options);
	}

	/**
	 * Creates vote for a poll with service
	 *
	 * @param username
	 * @param pollQuestion
	 * @param optionIndex
	 * @return Vote
	 * 
	 */
	@PostMapping("/votes")
	public Vote vote(@RequestParam("username") String username, @RequestParam("pollquestion") String pollQuestion,
			@RequestParam("optionindex") int optionIndex) throws Exception {
		User user = pollservice.listUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst()
				.orElseThrow();
		Poll poll = pollservice.listPolls().stream().filter(p -> p.getQuestion().equals(pollQuestion)).findFirst()
				.orElseThrow();
		return pollservice.vote(user, poll, optionIndex);
	}

	/**
	 * Deletes a poll
	 *
	 * @param question
	 * 
	 */
	@DeleteMapping("/polls")
	public void deletePoll(@RequestParam("question") String question) {
		Optional<Poll> poll = pollservice.listPolls().stream().filter(p -> p.getQuestion().equals(question))
				.findFirst();
		if (!poll.isEmpty()) {
			pollservice.deletePoll(poll.get());
		}

	}
	
	

}
