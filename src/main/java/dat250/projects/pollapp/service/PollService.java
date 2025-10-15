package dat250.projects.pollapp.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dat250.projects.pollapp.model.Poll;
import dat250.projects.pollapp.model.User;
import dat250.projects.pollapp.model.Vote;
import dat250.projects.pollapp.model.VoteOption;

/**
 * PollService for usage as a midleman of controller
 * 
 * @author Biljan
 */
@Service
public class PollService {

	private final Map<String, User> users = new HashMap<>();
	private final Map<String, Poll> polls = new HashMap<>();

	/**
	 * Creates user
	 *
	 * @param name
	 * @param email
	 * @return User
	 * 
	 */
	public User createUser(String name, String email) {
		User user = new User();
		user.setEmail(email);
		user.setUsername(name);
		users.put(name, user);
		return user;

	}

	/**
	 * Give all user
	 *
	 * @return Collection<User>
	 * 
	 */
	public Collection<User> listUsers() {
		return users.values();
	}

	/**
	 * Give all polls
	 *
	 * @return Collection<Poll>
	 * 
	 */
	public Collection<Poll> listPolls() {
		return polls.values();
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
		polls.put(question, poll);
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
	public void deletePoll(Poll poll) {
		polls.remove(poll.getQuestion());
		users.values().forEach(u -> u.getPoll().remove(poll));
		users.values().forEach(u -> u.getVote().removeIf(v -> poll.getVoteoption().contains(v.getVotedOn())));
	}

	/**
	 * Vote for an option
	 *
	 * @param user
	 * @param poll
	 * @param option
	 * @return Vote
	 * 
	 */
	public Vote vote(User user, Poll poll, VoteOption option) throws Exception {

		// Find any existing vote by this user in this poll
		Optional<Vote> existingVote = user.getVote().stream().filter(v -> poll.getVoteoption().contains(v.getVotedOn()))
				.findFirst();

		// Remove it if present
		existingVote.ifPresent(v -> {
			user.getVote().remove(v);
			v.getVotedOn().getVote().remove(v);
		});
		// Create new vote
		Vote vote = new Vote();
		vote.setPublishedAt(Instant.now());
		vote.setVotedOn(option);
		vote.setUser(user);

		user.getVote().add(vote);

		option.getVote().add(vote);

		return vote;

	}

}
