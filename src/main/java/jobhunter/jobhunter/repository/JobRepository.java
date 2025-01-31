package jobhunter.jobhunter.repository;

import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.Skill;
import jobhunter.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findByIdIn(List<Long> ids);
    List<Job> findBySkillsIn(List<Skill> skills);
}
