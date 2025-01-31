package jobhunter.jobhunter.service;

import jobhunter.jobhunter.dto.job.JobConverter;
import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.Skill;
import jobhunter.jobhunter.dto.job.JobRequest;
import jobhunter.jobhunter.dto.job.JobResponse;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.repository.JobRepository;
import jobhunter.jobhunter.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobConverter jobConverter;

    @Autowired
    private JobRepository jobRepository;

    public List<Job> handleGetJobBySkillIn(List<Skill> skills){
        return  jobRepository.findBySkillsIn(skills);
    }

    public List<Job> handleGetAllJob(){
        return jobRepository.findAll();
    }

    public JobResponse handleCreateJob(JobRequest jobRequest) {
        Job job = jobConverter.toJob(jobRequest);
        if(jobRequest.getSkills() != null){
           List<Long> ids = jobRequest.getSkills().stream()
                   .map(skill -> skill.getId()).toList();
           List<Skill> skills = new ArrayList<>();
           for(Long id : ids){
               Optional<Skill> skill = skillRepository.findById(id);
               if(skill.isPresent()){
                   skills.add(skill.get());
               }
           }
           job.setSkills(skills);
        }

        jobRepository.save(job);
        JobResponse jobResponse = jobConverter.toJobResponse(job);
        return jobResponse;
    }

    public JobResponse handleGetJobById(long id){
        Optional<Job> job = jobRepository.findById(id);
        if(!job.isPresent()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return jobConverter.toJobResponse(job.get());
    }

    public ResultPagination handleGetJobs(Specification<Job> spec, Pageable pageable){
        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        List<JobResponse> jobResponses = jobPage.getContent().stream()
                .map(job -> jobConverter.toJobResponse(job)).toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .pages(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .total(jobPage.getTotalElements())
                        .pages(jobPage.getTotalPages())
                        .build())
                .result(jobResponses)
                .build();
    }

    public JobResponse handleUpdateJob(JobRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getId());
        if (!job.isPresent()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        if(jobRequest.getSkills() != null){
            List<Long> skillIds = jobRequest.getSkills()
                    .stream().map(skill -> skill.getId())
                    .toList();
            List<Skill> skills = skillRepository.findByIdIn(skillIds);
            jobRequest.setSkills(skills);
        }
        Job currentJob = jobConverter.toJob(jobRequest);
        jobRepository.save(currentJob);
        return jobConverter.toJobResponse(currentJob);
    }

    public void handleDeleteJob(long id){
        jobRepository.deleteById(id);
    }
}
