package jobhunter.jobhunter.dto.resume;

import jobhunter.jobhunter.dto.user.UserConverter;
import jobhunter.jobhunter.domain.Resume;
import jobhunter.jobhunter.dto.job.JobConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumeConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JobConverter jobConverter;

    @Autowired
    private UserConverter userConverter;

    public Resume toResume(ResumeRequest resumeRequest) {
        return modelMapper.map(resumeRequest, Resume.class);
    }


    public ResumeResponse toResumeResponse(Resume resume) {
        ResumeResponse resumeResponse = modelMapper.map(resume, ResumeResponse.class);
        resumeResponse.setJob(jobConverter.toJobResponse(resume.getJob()));
        resumeResponse.setUser(userConverter.toUserResponse(resume.getUser()));
        return resumeResponse;
    }
}
