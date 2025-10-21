package dat250.projects.pollapp.repository;

import org.springframework.data.repository.CrudRepository;

import dat250.projects.pollapp.model.Vote;

public interface VoteRepository extends CrudRepository<Vote, Integer>{

}
