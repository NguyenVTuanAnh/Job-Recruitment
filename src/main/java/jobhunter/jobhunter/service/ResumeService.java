package jobhunter.jobhunter.service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import jobhunter.jobhunter.dto.resume.ResumeConverter;
import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.Resume;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.dto.resume.ResumeRequest;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.resume.ResumeResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.repository.JobRepository;
import jobhunter.jobhunter.repository.ResumeRepository;
import jobhunter.jobhunter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeConverter resumeConverter;

    @Autowired
    private AuthService authService;

    @Autowired
    private FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeResponse handleCreateResume(ResumeRequest resumeRequest) {
        Resume resume = resumeConverter.toResume(resumeRequest);
        Optional<User> userOptional = userRepository.findById(resumeRequest.getUser().getId());
        if (!userOptional.isPresent()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        resume.setUser(userOptional.get());
        Optional<Job> jobOptional = jobRepository.findById(resumeRequest.getJob().getId());
        if (!jobOptional.isPresent()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        resume.setJob(jobOptional.get());
        resumeRepository.save(resume);
        return resumeConverter.toResumeResponse(resume);
    }

    public ResumeResponse handleGetResume(long id) {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (!resumeOptional.isPresent()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return resumeConverter.toResumeResponse(resumeOptional.get());
    }

    public ResultPagination handleGetResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = resumeRepository.findAll(spec, pageable);
        List<ResumeResponse> resumeResponses = resumePage.getContent().stream()
                .map(resume -> resumeConverter.toResumeResponse(resume)).toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .total(resumePage.getTotalElements())
                        .pages(resumePage.getTotalPages())
                        .build())
                .result(resumeResponses)
                .build();
    }

    public ResumeResponse handleUpdateResume(ResumeRequest resumeRequest) {
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeRequest.getId());
        if (!resumeOptional.isPresent()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        Resume resume = resumeOptional.get();
        resume.setStatus(resumeRequest.getStatus());
        return resumeConverter.toResumeResponse(resumeRepository.save(resume));
    }


    public void handleDeleteResume(long id) {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (!resumeOptional.isPresent()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        resumeRepository.deleteById(id);
    }

    public ResultPagination handleGetResumeByUser(Pageable pageable) {
        String username = AuthService.getCurrentUserLogin().isPresent() ?
                AuthService.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("username" + username + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        List<ResumeResponse> resumeResponseList = pageResume.stream()
                .map(resume -> resumeConverter.toResumeResponse(resume))
                .toList();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .total(pageResume.getTotalElements())
                .pages(pageResume.getTotalPages())
                .build();
        return ResultPagination.builder()
                .meta(meta)
                .result(resumeResponseList)
                .build();
    }
}
