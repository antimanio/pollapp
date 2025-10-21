package dat250.projects.pollapp.repository;

import org.springframework.data.repository.CrudRepository;

import dat250.projects.pollapp.model.Poll;

public interface PollRepository extends CrudRepository<Poll, Integer>{

}
