package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.jobhunter.domain.Skill;
import jobhunter.jobhunter.dto.skill.SkillRequest;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.skill.SkillResponse;
import jobhunter.jobhunter.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(@RequestBody @Valid SkillRequest skillRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SkillResponse>builder()
                        .data(skillService.handleCreateSkill(skillRequest))
                        .statusCode("201")
                        .message("Skill created")
                        .build());
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<ResultPagination>> getSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .message("all skills")
                        .error(null)
                        .data(skillService.handleGetSkills(spec, pageable))
                        .build());
    }


    @PutMapping("/skills")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(@RequestBody @Valid SkillRequest skillRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<SkillResponse>builder()
                        .data(skillService.handleUpdateSkill(skillRequest))
                        .statusCode("200")
                        .message("Skill updated")
                        .error(null)
                        .build());
    }


    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .data(null)
                        .statusCode("200")
                        .message("Skill deleted")
                        .error(null)
                        .build());
    }
}
