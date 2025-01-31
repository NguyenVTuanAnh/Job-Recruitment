package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jobhunter.jobhunter.domain.Resume;
import jobhunter.jobhunter.dto.resume.ResumeRequest;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.resume.ResumeResponse;
import jobhunter.jobhunter.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<ResumeResponse>> createResume(@RequestBody ResumeRequest resumeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ResumeResponse>builder()
                        .message("resume created")
                        .error(null)
                        .data(resumeService.handleCreateResume(resumeRequest))
                        .build());
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResume(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResumeResponse>builder()
                        .message("get resume by id")
                        .error(null)
                        .statusCode("200")
                        .data(resumeService.handleGetResume(id))
                        .build());
    }

    @GetMapping("/resumes")
    public ResponseEntity<ApiResponse<ResultPagination>> getAllResumes(
            @Filter Specification<Resume> spec, Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .message("get all resumes")
                        .error(null)
                        .statusCode("200")
                        .data(resumeService.handleGetResumes(spec, pageable))
                        .build());
    }

    @PutMapping("/resumes")
    public ResponseEntity<ApiResponse<ResumeResponse>> updateResume(@RequestBody ResumeRequest resumeRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResumeResponse>builder()
                        .message("update resume")
                        .error(null)
                        .statusCode("200")
                        .data(resumeService.handleUpdateResume(resumeRequest))
                        .build());
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable("id") int id) {
        resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .error(null)
                        .statusCode("200")
                        .message("delete resume")
                        .data(null)
                        .build());
    }

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ApiResponse<ResultPagination>> getResumeByUser(Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .message("fetch resume by user")
                        .error(null)
                        .statusCode("200")
                        .data(resumeService.handleGetResumeByUser(pageable))
                        .build());
    }
}
