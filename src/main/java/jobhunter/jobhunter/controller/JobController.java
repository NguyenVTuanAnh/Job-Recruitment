package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.dto.job.JobRequest;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.job.JobResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@RequestBody JobRequest jobRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<JobResponse>builder()
                        .error(null)
                        .data(jobService.handleCreateJob(jobRequest))
                        .message("job created")
                        .statusCode("201")
                        .build());
    }


    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable("id") long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<JobResponse>builder()
                        .message("get job by id")
                        .error(null)
                        .statusCode("200")
                        .data(jobService.handleGetJobById(id))
                        .build());
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<ResultPagination>> getJobs(
            @Filter Specification<Job> spec,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .statusCode("200")
                        .message("all job")
                        .error(null)
                        .data(jobService.handleGetJobs(spec, pageable))
                        .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@RequestBody JobRequest jobRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<JobResponse>builder()
                        .statusCode("200")
                        .message("all job")
                        .error(null)
                        .data(jobService.handleUpdateJob(jobRequest))
                        .build());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable("id") long id){
        jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .data(null)
                        .error(null)
                        .message("deleted a job")
                        .statusCode("200")
                        .build());
    }
}
