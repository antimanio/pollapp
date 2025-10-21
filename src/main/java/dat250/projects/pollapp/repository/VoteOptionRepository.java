package dat250.projects.pollapp.repository;

import org.springframework.data.repository.CrudRepository;

import dat250.projects.pollapp.model.VoteOption;

public interface VoteOptionRepository extends CrudRepository<VoteOption, Integer>{

}
