package jobhunter.jobhunter.dto.job;

import jobhunter.jobhunter.domain.Job;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Job toJob(JobRequest jobRequest) {
        return modelMapper.map(jobRequest, Job.class);
    }


    public JobResponse toJobResponse(Job job) {
        JobResponse jobResponse = modelMapper.map(job, JobResponse.class);
        List<String> skillNames = job.getSkills().stream()
                .map(skill -> skill.getName()).toList();
        jobResponse.setSkills(skillNames);
        return jobResponse;
    }
}
