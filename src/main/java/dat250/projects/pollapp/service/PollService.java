package dat250.projects.pollapp.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dat250.projects.pollapp.model.Poll;
import dat250.projects.pollapp.model.User;
import dat250.projects.pollapp.model.Vote;
import dat250.projects.pollapp.model.VoteOption;
import dat250.projects.pollapp.repository.PollRepository;
import dat250.projects.pollapp.repository.UserRepository;
import dat250.projects.pollapp.repository.VoteOptionRepository;
import dat250.projects.pollapp.repository.VoteRepository;
import jakarta.transaction.Transactional;

/**
 * PollService for usage as a midleman of controller
 * 
 * @author Biljan
 */
@Service
public class PollService {

//	private final Map<String, User> users = new HashMap<>();
//	private final Map<String, Poll> polls = new HashMap<>();
	
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private PollRepository pollrepo;
	
	@Autowired
	private VoteOptionRepository voteoptionrepo;
	
	@Autowired
	private VoteRepository voterepo;

	/**
	 * Creates user
	 *
	 * @param name
	 * @param email
	 * @return User
	 * 
	 */
	 @Transactional
	public User createUser(String name, String email) {
		User user = new User();
		user.setEmail(email);
		user.setUsername(name);
			if(userrepo.findByEmail(email).isPresent()) {
				throw new RuntimeException("User with email " + email + " already exists");
			}
		
		userrepo.save(user);
		return user;

	}
	
	/**
	 * Find user
	 *
	 * @param email
	 * @return User
	 * 
	 */
	public Optional<User> findUser(String email) {
		
		return userrepo.findByEmail(email);

	}
	
	

	/**
	 * Give all user
	 *
	 * @return Collection<User>
	 * 
	 */
	public Collection<User> listUsers() {
		
		return (Collection<User>) userrepo.findAll();
	}

	/**
	 * Give all polls
	 *
	 * @return Collection<Poll>
	 * 
	 */
	public Collection<Poll> listPolls() {
		return (Collection<Poll>) pollrepo.findAll();
	}

	/**
	 * Give all votes
	 * 
	 * @param user
	 * @return Collection<User>
	 * 
	 */
	public Collection<Vote> listVotes(User user) {
		return user.getVote();
	}

	/**
	 * Creates poll
	 *
	 * @param user
	 * @param question
	 * @param validUntil
	 * @param option
	 * @return Poll
	 * 
	 */
	public Poll createPoll(User user, String question, Instant validUntil, List<String> option) {
		Poll poll = new Poll();
		poll.setCreatedby(user);
		poll.setQuestion(question);
		poll.setPublishedAt(Instant.now());
		poll.setValidUntil(validUntil);
		List<VoteOption> voteoptionlist = new ArrayList<>();
		for (int i = 0; i < option.size(); i++) {
			VoteOption voteoption = new VoteOption();
			voteoption.setCaption(option.get(i));
			voteoption.setPresentationOrder(i + 1);
			voteoption.setPoll(poll);
			voteoptionlist.add(voteoption);
		}
		poll.setVoteoption(voteoptionlist);
		user.getPoll().add(poll);
	
		
		
		pollrepo.save(poll);
		return poll;
	}

	/**
	 * Delete poll
	 *
	 *
	 * @param poll
	 * 
	 * 
	 */
	@Transactional
	public void deletePoll(Poll poll) {
		
	    

	    // Delete the poll with  cascade and orphanRemoval and also mapping from user to poll
	    pollrepo.delete(poll);

		
	}

	/**
	 * Vote for an option
	 *
	 * @param user
	 * @param poll
	 * @param index
	 * @return Vote
	 * 
	 */
	@Transactional
	public Vote vote(User user, Poll poll, int index) {
		// Fetch managed entities
	    User managedUser = userrepo.findById(user.getId()).orElseThrow();
	    Poll managedPoll = pollrepo.findById(poll.getId()).orElseThrow();
	    VoteOption managedOption = managedPoll.getVoteoption().get(index);

	    // Find existing vote
	    Optional<Vote> existingVote = managedUser.getVote().stream()
	            .filter(v -> managedPoll.getVoteoption().contains(v.getVotedOn()))
	            .findFirst();

	    if (existingVote.isPresent()) {
	        Vote voteToDelete = existingVote.get();

	        managedUser.getVote().remove(voteToDelete);
	        voteToDelete.getVotedOn().getVote().remove(voteToDelete);

	        
	        voterepo.delete(voteToDelete);
	    }

	   
	    Vote vote = new Vote();
	    vote.setPublishedAt(Instant.now());
	    vote.setUser(managedUser);
	    vote.setVotedOn(managedOption);

	    // Add to collections
	    managedUser.getVote().add(vote);
	    managedOption.getVote().add(vote);

	    // Save vote
	    return voterepo.save(vote);

	}

}
